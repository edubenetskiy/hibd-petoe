package ru.itmo.se.hibd.lab1.importer.core;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;

import java.util.Map;

@Builder
@ToString
@With
public class SimpleRecord implements Record {

    @NonNull Table table;
    @NonNull String tableName;
    @NonNull Object id;
    @NonNull Map<String, Object> columnValues;
    @NonNull StorageType storageType;

    public static SimpleRecord copyOf(Record other) {
        return SimpleRecord.builder()
                .id(other.getId())
                .table(other.getTable())
                .tableName(other.getTableName())
                .columnValues(other.getColumnValues())
                .storageType(other.getStorageType())
                .build();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Map<String, Object> getColumnValues() {
        return columnValues;
    }

    @Override
    public StorageType getStorageType() {
        return storageType;
    }
}
