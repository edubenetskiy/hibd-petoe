package ru.itmo.se.hibd.petoe.database.mysql;

import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.List;

public class MysqlStorage implements Storage {

    final Jdbi jdbi;

    public MysqlStorage(Jdbi mysqlJdbiConnection) {
        this.jdbi = mysqlJdbiConnection;
    }

    @Override
    public Collection<Table> getTables() {
        return List.of(
                mysqlTable()
                        .internalTableName("coauthors")
                        .targetTableName("coauthors")
                        .idExtractor(MapRecordKeyExtractor.withColumns("person_id", "publication_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("conferences")
                        .targetTableName("conference")
                        .idExtractor(MapRecordKeyExtractor.withColumn("id"))
                        .build(),
                mysqlTable()
                        .internalTableName("conferences_participants")
                        .targetTableName("conference_participant")
                        .idExtractor(MapRecordKeyExtractor.withColumns("person_id", "conference_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("library_card")
                        .targetTableName("library_card")
                        .idExtractor(MapRecordKeyExtractor.withAllColumns())
                        .build(),
                mysqlTable()
                        .internalTableName("persons")
                        .targetTableName("person")
                        .idExtractor(MapRecordKeyExtractor.withColumn("person_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("project_participants")
                        .targetTableName("project_participant")
                        .idExtractor(MapRecordKeyExtractor.withColumns("person_id", "project_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("projects")
                        .targetTableName("project")
                        .idExtractor(MapRecordKeyExtractor.withColumn("id"))
                        .build(),
                mysqlTable()
                        .internalTableName("publications")
                        .targetTableName("publication")
                        .idExtractor(MapRecordKeyExtractor.withColumn("id"))
                        .build()
        );
    }

    private MysqlTable.MysqlTableBuilder mysqlTable() {
        return MysqlTable.builder()
                .storage(MysqlStorage.this);
    }
}
