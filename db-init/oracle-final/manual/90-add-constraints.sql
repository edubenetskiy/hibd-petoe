ALTER TABLE megafaculty
    MODIFY (id PRIMARY KEY,
            name NOT NULL);

ALTER TABLE faculty
    MODIFY (id PRIMARY KEY,
            name NOT NULL)
    ADD FOREIGN KEY (megafaculty_id) REFERENCES megafaculty (id);

ALTER TABLE person
    MODIFY (id PRIMARY KEY,
            name NOT NULL,
            surname NOT NULL,
            date_of_birth NOT NULL,
            country_of_birth NOT NULL,
            city_of_birth NOT NULL,
            faculty_id NOT NULL)
    ADD FOREIGN KEY (faculty_id) REFERENCES faculty (id);

ALTER TABLE person_job
    MODIFY (id PRIMARY KEY,
            name NOT NULL);

ALTER TABLE teacher
    MODIFY (id PRIMARY KEY,
            person_id NOT NULL,
            person_job_id NOT NULL,
            start_date NOT NULL,
            end_date NOT NULL)
    ADD FOREIGN KEY (person_id) REFERENCES person (id)
    ADD FOREIGN KEY (person_job_id) REFERENCES person_job (id);

ALTER TABLE study_group
    MODIFY (id PRIMARY KEY,
            group_number NOT NULL,
            course_number NOT NULL,
            start_study_date NOT NULL);

ALTER TABLE speciality
    MODIFY (id PRIMARY KEY,
            name NOT NULL,
            code NOT NULL,
            qualification NOT NULL CHECK (
                qualification IN ('бакалавр', 'магистр', 'специалист')),
            standard_type NOT NULL CHECK (standard_type IN ('старый', 'новый')),
            study_form NOT NULL CHECK (study_form IN ('очная', 'заочная')),
            faculty_id NOT NULL)
    ADD FOREIGN KEY (faculty_id) REFERENCES faculty (id)
    ADD CONSTRAINT check_speciality_code
        CHECK (REGEXP_LIKE(code, '[0-9]{2}\.[0-9]{2}\.[0-9]{2}'));

ALTER TABLE direction
    MODIFY (
        id PRIMARY KEY,
        name NOT NULL,
        code NOT NULL)
    ADD CONSTRAINT check_direction_code
        CHECK (REGEXP_LIKE(code, '[0-9]{2}\.[0-9]{2}\.[0-9]{2}'));

ALTER TABLE student
    MODIFY (
        id PRIMARY KEY,
        study_type CHECK (study_type IN ('бюджет', 'контракт')),
        is_privileged NOT NULL,
        study_group_id NOT NULL,
        direction_id NOT NULL,
        speciality_id NOT NULL,
        person_id NOT NULL)
    ADD FOREIGN KEY (study_group_id) REFERENCES study_group (id)
    ADD FOREIGN KEY (direction_id) REFERENCES direction (id)
    ADD FOREIGN KEY (speciality_id) REFERENCES speciality (id)
    ADD FOREIGN KEY (person_id) REFERENCES person (id);

ALTER TABLE subject
    MODIFY (id PRIMARY KEY,
            name NOT NULL,
            code NOT NULL,
            lecture_hours NOT NULL,
            workshop_hours NOT NULL,
            laboratory_hours NOT NULL,
            form_of_control NOT NULL);

ALTER TABLE timetable
    MODIFY (
        id PRIMARY KEY,
        time_slot NOT NULL,
        room_number NOT NULL,
        teacher_id NOT NULL,
        subject_id NOT NULL,
        study_group_id NOT NULL)
    ADD FOREIGN KEY (teacher_id) REFERENCES teacher (id)
    ADD FOREIGN KEY (subject_id) REFERENCES subject (id)
    ADD FOREIGN KEY (study_group_id) REFERENCES study_group (id);

ALTER TABLE conference
    MODIFY (id PRIMARY KEY,
            name NOT NULL,
            place NOT NULL,
            conference_date NOT NULL);

ALTER TABLE publication
    MODIFY (id PRIMARY KEY,
            name NOT NULL,
            edition_lang NOT NULL,
            edition_vol NOT NULL,
            edition_place NOT NULL,
            edition_type NOT NULL,
            citation_index NOT NULL,
            publication_date NOT NULL);

