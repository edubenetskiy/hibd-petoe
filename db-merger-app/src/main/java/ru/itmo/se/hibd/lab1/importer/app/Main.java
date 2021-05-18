package ru.itmo.se.hibd.lab1.importer.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.lab1.importer.core.ClusterizableTable;
import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.Storage;
import ru.itmo.se.hibd.lab1.importer.core.Table;
import ru.itmo.se.hibd.lab1.importer.core.TargetDatabase;
import ru.itmo.se.hibd.lab1.importer.core.WritableStorage;
import ru.itmo.se.hibd.petoe.database.mongo.MongoStorage;
import ru.itmo.se.hibd.petoe.database.mysql.MysqlStorage;
import ru.itmo.se.hibd.petoe.database.oracle.OracleTargetDatabase;
import ru.itmo.se.hibd.petoe.inmemorystorage.InMemoryWritableStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class Main {
    public static void main(String[] args) {
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

        // ШАГ 2: Объединение и импорт
        // Подключиться к целевой БД
        TargetDatabase targetDatabase = connectToTargetDatabase();
        // Для каждой таблицы во временной БД
        for (ClusterizableTable table : temporaryStorage.getTables()) {
            // Сгруппировать записи в таблице по ИД
            Map<Object, Collection<Record>> recordsById = table.groupRecordsById();
            // Для каждого уникального ИД в таблице временной БД,
            // найти все строки (и признаки хранилища) с этим ИД
            recordsById.forEach((Object id, Collection<Record> recordsWithSameId) -> {
                // Объединить строки с одинаковым ИД в одну запись
                Record mergedRecord = mergeRecords(recordsWithSameId);
                // Сохранить объединённую запись в целевое хранилище
                targetDatabase.writeRecord(mergedRecord);
            });
        }
    }

    private static void validateRecord(Record record) {
        // TODO: добавить проверку и бросить исключение, если строка содержит колонки, отсутствующие в целевой БД
        // если недостающих колонок нет, не предпринимать никаких действий
    }

    private static TargetDatabase connectToTargetDatabase() {
        // TODO: Подключиться к целевой схеме Oracle и создать экземпляр TargetDatabase
        Jdbi jdbi = Jdbi.create("jdbc:oracle:thin:@localhost:1521:XE", "hibd", "hibd");
        return new OracleTargetDatabase(jdbi);
    }

    private static Record mergeRecords(Collection<Record> records) {
        // Объединить данные для одной и той же строки из разных БД в одну строку для целевой БД.
        // TODO: Для каждой колонки выбрать не-NULL значения, из них - значение из БД с наибольшим приоритетом.
        if (records.size() == 1) {
            return records.iterator().next();
        }
        throw new IllegalStateException("don't know how to merge records");
    }

    private static WritableStorage connectToTemporaryStorage() {
        return new InMemoryWritableStorage();
    }

    private static Collection<Storage> connectToSourceDatabases() {
        // TODO: Подключиться к БД Mongo и создать экземпляр SourceDatabase для этой БД
        Storage mongoStorage = connectToMongoDatabase();
        // TODO: Подключиться к БД MySQL и создать экземпляр SourceDatabase для этой БД
        Storage mysqlStorage = connectToMysqlStorage();
        // TODO: Подключиться к БД Oracle и создать экземпляр SourceDatabase для этой БД
        // TODO: Подключиться к БД PostgreSQL и создать экземпляр SourceDatabase для этой БД
        // TODO: Вернуть коллекцию подключений ко всем БД
        return List.of(mongoStorage, mysqlStorage);
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
}
