package ru.itmo.se.hibd.lab1.importer.app.schema;

import java.util.Collection;

public interface SchemaMetadata {

    Collection<TableMetadata> allTables();

    TableMetadata findTableByName(String tableName);
}
