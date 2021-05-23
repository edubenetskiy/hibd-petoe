package ru.itmo.se.hibd.petoe.recordkeyextractor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public interface MapRecordKeyExtractor {

    static MapRecordKeyExtractor withColumn(String keyColumn) {
        return new ByColumnMapRecordKeyExtractor(Collections.singletonList(keyColumn));
    }

    static MapRecordKeyExtractor withColumns(String... keyColumnNames) {
        return new ByColumnMapRecordKeyExtractor(Arrays.asList(keyColumnNames));
    }

    static MapRecordKeyExtractor withAllColumns() {
        return columnValues -> columnValues;
    }

    Object extractKey(Map<String, Object> columnValues);

}
