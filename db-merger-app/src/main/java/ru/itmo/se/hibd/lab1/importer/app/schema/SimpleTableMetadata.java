package ru.itmo.se.hibd.lab1.importer.app.schema;

import lombok.Builder;
import lombok.ToString;

import java.util.Collection;

@Builder
@ToString(onlyExplicitlyIncluded = true)
public class SimpleTableMetadata implements TableMetadata {

    @ToString.Include
    private final String tableName;

    @ToString.Include
    private final Collection<ColumnMetadata> columns;

    @Override
    public String name() {
        return tableName;
    }

    @Override
    public Collection<ColumnMetadata> columns() {
        return columns;
    }
}
