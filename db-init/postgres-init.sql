CREATE TABLE speciality
(
    speciality_id            SERIAL       NOT NULL,
    speciality_code          CHAR(8)      NOT NULL,
    speciality_name          VARCHAR(255) NOT NULL,
    speciality_qualification VARCHAR(40)  NOT NULL,
    standard                 CHAR(100)    NOT NULL,
    form_of_study            CHAR(100)    NOT NULL,
    place_of_study           VARCHAR(255) NOT NULL,
    PRIMARY KEY (speciality_id),
    CONSTRAINT speciality_code UNIQUE (speciality_code)
);

INSERT INTO speciality (speciality_code,
                        speciality_name,
                        speciality_qualification,
                        standard,
                        form_of_study,
                        place_of_study)
VALUES ('09.03.04',
        'Разработка программноинформационных систем',
        'Академический магистр',
        'новый',
        'очная',
        'ПИиКТ');

CREATE TABLE students
(
    student_id         SERIAL       NOT NULL,
    student_name       VARCHAR(255) NOT NULL,
    student_surname    VARCHAR(255) NOT NULL,
    student_patronymic VARCHAR(255) NOT NULL,
    speciality_id      INTEGER      NOT NULL,
    CONSTRAINT speciality FOREIGN KEY (speciality_id) REFERENCES speciality (speciality_id) ON DELETE CASCADE,
    PRIMARY KEY (student_id)
);

INSERT INTO students (student_name,
                      student_surname,
                      student_patronymic,
                      speciality_id)
VALUES ('Сидоров', 'Иван', 'Петрович', 1);

CREATE TABLE lecturers
(
    lecturer_id         SERIAL       NOT NULL,
    lecturer_name       VARCHAR(255) NOT NULL,
    lecturer_surname    VARCHAR(255) NOT NULL,
    lecturer_patronymic VARCHAR(255) NOT NULL,
    PRIMARY KEY (lecturer_id)
);

INSERT INTO lecturers (lecturer_name,
                       lecturer_surname,
                       lecturer_patronymic)
VALUES ('Смирнов', 'Павел', 'Сергеевич');

CREATE TABLE subjects
(
    subject_id      SERIAL       NOT NULL,
    subject_name    VARCHAR(255) NOT NULL,
    subject_code    VARCHAR(20)  NOT NULL,
    lectures        INTEGER      NOT NULL,
    workshops       INTEGER      NOT NULL,
    laboratories    INTEGER      NOT NULL,
    form_of_control CHAR(100)    NOT NULL,
    PRIMARY KEY (subject_id)
);

INSERT INTO subjects (subject_name,
                      subject_code,
                      lectures,
                      workshops,
                      laboratories,
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
    CONSTRAINT lecturers FOREIGN KEY (teacher_id) REFERENCES lecturers (lecturer_id) ON DELETE CASCADE,
    CONSTRAINT subjects FOREIGN KEY (subject_id) REFERENCES subjects (subject_id) ON DELETE CASCADE
);

INSERT INTO lecturers_to_subjects (teacher_id, subject_id)
VALUES ('1', '1');

CREATE TABLE specialities_to_subjects
(
    speciality_id INTEGER NOT NULL,
    subject_id    INTEGER NOT NULL,
    semester      INTEGER NOT NULL,
    CONSTRAINT speciality FOREIGN KEY (speciality_id) REFERENCES speciality (speciality_id) ON DELETE CASCADE,
    CONSTRAINT subjects FOREIGN KEY (subject_id) REFERENCES subjects (subject_id) ON DELETE CASCADE,
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
    CONSTRAINT students FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT subjects FOREIGN KEY (subject_id) REFERENCES subjects (subject_id) ON DELETE
        SET
        NULL,
    CONSTRAINT lecturers FOREIGN KEY (teacher_id) REFERENCES lecturers (lecturer_id) ON DELETE
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
