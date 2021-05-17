package ru.itmo.se.hibd.lab1.importer.core;

import java.util.Collection;

public interface WritableStorage {

    Collection<Table> getTables();

    void writeRecord(Record record);
}
