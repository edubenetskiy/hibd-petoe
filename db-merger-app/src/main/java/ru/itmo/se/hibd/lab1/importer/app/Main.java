package ru.itmo.se.hibd.lab1.importer.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.app.schema.CSVFileSchemaMetadata;
import ru.itmo.se.hibd.lab1.importer.app.schema.ColumnMetadata;
import ru.itmo.se.hibd.lab1.importer.app.schema.SchemaMetadata;
import ru.itmo.se.hibd.lab1.importer.app.schema.TableMetadata;
import ru.itmo.se.hibd.lab1.importer.core.Cell;
import ru.itmo.se.hibd.lab1.importer.core.ClusterizableTable;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.SimpleRecord;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.StorageType;
import ru.itmo.se.hibd.lab1.importer.core.Table;
import ru.itmo.se.hibd.lab1.importer.core.TargetDatabase;
import ru.itmo.se.hibd.lab1.importer.core.WritableStorage;
import ru.itmo.se.hibd.petoe.database.mongo.MongoStorage;
import ru.itmo.se.hibd.petoe.database.mysql.MysqlStorage;
import ru.itmo.se.hibd.petoe.database.oracle.OracleStorage;
import ru.itmo.se.hibd.petoe.database.oracle.OracleTargetDatabase;
import ru.itmo.se.hibd.petoe.database.postgresql.PostgresqlStorage;
import ru.itmo.se.hibd.petoe.inmemorystorage.InMemoryWritableStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class Main {

    public static final SchemaMetadata SCHEMA_METADATA =
            new CSVFileSchemaMetadata(Main.class.getResource("/column-names/oracle-final.csv"));

    public static void main(String[] args) throws Exception {
        // Подключиться ко всех исходным БД
        Collection<Storage> sourceStorages = connectToSourceDatabases();
        // Подключиться к временной БД
        WritableStorage temporaryStorage = connectToTemporaryStorage();

        // ШАГ 1: Преобразование и экспорт
        // Для каждой исходной БД
        for (Storage sourceStorage : sourceStorages) {
            //      Для каждой таблицы в исходной БД
            for (Table table : sourceStorage.getTables()) {
                //          Для каждой строки из таблицы исходной БД
                for (Record record : table.readAllRecords()) {
                    // Проверить, что в строке нет колонок, отсутствующих в целевой схеме
                    validateRecord(record);
                    // Записать строку и признак исходного хранилища в временное хранилище
                    log.debug("Writing record to temporary storage: {}", record);
                    temporaryStorage.writeRecord(record);
                }
            }
        }

        validateSchema(temporaryStorage);

        // ШАГ 2: Объединение и импорт
        // Подключиться к целевой БД
        try (TargetDatabase targetDatabase = connectToTargetDatabase()) {
            // Для каждой таблицы во временной БД
            for (ClusterizableTable table : temporaryStorage.getTables()) {
                // Сгруппировать записи в таблице по ИД
                Map<Object, Collection<Record>> recordsById = table.groupRecordsById();
                // Для каждого уникального ИД в таблице временной БД,
                // найти все строки (и признаки хранилища) с этим ИД
                recordsById.forEach((Object id, Collection<Record> recordsWithSameId) -> {
                    // Объединить строки с одинаковым ИД в одну запись
                    Record mergedRecord = mergeRecords(recordsWithSameId);
                    // Убрать лишние колонки из строки (возможна потеря данных!)
                    Record distilledRecord = distillRecord(mergedRecord);
                    // Сохранить объединённую запись в целевое хранилище
                    log.info("Writing record to target storage: {}", distilledRecord);
                    targetDatabase.writeRecord(distilledRecord);
                });
            }
        }

        log.info("Program complete");
    }

    private static void validateRecord(Record record) {
        // TODO: добавить проверку и бросить исключение, если строка содержит колонки, отсутствующие в целевой БД
        // если недостающих колонок нет, не предпринимать никаких действий
    }

    private static void validateSchema(WritableStorage temporaryStorage) {
        for (TableMetadata tableMetadata : SCHEMA_METADATA.allTables()) {
            String tableName = tableMetadata.name();
            if (!temporaryStorage.hasTable(tableName)) {
                log.warn("Temporary storage has no table {}", tableName);
                continue;
            }
            validateTable(temporaryStorage.getTableByName(tableName));
        }
    }

    private static void validateTable(ClusterizableTable table) {
        String tableName = table.getName();
        List<String> sourceColumnNames = table.groupRecordsById().values().stream()
                .flatMap(Collection::stream)
                .flatMap(record -> record.getColumnValues().keySet().stream())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        Collection<String> targetColumnNames = SCHEMA_METADATA.findTableByName(tableName).columns().stream()
                .map(ColumnMetadata::name)
                .sorted()
                .collect(Collectors.toList());
        log.info("Table '{}': Columns in source databases: {}", tableName, sourceColumnNames);
        log.info("Table '{}': Columns in target database:  {}", tableName, targetColumnNames);
        Collection<String> unknownColumnNames = CollectionUtils.subtract(sourceColumnNames, targetColumnNames);
        Collection<String> unmappedColumnNames = CollectionUtils.subtract(targetColumnNames, sourceColumnNames);
        if (!unknownColumnNames.isEmpty()) {
            log.warn("Unknown columns in source tables '{}': Columns present in source DBs, but missing from target DB: {}",
                    tableName, unknownColumnNames);
        }
        if (!unmappedColumnNames.isEmpty()) {
            log.warn("Unmapped columns in target table '{}': Columns missing from source DBs, but must be in target DB: {}",
                    tableName, unmappedColumnNames);
        }
    }

    private static Record distillRecord(Record record) {
        Collection<String> targetTableColumns = SCHEMA_METADATA.findTableByName(record.getTableName())
                .columns().stream()
                .map(ColumnMetadata::name)
                .collect(Collectors.toList());

        Map<String, Object> valuesForTargetTableColumns = EntryStream.of(record.getColumnValues())
                .filterKeys(targetTableColumns::contains)
                .toMap();

        return SimpleRecord.copyOf(record).withColumnValues(valuesForTargetTableColumns);
    }

    private static TargetDatabase connectToTargetDatabase() {
        Jdbi jdbi = Jdbi.create("jdbc:oracle:thin:@localhost:1521:XE", "hibd", "hibd");
        return new OracleTargetDatabase(jdbi);
    }

    private static Record mergeRecords(Collection<Record> records) {
        // Объединить данные для одной и той же строки из разных БД в одну строку для целевой БД.
        // TODO: Для каждой колонки выбрать не-NULL значения, из них - значение из БД с наибольшим приоритетом.
        if (records.size() == 1) {
            return records.iterator().next();
        }
        throw new IllegalStateException(
                "don't know how to merge " + records.size() + " records:\n" +
                records.stream()
                        .map(record -> "    - " + record.toString())
                        .collect(Collectors.joining("\n")));
    }

    private static WritableStorage connectToTemporaryStorage() {
        return new InMemoryWritableStorage();
    }

    private static Collection<Storage> connectToSourceDatabases() {
        Storage mongoStorage = connectToMongoDatabase();
        Storage mysqlStorage = connectToMysqlStorage();
        Storage oracleStorage = connectToOracleStorage();
        Storage postgresqlStorage = connectToPostgresqlStorage();
        return List.of(mongoStorage, mysqlStorage, oracleStorage, postgresqlStorage);
    }

    private static Storage connectToMongoDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://admin:admin@localhost/");
        com.mongodb.client.MongoDatabase mongoDatabaseConnection = mongoClient.getDatabase("test");
        return new MongoStorage(mongoDatabaseConnection);
    }

    private static Storage connectToMysqlStorage() {
        Jdbi mysqlJdbiConnection = Jdbi.create("jdbc:mysql://localhost:3306/mysqldb", "root", "admin");
        return new MysqlStorage(mysqlJdbiConnection);
    }

    private static Storage connectToOracleStorage() {
        Jdbi oracleJdbiConnection = Jdbi.create("jdbc:oracle:thin:@localhost:1522:XE", "orac", "orac");
        return new OracleStorage(oracleJdbiConnection);
    }

    private static Storage connectToPostgresqlStorage() {
        Jdbi postgresqlJdbiConnection = Jdbi.create("jdbc:postgresql://localhost:5010/student", "student", "student");
        return new PostgresqlStorage(postgresqlJdbiConnection);
    }
}
