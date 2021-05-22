package ru.itmo.se.hibd.lab1.importer.app.schema;

import lombok.SneakyThrows;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVFileSchemaMetadata implements SchemaMetadata {

    private final MultiValuedMap<String, String> tableColumns;
    private final Map<String, TableMetadata> allTablesByName;

    @SneakyThrows
    public CSVFileSchemaMetadata(URL url) {
        MultiValuedMap<String, String> tableColumnsFromFile = new HashSetValuedHashMap<>();
        CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        for (CSVRecord csvRecord : csvParser) {
            String tableName = csvRecord.get("TABLE_NAME").toLowerCase(Locale.ROOT);
            String columnName = csvRecord.get("COLUMN_NAME").toLowerCase(Locale.ROOT);
            tableColumnsFromFile.put(tableName, columnName);
        }
        this.tableColumns = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(tableColumnsFromFile);
        this.allTablesByName = tableColumns.keySet().stream()
                .map(this::createTableMetadata)
                .collect(Collectors.toMap(TableMetadata::name, Function.identity()));
    }

    @Override
    public Collection<TableMetadata> allTables() {
        return allTablesByName.values();
    }

    @Override
    public TableMetadata findTableByName(String tableName) {
        if (!allTablesByName.containsKey(tableName)) {
            throw new RuntimeException("table with name " + tableName + " not found in schema");
        }
        return allTablesByName.get(tableName);
    }

    private SimpleTableMetadata createTableMetadata(String tableName) {
        Collection<ColumnMetadata> columns = tableColumns.get(tableName).stream()
                .map(SimpleColumnMetadata::fromColumnName)
                .collect(Collectors.toList());
        return SimpleTableMetadata.builder()
                .tableName(tableName)
                .columns(columns)
                .build();
    }
}
