CREATE USER hibd IDENTIFIED BY hibd;
GRANT ALL PRIVILEGES TO hibd;
ALTER SESSION SET CURRENT_SCHEMA = hibd;

CREATE TABLE megafaculty
(
    id   number PRIMARY KEY,
    name varchar2(100) NOT NULL
);

CREATE TABLE faculty
(
    id             number PRIMARY KEY,
    name           varchar2(100) NOT NULL,
    megafaculty_id number,
    FOREIGN KEY (megafaculty_id) REFERENCES megafaculty (id)
);

CREATE TABLE person
(
    id               number PRIMARY KEY,
    name             varchar2(100) NOT NULL,
    surname          varchar2(100) NOT NULL,
    patronymic       varchar2(100),
    date_of_birth    date   NOT NULL,
    country_of_birth varchar2(100) NOT NULL,
    city_of_birth    varchar2(100) NOT NULL,
    person_position  varchar2(100),
    faculty_id       number NOT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty (id)
);

CREATE TABLE person_job
(
    id   number PRIMARY KEY,
    name varchar2(100) NOT NULL
);

CREATE TABLE teacher
(
    id            number PRIMARY KEY,
    person_id     number NOT NULL,
    person_job_id number NOT NULL,
    start_date    date   NOT NULL,
    end_date      date,
    FOREIGN KEY (person_id) REFERENCES person (id),
    FOREIGN KEY (person_job_id) REFERENCES person_job (id)
);

CREATE TABLE study_group
(
    id               number PRIMARY KEY,
    group_number     varchar2(100) NOT NULL,
    course_number    number NOT NULL,
    start_study_date date   NOT NULL,
    end_study_date   date
);

CREATE TABLE speciality
(
    id            number PRIMARY KEY,
    name          varchar2(1000) NOT NULL,
    code          varchar2(100) NOT NULL,
    qualification varchar2(100) NOT NULL CHECK (
        qualification IN ('бакалавр', 'магистр', 'специалист')
        ),
    standard_type varchar2(100) NOT NULL CHECK (standard_type IN ('старый', 'новый')),
    study_form    varchar2(100) NOT NULL CHECK (study_form IN ('очная', 'заочная')),
    faculty_id    number NOT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty (id),
    CONSTRAINT check_specialty_code CHECK (
        REGEXP_LIKE(code, '[0-9]{2}\.[0-9]{2}\.[0-9]{2}')
        )
);

CREATE TABLE direction
(
    id   number PRIMARY KEY,
    name varchar2(1000) NOT NULL,
    code varchar2(100) NOT NULL,
    CONSTRAINT check_direction_code CHECK (
        REGEXP_LIKE(code, '[0-9]{2}\.[0-9]{2}\.[0-9]{2}')
        )
);

CREATE TABLE student
(
    id             number PRIMARY KEY,
    study_type     varchar2(100) CHECK (study_type IN ('бюджет', 'контракт')),
    is_privileged  number(1, 0) NOT NULL,
    study_group_id number NOT NULL,
    direction_id   number NOT NULL,
    speciality_id  number NOT NULL,
    person_id      number NOT NULL,
    FOREIGN KEY (study_group_id) REFERENCES study_group (id),
    FOREIGN KEY (direction_id) REFERENCES direction (id),
    FOREIGN KEY (speciality_id) REFERENCES speciality (id),
    FOREIGN KEY (person_id) REFERENCES person (id)
);

CREATE TABLE subject
(
    id               number PRIMARY KEY,
    name             varchar2(100) NOT NULL,
    code             varchar2(100) NOT NULL,
    lecture_hours    number NOT NULL,
    workshop_hours   number NOT NULL,
    laboratory_hours number NOT NULL,
    form_of_control  varchar2(100) NOT NULL
);

CREATE TABLE timetable
(
    id             number PRIMARY KEY,
    time_slot      timestamp NOT NULL,
    room_number    number    NOT NULL,
    teacher_id     number    NOT NULL,
    subject_id     number    NOT NULL,
    study_group_id number    NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id),
    FOREIGN KEY (subject_id) REFERENCES subject (id),
    FOREIGN KEY (study_group_id) REFERENCES study_group (id)
);

