package ru.itmo.se.hibd.petoe.inmemorystorage;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.Map;

public class InMemoryTable implements Table {

    String name;

    MultiValuedMap<Object, Record> recordsById = new ArrayListValuedHashMap<>();

    public InMemoryTable(String tableName) {
        this.name = tableName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterable<Record> readAllRecords() {
        return recordsById.values();
    }

    @Override
    public Map<Object, Collection<Record>> groupRecordsById() {
        return recordsById.asMap();
    }

    public void save(Record record) {
        recordsById.put(record.getId(), record);
    }
}
