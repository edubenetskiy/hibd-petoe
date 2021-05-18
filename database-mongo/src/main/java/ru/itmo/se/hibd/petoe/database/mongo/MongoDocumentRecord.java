package ru.itmo.se.hibd.petoe.database.mongo;

import org.bson.Document;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.util.Collections;
import java.util.Map;

public class MongoDocumentRecord implements Record {

    private final MongoCollectionTable table;
    private final Object id;
    private final Document document;

    public MongoDocumentRecord(MongoCollectionTable table, Object id, Document document) {
        this.table = table;
        this.id = id;
        this.document = document;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public String toString() {
        return "MongoDocumentRecord{" +
               "table=" + table +
               ", id=" + id +
               ", document=" + document +
               '}';
    }

    @Override
    public Map<String, Object> getColumnValues() {
        return Collections.unmodifiableMap(document);
    }
}
