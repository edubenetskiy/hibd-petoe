package ru.itmo.se.hibd.petoe.database.oracle;

import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import oracle.sql.TIMESTAMP;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Map;
import java.util.stream.Collectors;

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
                            Map<String, Object> fixedColumnValues = columnValues.entrySet().stream()
                                    .map(this::fixEntryValueTypes)
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                            Object id = idExtractor.extractKey(fixedColumnValues);
                            return new OracleRowRecord(OracleTable.this, id, fixedColumnValues);
                        })
                        .collect(toList()));
    }

    @SneakyThrows
    private Map.Entry<String, Object> fixEntryValueTypes(Map.Entry<String, Object> entry) {
        Object value = entry.getValue();
        if (value instanceof TIMESTAMP) {
            value = ((TIMESTAMP) value).timestampValue();
        }
        return Map.entry(entry.getKey(), value);
    }

}
