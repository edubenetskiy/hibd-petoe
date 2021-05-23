package ru.itmo.se.hibd.lab1.importer.core;

public interface TargetDatabase extends AutoCloseable {

    void writeRecord(Record mergedRecord);

}
