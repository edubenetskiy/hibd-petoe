from pymongo import MongoClient
from random import randint, randrange, getrandbits
import radar

#Step 1: Connect to MongoDB - Note: Change connection string as needed
client = MongoClient('mongodb://localhost:27017/', username="admin", password="admin")
db=client.test

NUM_OF_DORMS =  1000
NUM_OF_ROOM_TYPES = 1000
NUM_OF_ROOMS = 1000
NUM_OF_STUDENTS = 1000

#Create dorms
for x in range(1, NUM_OF_DORMS):
    row = {
    	'_id' : x,
    	'address' : 'string dorm address ' + str(x),
        'number_of_rooms' : randint(40, 500)
    }
    result=db.dorms.insert_one(row)

#Create 300 room_types
for x in range(1, NUM_OF_ROOM_TYPES):
    row = {
    	'_id' : x,
    	'number_of_places' : randint(1,4),
        'price' : randrange(600, 5000)
    }
    result=db.room_types.insert_one(row)

#Create rooms
for x in range(1, NUM_OF_ROOMS):
    row = {
    	'_id' : x,
    	'dorm_id' : randint(1,NUM_OF_DORMS),
        'room_type_id' : randint(1,NUM_OF_ROOM_TYPES),
        'number_of_occupied_places' : randint(0,4),
        'does_have_bugs' : bool(getrandbits(1)),
        'date_of_last_desinfection' : radar.random_datetime(start='2016-05-24T23:59:59', stop='2020-05-24T23:59:59')
    }
    result=db.rooms.insert_one(row)

#Create students
study_forms = ['FREE','SELF-FUNDED']
for x in range(1, NUM_OF_STUDENTS):
    row = {
    	'_id' : x,
    	'name' : 'string student name ' + str(x),
        'is_privileged' : bool(getrandbits(1)),
        'form_of_study' : study_forms[randint(0, (len(study_forms)-1))]
    }
    result=db.students.insert_one(row)

def is_available(id):
	try:
		occupied = db.rooms.find_one({'_id': id})['number_of_occupied_places']
		room_type = db.rooms.find_one({'_id': id})['room_type_id']
		free_places = db.room_types.find_one({'_id': int(room_type)})['number_of_places']
		if free_places > occupied:
			return True
		else:
			return False
	except:
		return False


def get_room():
	room = 0
	is_avail = False
	while is_avail==False:
		room = randint(1,NUM_OF_ROOMS)
		is_avail = is_available(room)

	try:
 		occupied_new = db.rooms.find_one({'_id': room})['number_of_occupied_places'] + 1
 		db.rooms.update_one({'_id': room}, { "$set": { 'number_of_occupied_places': occupied_new }})
 		return room
	except:
 		print('ox')


#Create student_dorm_info
for x in range(1, NUM_OF_STUDENTS-1):
    row = {
    	'_id' : x,
    	'student_id' : NUM_OF_STUDENTS-x,
        'room_id' : get_room(),
        'date_of_entry' : radar.random_datetime(start='2016-05-24T23:59:59', stop='2017-05-24T23:59:59'),
        'date_of_contract_expire' : radar.random_datetime(start='2018-05-24T23:59:59', stop='2024-05-24T23:59:59'),
        'number_of_warnings' : randint(1,3),
       'paid_till' : radar.random_datetime(start='2021-05-24T23:59:59', stop='2022-05-24T23:59:59'),
        'last_check_in' : radar.random_datetime(start='2021-03-24T23:59:59', stop='2021-05-24T23:59:59'),
        'last_check_out' : radar.random_datetime(start='2021-03-24T23:59:59', stop='2021-05-24T23:59:59'),
    }
    result=db.student_dorm_info.insert_one(row)


