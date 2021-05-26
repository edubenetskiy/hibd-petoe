ALTER SESSION SET CURRENT_SCHEMA = hibd;

CREATE TABLE megafaculty
(
    id   number,
    name varchar2(100)
);

CREATE TABLE faculty
(
    id             number,
    name           varchar2(100),
    megafaculty_id number
);

CREATE TABLE person_job
(
    id   number,
    name varchar2(100)
);

CREATE TABLE teacher
(
    id               number,
    name             varchar2(100),
    surname          varchar2(100),
    patronymic       varchar2(100),
    date_of_birth    date,
    country_of_birth varchar2(100),
    city_of_birth    varchar2(100),
    person_position  varchar2(100),
    faculty_id       number,
    person_job_id    number,
    start_date       date,
    end_date         date
);

CREATE TABLE study_group
(
    id               number,
    group_number     varchar2(100),
    course_number    number,
    start_study_date date,
    end_study_date   date
);

CREATE TABLE speciality
(
    id            number,
    name          varchar2(1000),
    code          varchar2(1000),
    qualification varchar2(1000),
    standard_type varchar2(1000),
    study_form    varchar2(1000),
    faculty_id    number
);

CREATE TABLE direction
(
    id   number,
    name varchar2(1000),
    code varchar2(1000)
);

CREATE TABLE student
(
    id               number,
    study_type       varchar2(1000),
    is_privileged    number(1, 0),
    study_group_id   number,
    direction_id     number,
    speciality_id    number,
    name             varchar2(100),
    surname          varchar2(100),
    patronymic       varchar2(100),
    date_of_birth    date,
    country_of_birth varchar2(100),
    city_of_birth    varchar2(100),
    person_position  varchar2(100),
    faculty_id       number
);

CREATE TABLE subject
(
    id               number,
    name             varchar2(1000),
    code             varchar2(1000),
    lecture_hours    number,
    workshop_hours   number,
    laboratory_hours number,
    form_of_control  varchar2(1000)
);

CREATE TABLE timetable
(
    id             number,
    time_slot      timestamp,
    room_number    number,
    teacher_id     number,
    subject_id     number,
    study_group_id number
);

CREATE TABLE conference
(
    id              number,
    name            varchar2(1000),
    place           varchar2(1000),
    conference_date date
);

CREATE TABLE publication
(
    id               number,
    name             varchar2(1000),
    edition_lang     varchar2(1000),
    edition_vol      number,
    edition_place    varchar2(1000),
    edition_type     varchar2(1000),
    citation_index   number,
    publication_date date
);

CREATE TABLE project
(
    id         number,
    name       varchar2(1000),
    start_date date,
    end_date   date
);

CREATE TABLE library_card
(
    id           number,
    book_name    varchar2(1000),
    receive_date date,
    return_date  date
);

CREATE TABLE project_student
(
    student_id number,
    project_id number
);

CREATE TABLE project_teacher
(
    teacher_id number,
    project_id number
);

CREATE TABLE coauthors_student
(
    student_id     number,
    publication_id number
);

CREATE TABLE coauthors_teacher
(
    teacher_id     number,
    publication_id number
);

CREATE TABLE conference_student
(
    student_id    number,
    conference_id number
);

CREATE TABLE conference_teacher
(
    teacher_id    number,
    conference_id number
);


CREATE TABLE dorm
(
    id              number,
    address         varchar2(1000),
    number_of_rooms number
);

CREATE TABLE room_type
(
    id               number,
    number_of_places number,
    price            number
);

CREATE TABLE room
(
    id                        number,
    dorm_id                   number,
    room_type_id              number,
    number_of_occupied_places number,
    does_have_bugs            number(1, 0),
    date_of_last_disinfection date
);

CREATE TABLE student_dorm_info
(
    id                      number,
    student_id              number,
    room_id                 number,
    date_of_entry           date,
    date_of_contract_expire date,
    number_of_warnings      number,
    paid_till               date,
    last_check_in           date,
    last_check_out          date
);

CREATE TABLE teacher_subject
(
    teacher_id number,
    subject_id number
);

CREATE TABLE speciality_subject
(
    speciality_id number,
    subject_id    number,
    semester      number
);

CREATE TABLE record_book
(
    student_id         number,
    subject_id         number,
    teacher_id         number,
    points             number,
    date_of_completion date,
    mark               number,
    mark_letter        varchar2(1),
    exam_date          date
);
