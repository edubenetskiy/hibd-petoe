package ru.itmo.se.hibd.petoe.recordkeyextractor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SequenceKeyExtractor<K> implements MapRecordKeyExtractor<List<K>> {

    private final List<MapRecordKeyExtractor<K>> extractors;

    public SequenceKeyExtractor(List<MapRecordKeyExtractor<K>> extractors) {
        this.extractors = extractors;
    }

    @Override
    public List<K> extractKey(Map<String, Object> columnValues) {
        return extractors.stream()
                .map(extractor -> extractor.extractKey(columnValues))
                .collect(Collectors.toList());
    }
}
