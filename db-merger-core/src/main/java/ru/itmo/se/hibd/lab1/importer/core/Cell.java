package ru.itmo.se.hibd.lab1.importer.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@With
public class Cell {
    StorageType storageType;
    String columnName;
    Object value;
}
