package ru.itmo.se.hibd.petoe.database.postgresql;

import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.List;

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
                        .idExtractor(MapRecordKeyExtractor.withColumn("speciality_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("students")
                        .targetTableName("student")
                        .idExtractor(MapRecordKeyExtractor.withColumn("student_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("lecturers")
                        .targetTableName("person")
                        .idExtractor(MapRecordKeyExtractor.withColumn("lecturer_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("subjects")
                        .targetTableName("subject")
                        .idExtractor(MapRecordKeyExtractor.withColumn("subject_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("lecturers_to_subjects")
                        .targetTableName("teacher_subject")
                        .idExtractor(MapRecordKeyExtractor.withColumns("lecturer_id", "subject_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("specialities_to_subjects")
                        .targetTableName("speciality_subject")
                        .idExtractor(MapRecordKeyExtractor.withColumns("speciality_id", "subject_id"))
                        .build(),
                getPostgresqlTable()
                        .internalTableName("record_book")
                        .targetTableName("record_book")
                        .idExtractor(MapRecordKeyExtractor.withColumns("student_id", "subject_id", "lecturer_id"))
                        .build()
        );
    }

    private PostgresqlTable.PostgresqlTableBuilder getPostgresqlTable() {
        return PostgresqlTable.builder()
                .storage(PostgresqlStorage.this);
    }

}
