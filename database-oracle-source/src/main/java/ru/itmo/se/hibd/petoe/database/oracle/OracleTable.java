package ru.itmo.se.hibd.petoe.database.oracle;

import lombok.Builder;
import lombok.NonNull;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import static java.util.stream.Collectors.toList;

@Builder
public class OracleTable implements Table {

    private final @NonNull OracleStorage storage;
    private final @NonNull String internalTableName;
    private final @NonNull MapRecordKeyExtractor idExtractor;
    private final @NonNull String targetTableName;

    public OracleTable(OracleStorage storage, String internalTableName, MapRecordKeyExtractor idExtractor, String targetTableName) {
        this.storage = storage;
        this.internalTableName = internalTableName;
        this.idExtractor = idExtractor;
        this.targetTableName = targetTableName;
    }

    @Override
    public String getName() {
        return targetTableName;
    }

    @Override
    public Iterable<Record> readAllRecords() {
        return storage.jdbi.withHandle(handle ->
                handle.select("select * from " + internalTableName)
                        .mapToMap()
                        .map(columnValues -> {
                            Object id = idExtractor.extractKey(columnValues); // TODO: extract ID by columns
                            return new OracleRowRecord(OracleTable.this, id, columnValues);
                        })
                        .collect(toList()));
    }

}