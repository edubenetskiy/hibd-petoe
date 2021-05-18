package ru.itmo.se.hibd.petoe.database.mysql;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public interface MapRecordKeyExtractor {

    public static MapRecordKeyExtractor withColumn(String keyColumn) {
        return new ByColumnMapRecordKeyExtractor(Collections.singletonList(keyColumn));
    }

    public static MapRecordKeyExtractor withColumns(String... keyColumnNames) {
        return new ByColumnMapRecordKeyExtractor(Arrays.asList(keyColumnNames));
    }

    public static MapRecordKeyExtractor withAllColumns() {
        return columnValues -> columnValues;
    }

    Object extractKey(Map<String, Object> columnValues);
}
