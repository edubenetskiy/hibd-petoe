package ru.itmo.se.hibd.petoe.recordkeyextractor;

import java.util.List;
import java.util.Map;

public class LongMapRecordKeyExtractor implements MapRecordKeyExtractor<Long> {

    private final MapRecordKeyExtractor<?> inner;

    public <K> LongMapRecordKeyExtractor(MapRecordKeyExtractor<?> inner) {
        this.inner = inner;
    }

    @Override
    public Long extractKey(Map<String, Object> columnValues) {
        Object extractedKey = inner.extractKey(columnValues);
        Long extractedKeyAsLong = convertToLong(extractedKey);
        if (extractedKeyAsLong != null) return extractedKeyAsLong;
        throw new IllegalArgumentException();
    }

    private Long convertToLong(Object extractedKey) {
        if (extractedKey instanceof List) {
            List<?> extractedKeyList = (List<?>) extractedKey;
            if (extractedKeyList.size() == 1) {
                return convertToLong(extractedKeyList.get(0));
            }
        }
        if (extractedKey instanceof Number) {
            return ((Number) extractedKey).longValue();
        }
        if (extractedKey instanceof String) {
            return Long.valueOf((String) extractedKey);
        }
        return null;
    }
}
