package ru.itmo.se.hibd.lab1.importer.core;

import java.util.Map;

public interface Record {

    Table getTable();

    default String getTableName() {
        return getTable().getName();
    }

    Object getId();

    Map<String, Object> getColumnValues();
}
