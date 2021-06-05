CREATE USER orac3rd IDENTIFIED BY orac3rd;
GRANT ALL PRIVILEGES TO orac3rd;
ALTER SESSION SET CURRENT_SCHEMA = orac3rd;

create table birthPlace(
    id number PRIMARY KEY,
    district varchar2(1000) NOT NULL,
    city varchar2(1000) NOT NULL,
    country varchar2(1000) NOT NULL
);

create table db_time(
    id number PRIMARY KEY,
    term number NOT NULL,
    year number NOT NULL
);

create table publisher(
    id number PRIMARY KEY,
    publisher varchar2(1000) NOT NULL,
    city varchar2(1000) NOT NULL,
    country varchar2(1000) NOT NULL
);

create table campus(
    id number PRIMARY KEY,
    address varchar2(1000) NOT NULL
);

create table factTable1(
    id number PRIMARY KEY,
    diploma_with_honor number NOT NULL,
    usual_diploma number NOT NULL,
    num_of_publications number NOT NULL,
    num_of_student_lib_cards number NOT NULL,
    num_of_conferences number NOT NULL,
    num_of_employee_lib_cards number NOT NULL,
    people_live_in_campus number NOT NULL,
    people_not_live_in_campus number NOT NULL,
    timeId number NOT NULL,
    FOREIGN KEY (timeId) REFERENCES db_time(id)
);

create table factTable2(
    id number PRIMARY KEY,
    num_of_people_1st_course number NOT NULL,
    birthPlaceId number NOT NULL,
    timeId number NOT NULL,
    FOREIGN KEY (timeId) REFERENCES db_time(id),
    FOREIGN KEY (birthPlaceId) REFERENCES birthPlace(id)
);

create table factTable3(
    id number PRIMARY KEY,
    num_of_people_master number NOT NULL,
    publisher_id number NOT NULL,
    timeId number NOT NULL,
    FOREIGN KEY (timeId) REFERENCES db_time(id),
    FOREIGN KEY (publisher_id) REFERENCES publisher(id)
);

create table factTable4(
    id number PRIMARY KEY,
    avg_num_of_persons_in_one_room number NOT NULL,
    students_with_only_5_marks number NOT NULL,
    students_with_only_4_5_marks number NOT NULL,
    students_with_3_4_5_marks number NOT NULL,
    num_of_students_with_debts number NOT NULL,
    campusId number NOT NULL,
    timeId number NOT NULL,
    FOREIGN KEY (timeId) REFERENCES db_time(id),
    FOREIGN KEY (campusId) REFERENCES campus(id)
);