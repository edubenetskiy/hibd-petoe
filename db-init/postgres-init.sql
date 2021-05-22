CREATE TABLE speciality
(
    id            SERIAL       NOT NULL,
    code          CHAR(8)      NOT NULL,
    name          VARCHAR(255) NOT NULL,
    qualification VARCHAR(40)  NOT NULL,
    standard_type                 CHAR(100)    NOT NULL,
    study_form            CHAR(100)    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT speciality_code UNIQUE (code)
);

INSERT INTO speciality (code,
                        name,
                        qualification,
                        standard_type,
                        study_form)
VALUES ('09.03.04',
        'Разработка программноинформационных систем',
        'Академический магистр',
        'новый',
        'очная');

CREATE TABLE students
(
    id         SERIAL       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    surname    VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255) NOT NULL,
    speciality_id      INTEGER      NOT NULL,
    CONSTRAINT speciality FOREIGN KEY (speciality_id) REFERENCES speciality (id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);

INSERT INTO students (name,
                      surname,
                      patronymic,
                      speciality_id)
VALUES ('Сидоров', 'Иван', 'Петрович', 1);

CREATE TABLE lecturers
(
    id         SERIAL       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    surname    VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO lecturers (name,
                       surname,
                       patronymic)
VALUES ('Смирнов', 'Павел', 'Сергеевич');

CREATE TABLE subjects
(
    id      SERIAL       NOT NULL,
    name    VARCHAR(255) NOT NULL,
    code    VARCHAR(20)  NOT NULL,
    lecture_hours        INTEGER      NOT NULL,
    workshop_hours       INTEGER      NOT NULL,
    laboratory_hours    INTEGER      NOT NULL,
    form_of_control CHAR(100)    NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO subjects (name,
                      code,
                      lecture_hours,
                      workshop_hours,
                      laboratory_hours,
                      form_of_control)
VALUES ('Компьютерная графика',
        '2018449043-И',
        10,
        10,
        10,
        'экзамен');

CREATE TABLE lecturers_to_subjects
(
    teacher_id INTEGER NOT NULL,
    subject_id  INTEGER NOT NULL,
    CONSTRAINT lecturers FOREIGN KEY (teacher_id) REFERENCES lecturers (id) ON DELETE CASCADE,
    CONSTRAINT subjects FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
);

INSERT INTO lecturers_to_subjects (teacher_id, subject_id)
VALUES ('1', '1');

CREATE TABLE specialities_to_subjects
(
    speciality_id INTEGER NOT NULL,
    subject_id    INTEGER NOT NULL,
    semester      INTEGER NOT NULL,
    CONSTRAINT speciality FOREIGN KEY (speciality_id) REFERENCES speciality (id) ON DELETE CASCADE,
    CONSTRAINT subjects FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    CONSTRAINT semester CHECK (
            semester > 0
            AND semester <= 10
        )
);

INSERT INTO specialities_to_subjects (speciality_id, subject_id, semester)
VALUES ('1', '1', '1');

CREATE TABLE record_book
(
    student_id         INTEGER NOT NULL,
    subject_id         INTEGER NOT NULL,
    teacher_id         INTEGER NOT NULL,
    points             INTEGER NOT NULL,
    date_of_completion DATE    NOT NULL,
    CONSTRAINT students FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT subjects FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE
        SET
        NULL,
    CONSTRAINT lecturers FOREIGN KEY (teacher_id) REFERENCES lecturers (id) ON DELETE
        SET
        NULL,
    CONSTRAINT points CHECK (
            points > 0
            AND points <= 100
        )
);

INSERT INTO record_book (student_id,
                         subject_id,
                         teacher_id,
                         points,
                         date_of_completion)
VALUES ('1',
        '1',
        '1',
        '100',
        '2021-04-28 11:21:00+03:00');
