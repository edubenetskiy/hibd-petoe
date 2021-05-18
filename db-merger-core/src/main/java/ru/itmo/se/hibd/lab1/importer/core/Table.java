package ru.itmo.se.hibd.lab1.importer.core;

public interface Table {

    String getName();

    Iterable<Record> readAllRecords();
}
