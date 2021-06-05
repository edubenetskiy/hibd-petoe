import cx_Oracle
import radar
import string
import random
from random import randint, randrange, getrandbits

connection = None

NUM_OF_MEG = 1000;
NUM_OF_FACULTIES = 1000;
NUM_OF_PER_JOBS = 1000;
NUM_OF_TEACHERS = 1000;
NUM_OF_ST_GROUPS = 1000;
NUM_OF_SPECIALITIES = 1000;
NUM_OF_DIRECTIONS = 1000;
NUM_OF_STUDENTS = 1000;
NUM_OF_SUBJECTS = 1000;
NUM_OF_TIMETABLES = 1000;
NUM_OF_REC_BOOKS = 1000;

megafaculties = []
faculties = []
person_jobs = []
teachers = []
st_groups = []
specialities = []
directions = []
students = []
subjects = []
timetables = []
rec_books = []

def insert_megafaculty(megafaculties):

    sql = ('insert into megafaculty(id, name) '
        'values(:id,:name)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, megafaculties)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_megafaculty:')
        print(error)

def final_insert_megafaculty():
    for x in range(1, NUM_OF_MEG):
        name = 'megafacultyName' + str(x)
        megafaculties.append((x, name))
    insert_megafaculty(megafaculties)

def insert_faculty(faculties):

    sql = ('insert into faculty(id, name, megafaculty_id) '
        'values(:id,:name,:megafaculty_id)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, faculties)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_faculty:')
        print(error)

def final_insert_faculty():
    for x in range(1, NUM_OF_FACULTIES):
        name = 'facultyName' + str(x)
        megafaculty_id = randint(1, NUM_OF_MEG-1)
        faculties.append((x, name, megafaculty_id))
    insert_faculty(faculties)

def insert_person_jobs(person_jobs):

    sql = ('insert into person_job(id, name) '
        'values(:id,:name)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, person_jobs)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_person_jobs:')
        print(error)

def final_insert_person_jobs():
    for x in range(1, NUM_OF_PER_JOBS):
        name = 'jobName' + str(x)
        person_jobs.append((x, name))
    insert_person_jobs(person_jobs)

def insert_teachers(teachers):

    sql = ('insert into teacher(id, name, surname, patronymic, date_of_birth, country_of_birth, city_of_birth, faculty_id, person_job_id, start_date, end_date) '
        'values(:id,:name,:surname,:patronymic,:date_of_birth,:country_of_birth,:city_of_birth,:faculty_id,:person_job_id,:start_date,:end_date)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, teachers)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_teachers:')
        print(error)

def final_insert_teachers():
    for x in range(1, NUM_OF_TEACHERS):
        name = 'teacherName' + str(x)
        surname = 'teacherSurname' + str(x)
        patronymic = 'teacherPatronymic' + str(x)
        date_of_birth = radar.random_datetime(start='1910-03-24T23:59:59', stop='2021-05-24T23:59:59')
        country_of_birth = 'teacherCountry_of_birth' + str(x)
        city_of_birth = 'teacherCity_of_birth' + str(x)
        faculty_id = randint(1, NUM_OF_FACULTIES-1)
        person_job_id = randint(1, NUM_OF_PER_JOBS-1)
        start_date = radar.random_datetime(start='2021-03-24T23:59:59', stop='2021-05-24T23:59:59')
        end_date = radar.random_datetime(start='2021-03-24T23:59:59', stop='2021-05-24T23:59:59')
        teachers.append((x, name, surname, patronymic, date_of_birth, country_of_birth, city_of_birth, faculty_id, person_job_id, start_date, end_date))
    insert_teachers(teachers)

def insert_st_groups(st_groups):

    sql = ('insert into study_group(id, group_number, course_number, start_study_date, end_study_date) '
        'values(:id,:group_number,:course_number,:start_study_date,:end_study_date)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, st_groups)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_st_groups:')
        print(error)

def final_insert_st_groups():
    for x in range(1, NUM_OF_ST_GROUPS):
        group_number = 'group_number' + str(x)
        course_number = randint(1,6)
        start_study_date = radar.random_datetime(start='2020-03-24T23:59:59', stop='2021-05-24T23:59:59')
        end_study_date = radar.random_datetime(start='2020-03-24T23:59:59', stop='2021-05-24T23:59:59')
        st_groups.append((x, group_number, course_number, start_study_date, end_study_date))
    insert_st_groups(st_groups)

def insert_specialities(specialities):

    sql = ('insert into speciality(id, name, code, qualification, study_form) '
        'values(:id,:name,:code,:qualification,:study_form)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, specialities)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_specialities:')
        print(error)

def final_insert_specialities():
    for x in range(1, NUM_OF_SPECIALITIES):
        name = 'specialityName' + str(x)
        code = "".join( [random.choice(string.digits) for i in range(2)] ) + '.' + "".join( [random.choice(string.digits) for i in range(2)] ) + '.' + "".join( [random.choice(string.digits) for i in range(2)] )
        qualification = random.choice(['бакалавр', 'магистр', 'специалист'])
        study_form = random.choice(['очная', 'заочная'])
        specialities.append((x, name, code, qualification, study_form))
    insert_specialities(specialities)

