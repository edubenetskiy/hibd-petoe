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
    id             number PRIMARY KEY,
    study_type     varchar2(100) CHECK (study_type IN ('бюджет', 'контракт')),
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

INSERT INTO megafaculty(id, name)
VALUES (1, 'КТиУ');

INSERT INTO megafaculty(id, name)
VALUES (2, 'ТИнТ');

INSERT INTO faculty(id, name, megafaculty_id)
VALUES (1, 'ПИиКТ', 1);

INSERT INTO faculty(id, name, megafaculty_id)
VALUES (2, 'ФИТиП', 2);

INSERT INTO person(id,
                   name,
                   surname,
                   patronymic,
                   date_of_birth,
                   country_of_birth,
                   city_of_birth,
                   faculty_id)
VALUES (1,
        'Иван',
        'Иванов',
        'Иванович',
        '01-JAN-1997',
        'Россия',
        'Москва',
        1);

INSERT INTO person(id,
                   name,
                   surname,
                   patronymic,
                   date_of_birth,
                   country_of_birth,
                   city_of_birth,
                   faculty_id)
VALUES (2,
        'Владимир',
        'Владимирв',
        'Владимирович',
        '01-JAN-1995',
        'Россия',
        'Санкт-Петербург',
        2);

INSERT INTO person(id,
                   name,
                   surname,
                   patronymic,
                   date_of_birth,
                   country_of_birth,
                   city_of_birth,
                   faculty_id)
VALUES (3,
        'Петр',
        'Петров',
        'Петрович',
        '01-JAN-1996',
        'Россия',
        'Уфа',
        1);

INSERT INTO person_job(id, name)
VALUES (1, 'доцент');

INSERT INTO person_job(id, name)
VALUES (2, 'профессор');

INSERT INTO teacher(id,
                    person_id,
                    person_job_id,
                    start_date,
                    end_date)
VALUES (1, 1, 1, '01-JAN-2000', '01-JAN-2001');

INSERT INTO study_group(id,
                        group_number,
                        course_number,
                        start_study_date,
                        end_study_date)
VALUES (1, 'P41141', 5, '03-JAN-2000', '03-JAN-2001');

INSERT INTO speciality(id, name, code, qualification, study_form)
VALUES (1,
        'Разработка программно-информационных систем',
        '09.03.04',
        'бакалавр',
        'очная');

INSERT INTO direction(id, name, code)
VALUES (1, 'Программная инженерия', '09.04.04');

INSERT INTO student(id,
                    study_type,
                    study_group_id,
                    direction_id,
                    speciality_id,
                    person_id)
VALUES (1, 'бюджет', 1, 1, 1, 2);

INSERT INTO subject(id, name)
VALUES (1, 'Базы данных');

INSERT INTO timetable(id,
                      time_slot,
                      room_number,
                      teacher_id,
                      subject_id,
                      study_group_id)
VALUES (1, '26-JUN-02 09:00', 100, 1, 1, 1);

INSERT INTO record_book(student_id,
                        subject_id,
                        mark,
                        mark_letter,
                        exam_date)
VALUES (1, 1, 5, 'A', '01-JAN-2000');
