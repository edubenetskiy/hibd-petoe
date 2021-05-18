package ru.itmo.se.hibd.lab1.importer.core;

import java.util.Collection;

public interface WritableStorage {

    Collection<ClusterizableTable> getTables();

    void writeRecord(Record record);
}
