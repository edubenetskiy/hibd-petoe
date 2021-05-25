package ru.itmo.se.hibd.petoe.database.oracle;

import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.List;

import static ru.itmo.se.hibd.petoe.recordkeyextractor.MapRecordKeyExtractor.longColumns;

public class OracleStorage implements Storage {

    final Jdbi jdbi;

    public OracleStorage(Jdbi oracleJdbiConnection) {
        this.jdbi = oracleJdbiConnection;
    }

    @Override
    public Collection<Table> getTables() {
        return List.of(
                getOracleTable()
                        .internalTableName("megafaculty")
                        .targetTableName("megafaculty")
                        .idExtractor(longColumns("id"))
                        .build(),
                getOracleTable()
                        .internalTableName("faculty")
                        .targetTableName("faculty")
                        .idExtractor(longColumns("id", "megafaculty_id"))
                        .build(),
                getOracleTable()
                        .internalTableName("person")
                        .targetTableName("person")
                        .idExtractor(longColumns("id", "faculty_id"))
                        .build(),
                getOracleTable()
                        .internalTableName("person_job")
                        .targetTableName("person_job")
                        .idExtractor(longColumns("id"))
                        .build(),
                getOracleTable()
                        .internalTableName("teacher")
                        .targetTableName("teacher")
                        .idExtractor(longColumns("id", "person_id", "person_job_id"))
                        .build(),
                getOracleTable()
                        .internalTableName("study_group")
                        .targetTableName("study_group")
                        .idExtractor(longColumns("id"))
                        .build(),
                getOracleTable()
                        .internalTableName("speciality")
                        .targetTableName("speciality")
                        .idExtractor(longColumns("id"))
                        .build(),
                getOracleTable()
                        .internalTableName("direction")
                        .targetTableName("direction")
                        .idExtractor(longColumns("id"))
                        .build(),
                getOracleTable()
                        .internalTableName("student")
                        .targetTableName("student")
                        .idExtractor(longColumns("id", "study_group_id",
                                "direction_id", "speciality_id", "person_id"))
                        .build(),
                getOracleTable()
                        .internalTableName("subject")
                        .targetTableName("subject")
                        .idExtractor(longColumns("id"))
                        .build(),
                getOracleTable()
                        .internalTableName("timetable")
                        .targetTableName("timetable")
                        .idExtractor(longColumns("id", "teacher_id",
                                "subject_id", "study_group_id"))
                        .build(),
                getOracleTable()
                        .internalTableName("record_book")
                        .targetTableName("record_book")
                        .idExtractor(longColumns("student_id", "subject_id"))
                        .build()
        );
    }

    private OracleTable.OracleTableBuilder getOracleTable() {
        return OracleTable.builder()
                .storage(OracleStorage.this);
    }
}
