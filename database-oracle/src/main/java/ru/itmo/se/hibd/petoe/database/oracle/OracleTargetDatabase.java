package ru.itmo.se.hibd.petoe.database.oracle;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.TargetDatabase;

import java.util.Map;
import java.util.stream.Collectors;

public class OracleTargetDatabase implements TargetDatabase, AutoCloseable {

    private final Jdbi jdbi;
    private final Handle handle;

    public OracleTargetDatabase(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.handle = jdbi.open();
        handle.begin();
    }

    @Override
    public void writeRecord(Record mergedRecord) {
        String columnNames = mergedRecord.getColumnValues().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
        String placeholders = mergedRecord.getColumnValues().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .map(key -> ":" + key)
                .collect(Collectors.joining(", "));
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                mergedRecord.getTableName(), columnNames, placeholders);
        handle.createUpdate(sql)
                .bindMap(mergedRecord.getColumnValues())
                .execute();
    }

    @Override
    public void close() throws Exception {
        handle.commit(); // FIXME: better commit manually, large transactions may timeout
        handle.close();
    }
}
