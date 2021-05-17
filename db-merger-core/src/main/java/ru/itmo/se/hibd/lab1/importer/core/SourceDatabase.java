package ru.itmo.se.hibd.lab1.importer.core;

import java.util.Collection;

public interface SourceDatabase {

    Collection<Table> getTables();

}
