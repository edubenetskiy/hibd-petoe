db.createCollection("students", {capped: false});
db.createCollection("dorms", {capped: false});
db.createCollection("room_types", {capped: false});
db.createCollection("rooms", {capped: false});
db.createCollection("student_dorm_info", {capped: false});

db.students.insert([
    {
        _id: 111111,
        name: "Петров Петр Петрович",
        is_privileged: false,
        form_of_study: "FREE",
    },
    {
        _id: 222222,
        name: "Иванов Аркадий Петрович",
        is_privileged: false,
        form_of_study: "SELF-FUNDED",
    },
    {
        _id: 333333,
        name: "Арефьев Петр Васильевич",
        is_privileged: false,
        form_of_study: "FREE",
    },
    {
        _id: 444444,
        name: "Кириленко Василий Викторович",
        is_privileged: true,
        form_of_study: "SELF-FUNDED",
    },
    {
        _id: 555555,
        name: "Абрамов Петр Петрович",
        is_privileged: false,
        form_of_study: "FREE",
    },
    {
        _id: 666666,
        name: "Абрамович Иван Игоревич",
        is_privileged: false,
        form_of_study: "FREE",
    },
    {
        _id: 777777,
        name: "Иванов Петр Дмитриевич",
        is_privileged: false,
        form_of_study: "FREE",
    },
    {
        _id: 888888,
        name: "Германов Петр Дмитриевич",
        is_privileged: false,
        form_of_study: "FREE",
    },
    {
        _id: 999999,
        name: "Ананьев Петр Александрович",
        is_privileged: true,
        form_of_study: "FREE",
    },
    {
        _id: 100000,
        name: "Богданов Ильфат Петрович",
        is_privileged: false,
        form_of_study: "FREE",
    },
]);

db.dorms.insert([
    {
        _id: 1,
        address: "наб. р. Карповки 22 к2 лит Б",
        number_of_rooms: 40,
    },
    {
        _id: 2,
        address: "Вяземский пер. 5-7",
        number_of_rooms: 500,
    },
    {
        _id: 3,
        address: "Новоизмайловский пр-т. 16 к3",
        number_of_rooms: 250,
    },
    {
        _id: 4,
        address: "Альпийский пер. 15 к2",
        number_of_rooms: 240,
    },
]);

db.room_types.insert([
    {
        _id: 1,
        number_of_places: 3,
        price: 3032.98,
    },
    {
        _id: 2,
        number_of_places: 2,
        price: 3367.87,
    },
    {
        _id: 3,
        number_of_places: 4,
        price: 600,
    },
    {
        _id: 4,
        number_of_places: 3,
        price: 800.89,
    },
    {
        _id: 5,
        number_of_places: 4,
        price: 1090.23,
    },
    {
        _id: 6,
        number_of_places: 2,
        price: 1500.87,
    },
]);

db.rooms.insert([
    {
        _id: 101,
        dorm_id: 1,
        room_type_id: 1,
        number_of_occupied_places: 2,
        does_have_bugs: false,
        date_of_last_disinfection: {
            $date: "2018-04-15T16:54:40Z",
        },
    },
    {
        _id: 102,
        dorm_id: 1,
        room_type_id: 2,
        number_of_occupied_places: 2,
        does_have_bugs: false,
        date_of_last_disinfection: {
            $date: "2018-04-15T16:54:40Z",
        },
    },
    {
        _id: 103,
        dorm_id: 1,
        room_type_id: 3,
        number_of_occupied_places: 1,
        does_have_bugs: true,
        date_of_last_disinfection: {
            $date: "2021-04-15T16:54:40Z",
        },
    },
    {
        _id: 1103,
        dorm_id: 2,
        room_type_id: 4,
        number_of_occupied_places: 2,
        does_have_bugs: false,
        date_of_last_disinfection: {
            $date: "2021-04-15T16:54:40Z",
        },
    },
    {
        _id: 1104,
        dorm_id: 2,
        room_type_id: 5,
        number_of_occupied_places: 1,
        does_have_bugs: false,
        date_of_last_disinfection: {
            $date: "2018-04-15T16:54:40Z",
        },
    },
    {
        _id: 143,
        dorm_id: 3,
        room_type_id: 6,
        number_of_occupied_places: 2,
        does_have_bugs: true,
        date_of_last_disinfection: {
            $date: "2020-04-15T16:54:40Z",
        },
    },
    {
        _id: 142,
        dorm_id: 3,
        room_type_id: 1,
        number_of_occupied_places: 1,
        does_have_bugs: false,
        date_of_last_disinfection: {
            $date: "2018-04-15T16:54:40Z",
        },
    },
    {
        _id: 1990,
        dorm_id: 4,
        room_type_id: 2,
        number_of_occupied_places: 2,
        does_have_bugs: true,
        date_of_last_disinfection: {
            $date: "2021-04-15T16:54:40Z",
        },
    },
    {
        _id: 1991,
        dorm_id: 4,
        room_type_id: 3,
        number_of_occupied_places: 2,
        does_have_bugs: false,
        date_of_last_disinfection: {
            $date: "2021-04-15T16:54:40Z",
        },
    },
    {
        _id: 405,
        dorm_id: 4,
        room_type_id: 4,
        number_of_occupied_places: 2,
        does_have_bugs: false,
        date_of_last_disinfection: {
            $date: "2020-04-15T16:54:40Z",
        },
    },
]);

