package ru.itmo.se.hibd.petoe.warehouse.loader.configuration;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.jdbi.v3.core.Jdbi;

public class DatabaseConfiguration {

    @Produces
    @Named
    private static Jdbi sourceJdbi() {
        return Jdbi.create("jdbc:oracle:thin:@localhost:1521:XE", "hibd", "hibd");
    }

    @Produces
    @Named
    private static Jdbi warehouseJdbi() {
        return Jdbi.create("jdbc:oracle:thin:@localhost:1523:XE", "orac3rd", "orac3rd");
    }
}
