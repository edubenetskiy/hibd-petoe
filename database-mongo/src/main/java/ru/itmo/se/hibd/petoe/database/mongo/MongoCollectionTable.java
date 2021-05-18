package ru.itmo.se.hibd.petoe.database.mongo;

import com.mongodb.client.MongoCollection;
import lombok.Builder;
import lombok.NonNull;
import org.bson.Document;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.Table;

@Builder
public class MongoCollectionTable implements Table {

    private final @NonNull MongoStorage database;
    private final @NonNull MongoCollection<Document> collection;
    private final @NonNull String internalCollectionName;
    private final @NonNull String targetTableName;

    @Override
    public String getName() {
        return targetTableName;
    }

    @Override
    public Iterable<Record> readAllRecords() {
        return collection.find().map(document -> {
            MongoCollectionTable table = MongoCollectionTable.this;
            Object id = document.get("_id");
            return new MongoDocumentRecord(table, id, document);
        });
    }

    @Override
    public String toString() {
        return "MongoCollectionTable{" +
               "internalCollectionName='" + internalCollectionName + '\'' +
               ", targetTableName='" + targetTableName + '\'' +
               '}';
    }
}
