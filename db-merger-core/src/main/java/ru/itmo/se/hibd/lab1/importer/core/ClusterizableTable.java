package ru.itmo.se.hibd.lab1.importer.core;

import java.util.Collection;
import java.util.Map;

public interface ClusterizableTable {

    String getName();

    Map<Object, Collection<Record>> groupRecordsById();
}
