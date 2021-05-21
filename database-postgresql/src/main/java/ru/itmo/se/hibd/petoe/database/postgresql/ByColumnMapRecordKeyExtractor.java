package ru.itmo.se.hibd.petoe.database.postgresql;

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
        return keyColumnNames.stream().map(columnValues::get).collect(Collectors.toList());
    }

}
