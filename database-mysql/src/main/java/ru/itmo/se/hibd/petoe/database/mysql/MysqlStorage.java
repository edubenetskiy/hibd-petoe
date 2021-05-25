package ru.itmo.se.hibd.petoe.database.mysql;

import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.List;

import static ru.itmo.se.hibd.petoe.recordkeyextractor.MapRecordKeyExtractor.allColumns;
import static ru.itmo.se.hibd.petoe.recordkeyextractor.MapRecordKeyExtractor.longColumns;

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
                        .idExtractor(longColumns("person_id", "publication_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("conferences")
                        .targetTableName("conference")
                        .idExtractor(longColumns("id"))
                        .build(),
                mysqlTable()
                        .internalTableName("conferences_participants")
                        .targetTableName("conference_participant")
                        .idExtractor(longColumns("person_id", "conference_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("library_card")
                        .targetTableName("library_card")
                        .idExtractor(allColumns())
                        .build(),
                mysqlTable()
                        .internalTableName("persons")
                        .targetTableName("person")
                        .idExtractor(longColumns("person_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("project_participants")
                        .targetTableName("project_participant")
                        .idExtractor(longColumns("person_id", "project_id"))
                        .build(),
                mysqlTable()
                        .internalTableName("projects")
                        .targetTableName("project")
                        .idExtractor(longColumns("id"))
                        .build(),
                mysqlTable()
                        .internalTableName("publications")
                        .targetTableName("publication")
                        .idExtractor(longColumns("id"))
                        .build()
        );
    }

    private MysqlTable.MysqlTableBuilder mysqlTable() {
        return MysqlTable.builder()
                .storage(MysqlStorage.this);
    }
}
