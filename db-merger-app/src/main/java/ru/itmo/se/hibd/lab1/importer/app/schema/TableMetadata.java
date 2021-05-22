package ru.itmo.se.hibd.lab1.importer.app.schema;

import java.util.Collection;

public interface TableMetadata {

    String name();

    Collection<ColumnMetadata> columns();
}
