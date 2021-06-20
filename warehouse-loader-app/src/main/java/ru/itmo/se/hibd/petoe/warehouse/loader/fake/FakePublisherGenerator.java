package ru.itmo.se.hibd.petoe.warehouse.loader.fake;

import jakarta.inject.Named;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Named
public class FakePublisherGenerator {

    List<String> publisherTitles;

    public FakePublisherGenerator() {
        publisherTitles = readPublisherTitles();
    }

    public String nextPublisher() {
        return publisherTitles.get(ThreadLocalRandom.current().nextInt(publisherTitles.size()));
    }

    @SneakyThrows
    private List<String> readPublisherTitles() {
        return new String(
                FakePublisherGenerator.class.getResource("/fake-data/publishers.txt")
                        .openStream()
                        .readAllBytes(),
                StandardCharsets.UTF_8)
                .lines()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }
}
