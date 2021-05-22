package ru.itmo.se.hibd.lab1.importer.app.schema;

import lombok.ToString;

@ToString
class SimpleColumnMetadata implements ColumnMetadata {

    private final String columnName;

    private SimpleColumnMetadata(String columnName) {
        this.columnName = columnName;
    }

    public static SimpleColumnMetadata fromColumnName(String columnName) {
        return new SimpleColumnMetadata(columnName);
    }

    @Override
    public String name() {
        return columnName;
    }
}
