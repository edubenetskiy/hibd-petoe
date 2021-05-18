package ru.itmo.se.hibd.petoe.inmemorystorage;

import ru.itmo.se.hibd.lab1.importer.core.ClusterizableTable;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.WritableStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryWritableStorage implements WritableStorage {

    private final Map<String, InMemoryTable> tableByName = new HashMap<>();

    @Override
    public Collection<ClusterizableTable> getTables() {
        return Collections.unmodifiableCollection(tableByName.values());
    }

    @Override
    public void writeRecord(Record record) {
        String tableName = record.getTableName();
        InMemoryTable inMemoryTable = tableByName.computeIfAbsent(tableName, InMemoryTable::new);
        inMemoryTable.save(record);
    }
}
