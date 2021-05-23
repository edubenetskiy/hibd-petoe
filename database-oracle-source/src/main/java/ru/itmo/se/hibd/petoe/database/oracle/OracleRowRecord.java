package ru.itmo.se.hibd.petoe.database.oracle;

import lombok.NonNull;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.StorageType;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collections;
import java.util.Map;

public class OracleRowRecord implements Record {
    private final @NonNull OracleTable oracleTable;
    private final @NonNull Object key;
    private final @NonNull Map<String, Object> columnValues;

    public OracleRowRecord(OracleTable oracleTable, Object key, Map<String, Object> columnValues) {
        this.oracleTable = oracleTable;
        this.key = key;
        this.columnValues = columnValues;
    }

    @Override
    public Object getId() {
        return key;
    }

    @Override
    public Table getTable() {
        return oracleTable;
    }

    @Override
    public Map<String, Object> getColumnValues() {
        return Collections.unmodifiableMap(columnValues);
    }

    @Override public StorageType getStorageType() {
        return StorageType.ORACLE;
    }

    @Override
    public String toString() {
        return String.format("%s{oracleTable=%s, key=%s, columnValues=%s}",
                getClass().getSimpleName(), oracleTable, key, columnValues);
    }
}
