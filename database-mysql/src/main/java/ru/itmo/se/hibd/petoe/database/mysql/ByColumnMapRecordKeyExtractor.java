package ru.itmo.se.hibd.petoe.database.mysql;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ByColumnMapRecordKeyExtractor implements MapRecordKeyExtractor {

    public static final ByColumnMapRecordKeyExtractor BY_ID = new ByColumnMapRecordKeyExtractor(List.of("id"));

    private final List<String> keyColumnNames;

    public ByColumnMapRecordKeyExtractor(List<String> keyColumnNames) {
        this.keyColumnNames = keyColumnNames;
    }

    @Override
    public Object extractKey(Map<String, Object> columnValues) {
        if (!CollectionUtils.isSubCollection(keyColumnNames, columnValues.keySet())) {
            throw new IllegalArgumentException(
                    "record " + columnValues + " is missing some key columns: " +
                    CollectionUtils.subtract(keyColumnNames, columnValues.keySet()));
        }
        return keyColumnNames.stream()
                .map(columnValues::get)
                .peek(value -> {
                    if (value == null) throw new AssertionError("key cannot be null");
                })
                .collect(Collectors.toList());
    }
}
