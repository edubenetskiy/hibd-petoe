package ru.itmo.se.hibd.lab1.importer.core;

import java.util.Collection;

public interface WritableStorage {

    Collection<ClusterizableTable> getTables();

    ClusterizableTable getTableByName(String name);

    boolean hasTable(String name);

    void writeRecord(Record record);
}
