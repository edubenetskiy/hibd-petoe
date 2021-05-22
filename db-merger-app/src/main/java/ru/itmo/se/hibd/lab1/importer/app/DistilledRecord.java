package ru.itmo.se.hibd.lab1.importer.app;

import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class DistilledRecord implements Record {

    private final Record record;
    private final HashMap<String, Object> columnValues;

    public DistilledRecord(Record record, Collection<String> retainedColumnNames) {
        this.record = record;
        this.columnValues = new HashMap<>(record.getColumnValues());
        columnValues.keySet().retainAll(retainedColumnNames);
    }

    @Override
    public Table getTable() {
        return record.getTable();
    }

    @Override
    public Object getId() {
        return record.getId();
    }

    @Override
    public Map<String, Object> getColumnValues() {
        return columnValues;
    }

    @Override
    public String toString() {
        return "DistilledRecord{" +
               "record=" + record +
               ", columnValues=" + columnValues +
               '}';
    }
}