def insert_directions(directions):

    sql = ('insert into direction(id, name, code) '
        'values(:id,:name,:code)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, directions)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_directions:')
        print(error)

def final_insert_directions():
    for x in range(1, NUM_OF_DIRECTIONS):
        name = 'directionName' + str(x)
        code = "".join( [random.choice(string.digits) for i in range(2)] ) + '.' + "".join( [random.choice(string.digits) for i in range(2)] ) + '.' + "".join( [random.choice(string.digits) for i in range(2)] )
        directions.append((x, name, code))
    insert_directions(directions)

def insert_students(students):

    sql = ('insert into student(id,study_type,study_group_id,direction_id,speciality_id,name,surname,patronymic,date_of_birth,country_of_birth,city_of_birth,faculty_id) '
        'values(:id,:study_type,:study_group_id,:direction_id,:speciality_id,:name,:surname,:patronymic,:date_of_birth,:country_of_birth,:city_of_birth,:faculty_id)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, students)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_students:')
        print(error)

def final_insert_students():
    for x in range(1, NUM_OF_STUDENTS):
        study_type = random.choice(['бюджет', 'контракт'])
        study_group_id = randint(1, NUM_OF_ST_GROUPS-1)
        direction_id = randint(1, NUM_OF_DIRECTIONS-1)
        speciality_id = randint(1, NUM_OF_SPECIALITIES-1)
        name = 'studentName' + str(x)
        surname = 'studentSurname' + str(x)
        patronymic = 'studentPatronymic' + str(x)
        date_of_birth = radar.random_datetime(start='1910-03-24T23:59:59', stop='2021-05-24T23:59:59')
        country_of_birth = 'teacherCountry_of_birth' + str(x)
        city_of_birth = 'teacherCity_of_birth' + str(x)
        faculty_id = randint(1, NUM_OF_FACULTIES-1)

        students.append((x,study_type,study_group_id,direction_id,speciality_id,name,surname,patronymic,date_of_birth,country_of_birth,city_of_birth,faculty_id))
    insert_students(students)

def insert_subjects(subjects):

    sql = ('insert into subject(id, name) '
        'values(:id,:name)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, subjects)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_subjects:')
        print(error)

def final_insert_subjects():
    for x in range(1, NUM_OF_SUBJECTS):
        name = 'subjectName' + str(x)
        subjects.append((x, name))
    insert_subjects(subjects)

def insert_timetables(timetables):

    sql = ('insert into timetable(id,time_slot,room_number,teacher_id,subject_id,study_group_id) '
        'values(:id,:time_slot,:room_number,:teacher_id,:subject_id,:study_group_id)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, timetables)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_timetables:')
        print(error)

def final_insert_timetables():
    for x in range(1, NUM_OF_TIMETABLES):
        time_slot = radar.random_datetime(start='1910-03-24T23:59:59', stop='2021-05-24T23:59:59')
        room_number = randint(100, 300)
        teacher_id = randint(1, NUM_OF_TEACHERS-1)
        subject_id = randint(1, NUM_OF_SUBJECTS-1)
        study_group_id = randint(1, NUM_OF_ST_GROUPS-1)
        timetables.append((x,time_slot,room_number,teacher_id,subject_id,study_group_id))
    insert_timetables(timetables)

def insert_rec_books(rec_books):

    sql = ('insert into record_book(student_id,subject_id,mark,mark_letter,exam_date) '
        'values(:student_id,:subject_id,:mark,:mark_letter,:exam_date)')

    try:
        with connection.cursor() as cursor:
            cursor.executemany(sql, rec_books)
            connection.commit()
    except cx_Oracle.Error as error:
        print('Error occurred in insert_rec_books:')
        print(error)

def final_insert_rec_books():
    for x in range(1, NUM_OF_REC_BOOKS):
        student_id = x
        subject_id = x
        mark = randint(2, 5)
        mark_letters = ['a', 'b', 'c', 'd', 'e']
        mark_letter = random.choice(mark_letters)
        exam_date = radar.random_datetime(start='1910-03-24T23:59:59', stop='2021-05-24T23:59:59')
        rec_books.append((student_id,subject_id,mark,mark_letter,exam_date))
    insert_rec_books(rec_books)

try:
    connection = cx_Oracle.connect(user='orac', password='orac', dsn='localhost:1522/XE', encoding='UTF-8')

    # show the version of the Oracle Database
    print(connection.version)
    final_insert_megafaculty()
    final_insert_faculty()
    final_insert_person_jobs()
    final_insert_teachers()
    final_insert_st_groups()
    final_insert_specialities()
    final_insert_directions()
    final_insert_students()
    final_insert_subjects()
    final_insert_timetables()
    final_insert_rec_books()


except cx_Oracle.Error as error:
    print(error)
finally:
    # release the connection
    if connection:
        connection.close()