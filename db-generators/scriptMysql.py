import pandas as pd
from faker import Faker
from collections import defaultdict
from sqlalchemy import create_engine
fake = Faker()
fake_data = defaultdict(list)
students = 10000
teachers = 10000
projects = 10000
conferences = 10000
publications = 10000
library_card = students
project_student = 11000
project_teacher = 11000
conferences_student = 11000
conferences_teacher = 11000
coauthors_student = 11000
coauthors_teacher = 1000

student_position = ['Bachelor','Master']
teacher_position = ['Graduate student','Teacher','Docent','Professor']
edition_lang = ['RU', 'EN']
edition_place = ['Saint Petersburg', 'Moscow']
edition_type = ['First', 'Revised']

for i in range(students):
    fake_data["student_id"].append( i+1 )
    fake_data["name"].append( fake.name() )
    fake_data["student_position"].append( student_position[ fake.random_int(0, (len(student_position)-1)) ] )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('students', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)

for i in range(teachers):
    fake_data["teacher_id"].append( i+1 )
    fake_data["name"].append( fake.name() )
    fake_data["teacher_position"].append( teacher_position[ fake.random_int(0, (len(teacher_position)-1)) ] )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('teachers', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)

for i in range(projects):
    fake_data["id"].append( i+1 )
    fake_data["name"].append(' '.join( fake.words(5) ).capitalize() ) 
    fake_data["start_date"].append( fake.past_date() )
    fake_data["end_date"].append( fake.future_date() )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('projects', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)

for i in range(project_student):
    fake_data["student_id"].append( fake.random_int(1, students) )
    fake_data["project_id"].append( fake.random_int(1, projects) )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('project_student', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)

for i in range(project_teacher):
    fake_data["teacher_id"].append( fake.random_int(1, teachers) )
    fake_data["project_id"].append( fake.random_int(1, projects) )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('project_teacher', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)    

for i in range(publications):
    fake_data["id"].append( i+1 )
    fake_data["name"].append( (' '.join( fake.words(fake.random_int(1, 5)) ).capitalize() ) )
    fake_data["edition_lang"].append( edition_lang[ fake.random_int(0, (len(edition_lang)-1)) ] )
    fake_data["edition_vol"].append( fake.random_int(1, 5) )
    fake_data["edition_place"].append( edition_place[ fake.random_int(0, (len(edition_place)-1)) ] )
    fake_data["edition_type"].append( edition_type[ fake.random_int(0, (len(edition_type)-1)) ] )
    fake_data["citation_index"].append( fake.random_int(1, 15) )
    fake_data["publication_date"].append( fake.past_date() )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('publications', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)    

for i in range(coauthors_student):
    fake_data["student_id"].append( fake.random_int(1, students) )
    fake_data["publication_id"].append( fake.random_int(1, publications) )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('coauthors_student', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)    

for i in range(coauthors_teacher):
    fake_data["teacher_id"].append( fake.random_int(1, teachers) )
    fake_data["publication_id"].append( fake.random_int(1, publications) )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('coauthors_teacher', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)    

for i in range(conferences):
    fake_data["id"].append( i+1 )
    fake_data["name"].append( (' '.join( fake.words(fake.random_int(1, 5)) ).capitalize() ) )
    fake_data["place"].append( edition_place[ fake.random_int(0, (len(edition_place)-1)) ] )
    fake_data["conference_date"].append( fake.past_date() )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('conferences', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)    

for i in range(conferences_student):
    fake_data["student_id"].append( fake.random_int(1, students) )
    fake_data["conference_id"].append( fake.random_int(1, conferences) )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('conferences_student', con=engine,index=False)

fake = Faker()
fake_data = defaultdict(list)    

for i in range(conferences_teacher):
    fake_data["teacher_id"].append( fake.random_int(1, teachers) )
    fake_data["conference_id"].append( fake.random_int(1, conferences) )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('conferences_teacher', con=engine,index=False)


fake = Faker()
fake_data = defaultdict(list)    

for i in range(library_card):
    fake_data["id"].append( fake.random_int(1, students) )
    fake_data["book_name"].append( (' '.join( fake.words(fake.random_int(1, 5)) ).capitalize() ) )
    fake_data["receive_date"].append( fake.past_date() )
    fake_data["return_date"].append( fake.future_date() )

df_fake_data = pd.DataFrame(fake_data)

engine = create_engine('mysql://root:admin@localhost:3306/mysqldb', echo=False)
df_fake_data.to_sql('library_card', con=engine,index=False)