db.student_dorm_info.insert([
    {
        student_id: 111111,
        room_id: 101,
        date_of_entry: {
            $date: "2016-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2022-05-31T16:54:40Z",
        },
        number_of_warnings: 0,
        paid_till: {
            $date: "2021-04-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-15T16:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-15T18:54:40Z",
        },
    },
    {
        student_id: 222222,
        room_id: 102,
        date_of_entry: {
            $date: "2016-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2022-05-31T16:54:40Z",
        },
        number_of_warnings: 1,
        paid_till: {
            $date: "2021-03-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-16T20:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T18:54:40Z",
        },
    },
    {
        student_id: 333333,
        room_id: 103,
        date_of_entry: {
            $date: "2017-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2023-05-31T16:54:40Z",
        },
        number_of_warnings: 2,
        paid_till: {
            $date: "2021-03-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-16T20:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T18:54:40Z",
        },
    },
    {
        student_id: 444444,
        room_id: 1103,
        date_of_entry: {
            $date: "2020-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2021-05-31T16:54:40Z",
        },
        number_of_warnings: 0,
        paid_till: {
            $date: "2021-05-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-14T21:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T13:34:40Z",
        },
    },
    {
        student_id: 555555,
        room_id: 1104,
        date_of_entry: {
            $date: "2019-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2021-05-31T16:54:40Z",
        },
        number_of_warnings: 0,
        paid_till: {
            $date: "2021-05-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-14T21:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-13T13:34:40Z",
        },
    },
    {
        student_id: 666666,
        room_id: 143,
        date_of_entry: {
            $date: "2020-10-11T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2024-05-31T16:54:40Z",
        },
        number_of_warnings: 1,
        paid_till: {
            $date: "2021-05-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-14T21:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T13:34:40Z",
        },
    },
    {
        student_id: 777777,
        room_id: 142,
        date_of_entry: {
            $date: "2020-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2021-05-31T16:54:40Z",
        },
        number_of_warnings: 0,
        paid_till: {
            $date: "2021-05-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-14T21:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T13:34:40Z",
        },
    },
    {
        student_id: 888888,
        room_id: 1990,
        date_of_entry: {
            $date: "2020-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2021-05-31T16:54:40Z",
        },
        number_of_warnings: 0,
        paid_till: {
            $date: "2021-05-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-14T21:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T13:34:40Z",
        },
    },
    {
        student_id: 999999,
        room_id: 1991,
        date_of_entry: {
            $date: "2018-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2023-05-31T16:54:40Z",
        },
        number_of_warnings: 0,
        paid_till: {
            $date: "2021-05-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-14T21:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T13:34:40Z",
        },
    },
    {
        student_id: 100000,
        room_id: 405,
        date_of_entry: {
            $date: "2020-09-01T16:54:40Z",
        },
        date_of_contract_expire: {
            $date: "2021-05-31T16:54:40Z",
        },
        number_of_warnings: 0,
        paid_till: {
            $date: "2021-05-01T16:54:40Z",
        },
        last_check_in: {
            $date: "2021-04-14T21:54:40Z",
        },
        last_check_out: {
            $date: "2021-04-17T13:34:40Z",
        },
    },
]);
