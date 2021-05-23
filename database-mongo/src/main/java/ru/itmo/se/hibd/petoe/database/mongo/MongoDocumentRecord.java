package ru.itmo.se.hibd.petoe.database.mongo;

import org.bson.Document;
import org.bson.types.ObjectId;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.StorageType;
import ru.itmo.se.hibd.lab1.importer.core.Table;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class MongoDocumentRecord implements Record {

    private final MongoCollectionTable table;
    private final Object id;
    private final Document document;
    private final Map<String, Object> columnValues;

    public MongoDocumentRecord(MongoCollectionTable table, Object id, Document document) {
        this.table = table;
        this.id = id;
        this.document = document;
        this.columnValues = document.entrySet().stream()
                .map(this::fixEntryValueTypes)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
        return columnValues;
    }

    @Override public StorageType getStorageType() {
        return StorageType.MONGODB;
    }

    private Map.Entry<String, Object> fixEntryValueTypes(Map.Entry<String, Object> entry) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (key.equals("_id")) {
            key = "id";
        }
        if (value instanceof ObjectId) {
            value = value.hashCode();
        } else if (value instanceof Document && ((Document) value).containsKey("$date")) {
            value = OffsetDateTime.parse(((Document) value).getString("$date"));
        }
        return Map.entry(key, value);
    }
}