ALTER TABLE project
    MODIFY (
        id PRIMARY KEY,
        name NOT NULL,
        start_date NOT NULL,
        end_date NOT NULL);

ALTER TABLE library_card
    MODIFY (id PRIMARY KEY,
            book_name NOT NULL,
            receive_date NOT NULL,
            return_date NOT NULL);

ALTER TABLE project_participant
    MODIFY (
        person_id NOT NULL,
        project_id NOT NULL)
    ADD FOREIGN KEY (person_id) REFERENCES person (id)
    ADD FOREIGN KEY (project_id) REFERENCES project (id)
    ADD CONSTRAINT pk_project_participant PRIMARY KEY (person_id, project_id);

ALTER TABLE coauthors
    MODIFY (
        person_id NOT NULL,
        publication_id NOT NULL)
    ADD FOREIGN KEY (person_id) REFERENCES person (id)
    ADD FOREIGN KEY (publication_id) REFERENCES publication (id)
    ADD CONSTRAINT pk_coauthors PRIMARY KEY (person_id, publication_id);

ALTER TABLE conference_participant
    MODIFY (
        person_id NOT NULL,
        conference_id NOT NULL)
    ADD FOREIGN KEY (person_id) REFERENCES person (id)
    ADD FOREIGN KEY (conference_id) REFERENCES conference (id)
    ADD CONSTRAINT pk_conference_participant PRIMARY KEY (person_id, conference_id);

ALTER TABLE dorm
    MODIFY (id PRIMARY KEY,
            address NOT NULL,
            number_of_rooms NOT NULL);

ALTER TABLE room_type
    MODIFY (id PRIMARY KEY,
            number_of_places NOT NULL,
            price NOT NULL);

ALTER TABLE room
    MODIFY (
        id PRIMARY KEY,
        dorm_id NOT NULL,
        room_type_id NOT NULL,
        number_of_occupied_places NOT NULL,
        does_have_bugs NOT NULL)
    ADD FOREIGN KEY (dorm_id) REFERENCES dorm (id)
    ADD FOREIGN KEY (room_type_id) REFERENCES room_type (id);

ALTER TABLE student_dorm_info
    MODIFY (
        id PRIMARY KEY,
        student_id NOT NULL,
        room_id NOT NULL,
        date_of_entry NOT NULL,
        date_of_contract_expire NOT NULL,
        number_of_warnings NOT NULL,
        paid_till NOT NULL)
    ADD FOREIGN KEY (student_id) REFERENCES student (id)
    ADD FOREIGN KEY (room_id) REFERENCES room (id);

ALTER TABLE teacher_subject
    MODIFY (
        teacher_id NOT NULL,
        subject_id NOT NULL)
    ADD FOREIGN KEY (teacher_id) REFERENCES teacher (id)
    ADD FOREIGN KEY (subject_id) REFERENCES subject (id)
    ADD CONSTRAINT pk_teacher_subject PRIMARY KEY (teacher_id, subject_id);

ALTER TABLE speciality_subject
    MODIFY (
        speciality_id NOT NULL,
        subject_id NOT NULL,
        semester NOT NULL)
    ADD FOREIGN KEY (speciality_id) REFERENCES speciality (id)
    ADD FOREIGN KEY (subject_id) REFERENCES subject (id)
    ADD CONSTRAINT pk_speciality_subject PRIMARY KEY (speciality_id, subject_id);

ALTER TABLE record_book
    MODIFY (
        student_id NOT NULL,
        subject_id NOT NULL,
        teacher_id NOT NULL,
        points NOT NULL,
        mark NOT NULL,
        mark_letter NOT NULL,
        exam_date NOT NULL)
    ADD FOREIGN KEY (student_id) REFERENCES student (id)
    ADD FOREIGN KEY (subject_id) REFERENCES subject (id)
    ADD FOREIGN KEY (teacher_id) REFERENCES teacher (id)
    ADD CONSTRAINT check_mark CHECK (mark BETWEEN 2 AND 5)
    ADD CONSTRAINT pk_record_book PRIMARY KEY (student_id, subject_id, teacher_id);
