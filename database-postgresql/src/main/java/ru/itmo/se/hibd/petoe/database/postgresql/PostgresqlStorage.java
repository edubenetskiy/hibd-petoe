package ru.itmo.se.hibd.petoe.database.postgresql;

import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.List;

import static ru.itmo.se.hibd.petoe.recordkeyextractor.MapRecordKeyExtractor.longColumns;

public class PostgresqlStorage implements Storage {

    final Jdbi jdbi;

    public PostgresqlStorage(Jdbi postgresqlJdbiConnection) {
        this.jdbi = postgresqlJdbiConnection;
    }

    @Override
    public Collection<Table> getTables() {
        return List.of(
                getPostgresqlTable()
                        .internalTableName("speciality")
                        .targetTableName("speciality")
                        .idExtractor(longColumns("id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("students")
                        .targetTableName("student")
                        .idExtractor(longColumns("id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("lecturers")
                        .targetTableName("teacher")
                        .idExtractor(longColumns("id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("subjects")
                        .targetTableName("subject")
                        .idExtractor(longColumns("id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("lecturers_to_subjects")
                        .targetTableName("teacher_subject")
                        .idExtractor(longColumns("teacher_id", "subject_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("specialities_to_subjects")
                        .targetTableName("speciality_subject")
                        .idExtractor(longColumns("speciality_id", "subject_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("record_book")
                        .targetTableName("record_book")
                        .idExtractor(longColumns("student_id", "subject_id", "teacher_id"))
                        .build()
        );
    }

    private PostgresqlTable.PostgresqlTableBuilder getPostgresqlTable() {
        return PostgresqlTable.builder()
                .storage(PostgresqlStorage.this);
    }

}
