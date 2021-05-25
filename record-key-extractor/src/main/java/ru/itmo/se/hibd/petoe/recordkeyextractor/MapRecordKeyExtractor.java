package ru.itmo.se.hibd.petoe.recordkeyextractor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MapRecordKeyExtractor<K> {

    static MapRecordKeyExtractor<Object> column(String keyColumn) {
        return new ByColumnMapRecordKeyExtractor(Collections.singletonList(keyColumn));
    }

    static MapRecordKeyExtractor<List<Long>> longColumns(String... keyColumnNames) {
        return new SequenceKeyExtractor<>(
                Arrays.stream(keyColumnNames)
                        .map(MapRecordKeyExtractor::column)
                        .map(LongMapRecordKeyExtractor::new)
                        .collect(Collectors.toList()));
    }

    static MapRecordKeyExtractor<Object> allColumns() {
        return columnValues -> columnValues;
    }

    K extractKey(Map<String, Object> columnValues);
}
