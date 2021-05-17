package ru.itmo.se.hibd.lab1.importer.app;

import ru.itmo.se.hibd.lab1.importer.core.Record;
import ru.itmo.se.hibd.lab1.importer.core.SourceDatabase;
import ru.itmo.se.hibd.lab1.importer.core.Table;
import ru.itmo.se.hibd.lab1.importer.core.TargetDatabase;
import ru.itmo.se.hibd.lab1.importer.core.WritableStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Подключиться ко всех исходным БД
        Collection<SourceDatabase> sourceDatabases = connectToSourceDatabases();
        // Подключиться к временной БД
        WritableStorage temporaryStorage = connectToTemporaryStorage();
        // Подключиться к целевой БД
        TargetDatabase targetDatabase = connectToTargetDatabase();

        // ШАГ 1: Преобразование и экспорт
        // Для каждой исходной БД
        for (SourceDatabase sourceDatabase : sourceDatabases) {
            //      Для каждой таблицы в исходной БД
            for (Table table : sourceDatabase.getTables()) {
                //          Для каждой строки из таблицы исходной БД
                for (Record record : table.readAllRecords()) {
                    // Проверить, что в строке нет колонок, отсутствующих в целевой схеме
                    validateRecord(record);
                    // Записать строку и признак исходного хранилища в временное хранилище
                    temporaryStorage.writeRecord(record);
                }
            }
        }

        // ШАГ 2: Объединение и импорт
        // Для каждой таблицы во временной БД
        for (Table table : temporaryStorage.getTables()) {
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
        throw new UnsupportedOperationException("not implemented yet");
    }

    private static Record mergeRecords(Collection<Record> records) {
        // Объединить данные для одной и той же строки из разных БД в одну строку для целевой БД.
        // TODO: Для каждой колонки выбрать не-NULL значения, из них - значение из БД с наибольшим приоритетом.
        return null;
    }

    private static WritableStorage connectToTemporaryStorage() {
        // TODO: Создать примитивное хранилище в памяти на основе HashMap или других коллекций
        throw new UnsupportedOperationException("not implemented yet"); // TODO
    }

    private static Collection<SourceDatabase> connectToSourceDatabases() {
        // TODO: Подключиться к БД Mongo и создать экземпляр SourceDatabase для этой БД
        // TODO: Подключиться к БД MySQL и создать экземпляр SourceDatabase для этой БД
        // TODO: Подключиться к БД Oracle и создать экземпляр SourceDatabase для этой БД
        // TODO: Подключиться к БД PostgreSQL и создать экземпляр SourceDatabase для этой БД
        // TODO: Вернуть коллекцию подключений ко всем БД
        return List.of();
    }
}
