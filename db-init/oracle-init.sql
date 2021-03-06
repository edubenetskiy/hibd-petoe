CREATE USER orac IDENTIFIED BY orac;
GRANT ALL PRIVILEGES TO orac;
ALTER SESSION SET CURRENT_SCHEMA = orac;

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

CREATE TABLE person_job
(
    id   number PRIMARY KEY,
    name varchar2(100) NOT NULL
);

CREATE TABLE teacher
(
    id               number PRIMARY KEY,
    name             varchar2(100) NOT NULL,
    surname          varchar2(100) NOT NULL,
    patronymic       varchar2(100),
    date_of_birth    date   NOT NULL,
    country_of_birth varchar2(100) NOT NULL,
    city_of_birth    varchar2(100) NOT NULL,
    faculty_id       number NOT NULL,
    person_job_id    number NOT NULL,
    start_date       date   NOT NULL,
    end_date         date,
    FOREIGN KEY (person_job_id) REFERENCES person_job (id),
    FOREIGN KEY (faculty_id) REFERENCES faculty (id)
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
    study_form    varchar2(100) NOT NULL CHECK (study_form IN ('очная', 'заочная')),
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
    id               number PRIMARY KEY,
    study_type       varchar2(100) CHECK (study_type IN ('бюджет', 'контракт')),
    study_group_id   number NOT NULL,
    direction_id     number NOT NULL,
    speciality_id    number NOT NULL,
    name             varchar2(100) NOT NULL,
    surname          varchar2(100) NOT NULL,
    patronymic       varchar2(100),
    date_of_birth    date   NOT NULL,
    country_of_birth varchar2(100) NOT NULL,
    city_of_birth    varchar2(100) NOT NULL,
    faculty_id       number NOT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty (id),
    FOREIGN KEY (study_group_id) REFERENCES study_group (id),
    FOREIGN KEY (direction_id) REFERENCES direction (id),
    FOREIGN KEY (speciality_id) REFERENCES speciality (id)
);

CREATE TABLE subject
(
    id   number PRIMARY KEY,
    name varchar2(100) NOT NULL
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

CREATE TABLE record_book
(
    student_id  number NOT NULL,
    subject_id  number NOT NULL,
    mark        number NOT NULL,
    mark_letter varchar2(1) NOT NULL,
    exam_date   date   NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (subject_id) REFERENCES subject (id),
    CONSTRAINT pk_record_book PRIMARY KEY (student_id, subject_id),
    CONSTRAINT check_mark CHECK (
        mark BETWEEN 2
            AND 5
        )
);