CREATE TABLE conference
(
    id              number PRIMARY KEY,
    name            varchar2(1000) NOT NULL,
    place           varchar2(1000) NOT NULL,
    conference_date date NOT NULL
);

CREATE TABLE publication
(
    id               number PRIMARY KEY,
    name             varchar2(1000) NOT NULL,
    edition_lang     varchar2(1000) NOT NULL,
    edition_vol      number NOT NULL,
    edition_place    varchar2(1000) NOT NULL,
    edition_type     varchar2(1000) NOT NULL,
    citation_index   number NOT NULL,
    publication_date date   NOT NULL
);

CREATE TABLE project
(
    id         number PRIMARY KEY,
    name       varchar2(1000) NOT NULL,
    start_date date NOT NULL,
    end_date   date NOT NULL
);

CREATE TABLE library_card
(
    id           number PRIMARY KEY,
    book_name    varchar2(1000) NOT NULL,
    receive_date date NOT NULL,
    return_date  date NOT NULL
);

CREATE TABLE project_participant
(
    person_id  number NOT NULL,
    project_id number NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person (id),
    FOREIGN KEY (project_id) REFERENCES project (id),
    CONSTRAINT pk_project_participant PRIMARY KEY (person_id, project_id)
);

CREATE TABLE coauthors
(
    person_id      number NOT NULL,
    publication_id number NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person (id),
    FOREIGN KEY (publication_id) REFERENCES publication (id),
    CONSTRAINT pk_coauthors PRIMARY KEY (person_id, publication_id)
);

CREATE TABLE conference_participant
(
    person_id     number NOT NULL,
    conference_id number NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person (id),
    FOREIGN KEY (conference_id) REFERENCES conference (id),
    CONSTRAINT pk_conference_participant PRIMARY KEY (person_id, conference_id)
);

CREATE TABLE dorm
(
    id              number PRIMARY KEY,
    address         varchar2(1000) NOT NULL,
    number_of_rooms number NOT NULL
);

CREATE TABLE room_type
(
    id               number PRIMARY KEY,
    number_of_places number NOT NULL,
    price            number NOT NULL
);

CREATE TABLE room
(
    id                        number PRIMARY KEY,
    dorm_id                   number NOT NULL,
    room_type_id              number NOT NULL,
    number_of_occupied_places number NOT NULL,
    does_have_bugs            number(1, 0) NOT NULL,
    date_of_last_disinfection date,
    FOREIGN KEY (dorm_id) REFERENCES dorm (id),
    FOREIGN KEY (room_type_id) REFERENCES room_type (id)
);

CREATE TABLE student_dorm_info
(
    id                      number PRIMARY KEY,
    student_id              number NOT NULL,
    room_id                 number NOT NULL,
    date_of_entry           date   NOT NULL,
    date_of_contract_expire date   NOT NULL,
    number_of_warnings      number NOT NULL,
    paid_till               date   NOT NULL,
    last_check_in           date,
    last_check_out          date,
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (room_id) REFERENCES room (id)
);

CREATE TABLE teacher_subject
(
    teacher_id number NOT NULL,
    subject_id number NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id),
    FOREIGN KEY (subject_id) REFERENCES subject (id),
    CONSTRAINT pk_teacher_subject PRIMARY KEY (teacher_id, subject_id)
);

CREATE TABLE speciality_subject
(
    speciality_id number NOT NULL,
    subject_id    number NOT NULL,
    semester      number NOT NULL,
    FOREIGN KEY (speciality_id) REFERENCES speciality (id),
    FOREIGN KEY (subject_id) REFERENCES subject (id),
    CONSTRAINT pk_speciality_subject PRIMARY KEY (speciality_id, subject_id)
);

CREATE TABLE record_book
(
    student_id         number NOT NULL,
    subject_id         number NOT NULL,
    teacher_id         number NOT NULL,
    points             number NOT NULL,
    date_of_completion date,
    mark               number NOT NULL,
    mark_letter        varchar2(1) NOT NULL,
    exam_date          date   NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (subject_id) REFERENCES subject (id),
    FOREIGN KEY (teacher_id) REFERENCES teacher (id),
    CONSTRAINT pk_record_book PRIMARY KEY (student_id, subject_id, teacher_id),
    CONSTRAINT check_mark CHECK (
        mark BETWEEN 2
            AND 5
        )
);
