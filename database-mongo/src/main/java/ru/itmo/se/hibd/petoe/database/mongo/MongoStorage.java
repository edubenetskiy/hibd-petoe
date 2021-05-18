package ru.itmo.se.hibd.petoe.database.mongo;

import com.mongodb.client.MongoDatabase;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MongoStorage implements Storage {

    private final MongoDatabase connection;

    public MongoStorage(MongoDatabase connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Table> getTables() {
        // table names from Mongo db-init script
        return Stream.of(
                mongoCollection("students").targetTableName("student"),
                mongoCollection("dorms").targetTableName("dorm"),
                mongoCollection("room_types").targetTableName("room_type"),
                mongoCollection("rooms").targetTableName("room"),
                mongoCollection("student_dorm_info").targetTableName("student_dorm_info")
        )
                .map(MongoCollectionTable.MongoCollectionTableBuilder::build)
                .collect(Collectors.toList());
    }

    private MongoCollectionTable.MongoCollectionTableBuilder mongoCollection(String collectionName) {
        return MongoCollectionTable.builder()
                .database(this)
                .internalCollectionName(collectionName)
                .collection(connection.getCollection(collectionName));
    }
}
