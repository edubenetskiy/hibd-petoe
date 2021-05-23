package ru.itmo.se.hibd.petoe.database.postgresql;

import lombok.NonNull;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.StorageType;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collections;
import java.util.Map;

public class PostgresqlRowRecord implements Record {

    private final @NonNull PostgresqlTable postgresqlTable;
    private final @NonNull Object key;
    private final @NonNull Map<String, Object> columnValues;

    public PostgresqlRowRecord(PostgresqlTable postgresqlTable, Object key, Map<String, Object> columnValues) {
        this.postgresqlTable = postgresqlTable;
        this.key = key;
        this.columnValues = columnValues;
    }

    @Override
    public Object getId() {
        return key;
    }

    @Override
    public Table getTable() {
        return postgresqlTable;
    }

    @Override
    public Map<String, Object> getColumnValues() {
        return Collections.unmodifiableMap(columnValues);
    }

    @Override public StorageType getStorageType() {
        return StorageType.POSTGRESQL;
    }

    @Override
    public String toString() {
        return String.format("%s{postgresqlTable=%s, key=%s, columnValues=%s}",
                getClass().getSimpleName(), postgresqlTable, key, columnValues);
    }

}
