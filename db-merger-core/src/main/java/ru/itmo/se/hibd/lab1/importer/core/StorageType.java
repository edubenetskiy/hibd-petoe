package ru.itmo.se.hibd.lab1.importer.core;

public enum StorageType {
    UNKNOWN,
    MONGODB,
    POSTGRESQL,
    ORACLE,
    MYSQL,
    ORACLE_FINAL;

    public int rank() {
        return this.ordinal();
    }
}
