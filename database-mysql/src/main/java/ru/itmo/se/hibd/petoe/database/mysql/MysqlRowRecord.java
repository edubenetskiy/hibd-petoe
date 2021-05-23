package ru.itmo.se.hibd.petoe.database.mysql;

import lombok.NonNull;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.StorageType;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collections;
import java.util.Map;

public class MysqlRowRecord implements Record {

    private final @NonNull MysqlTable mysqlTable;
    private final @NonNull Object key;
    private final @NonNull Map<String, Object> columnValues;

    public MysqlRowRecord(MysqlTable mysqlTable, Object key, Map<String, Object> columnValues) {
        this.mysqlTable = mysqlTable;
        this.key = key;
        this.columnValues = columnValues;
    }

    @Override
    public Object getId() {
        return key;
    }

    @Override
    public Table getTable() {
        return mysqlTable;
    }

    @Override
    public Map<String, Object> getColumnValues() {
        return Collections.unmodifiableMap(columnValues);
    }

    @Override public StorageType getStorageType() {
        return StorageType.MYSQL;
    }

    @Override
    public String toString() {
        return String.format("%s{mysqlTable=%s, key=%s, columnValues=%s}",
                getClass().getSimpleName(), mysqlTable, key, columnValues);
    }
}
