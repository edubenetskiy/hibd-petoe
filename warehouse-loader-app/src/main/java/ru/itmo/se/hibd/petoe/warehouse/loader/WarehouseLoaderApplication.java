package ru.itmo.se.hibd.petoe.warehouse.loader;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ru.itmo.se.hibd.petoe.warehouse.loader.domain.Dormitory;
import ru.itmo.se.hibd.petoe.warehouse.loader.domain.Semester;
import ru.itmo.se.hibd.petoe.warehouse.loader.domain.SemesterImpl;
import ru.itmo.se.hibd.petoe.warehouse.loader.fake.PublisherGenerator;
import ru.itmo.se.hibd.petoe.warehouse.loader.generators.FactTable1Generator;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
public class WarehouseLoaderApplication {

    static {
//        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    public static final Semester MIN_SEMESTER = new SemesterImpl(YearMonth.of(1990, 3));
    public static final Semester MAX_SEMESTER = new SemesterImpl(YearMonth.of(2025, 9));
    public static final List<Semester> ALL_SEMESTERS = IntStream.rangeClosed(1990, 2025)
            .boxed()
            .flatMap(year -> Stream.of(
                    new SemesterImpl(YearMonth.of(year, Month.SEPTEMBER)),
                    new SemesterImpl(YearMonth.of(year + 1, Month.MARCH))
            ))
            .collect(toList());
    public static List<String> ALL_DISTRICTS = readFakeDistricts();

    private final UniformRealDistribution studentsWith5PlusDistribution = new UniformRealDistribution(0.0, 0.3);
    private final UniformRealDistribution studentsWith4PlusDistribution = new UniformRealDistribution(0.3, 0.7);
    private final UniformRealDistribution studentsWith3PlusDistribution = new UniformRealDistribution(0.7, 1);

    @SneakyThrows
    private static List<String> readFakeDistricts() {
        return new String(
                WarehouseLoaderApplication.class.getResource("/additional-data/districts.txt").openStream().readAllBytes(),
                StandardCharsets.UTF_8
        ).lines().filter(StringUtils::isNotBlank).collect(toList());
    }

    public static final String INSERT_FACT_TABLE_1 =
            "INSERT INTO orac3rd.facttable1\n" +
            "(" +
            " id, diploma_with_honor, usual_diploma, num_of_publications, num_of_student_lib_cards\n" +
            ", num_of_conferences, num_of_employee_lib_cards, people_live_in_campus\n" +
            ", people_not_live_in_campus, timeid" +
            ")\n" +
            "VALUES (" +
            " :id, :diploma_with_honor, :usual_diploma, :num_of_publications, :num_of_student_lib_cards, :num_of_conferences\n" +
            "       , :num_of_employee_lib_cards, :people_live_in_campus, :people_not_live_in_campus, :timeid)";

    @Inject @Named
    Jdbi sourceJdbi;

    @Inject @Named
    Jdbi warehouseJdbi;

    @Inject
    PublisherGenerator publisherGenerator;

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            container.select(WarehouseLoaderApplication.class).get().run(args);
        }
    }

    private void run(String[] args) {
        warehouseJdbi.useHandle(warehouse -> {
            sourceJdbi.useHandle(sourceHandle -> {
                populateDbTime(warehouse);
                populateFactTable1(warehouse, sourceHandle);
                populateFactTable2(warehouse, sourceHandle);
                populateFactTable3(warehouse, sourceHandle);
                populateFactTable4(warehouse, sourceHandle);
            });
        });

        log.info("Program complete");
    }

    private static void populateFactTable1(org.jdbi.v3.core.Handle warehouse, org.jdbi.v3.core.Handle sourceHandle) {
        ProgressBar.wrap(ALL_SEMESTERS.parallelStream(), "Writing FactTable1")
                .forEach(semester -> {
                    Map<String, ?> row = FactTable1Generator.createRowForFactTable1(sourceHandle, semester);
                    try {
                        warehouse.createUpdate(INSERT_FACT_TABLE_1)
                                .bindMap(row)
                                .execute();
                        log.info("Written FACTTABLE1 record for {}", semester);
                    } catch (Exception e) {
                        log.error("Failed to INSERT into FACTTABLE1", e);
                    }
                });
    }

    private static void populateFactTable2(Handle warehouse, Handle sourceDatabase) {
        // count of people, birth place ID, time ID
        List<Map<String, Object>> allPlacesOfBirth = findAllBirthPlaces(sourceDatabase);
        allPlacesOfBirth.forEach(placeOfBirth -> {
            try {
                warehouse.createUpdate("INSERT INTO orac3rd.birthplace (id, district, city, country) VALUES (:id, :district, :city, :country)")
                        .bindMap(placeOfBirth)
                        .execute();
                log.info("Successful INSERT into BIRTHPLACE");
            } catch (Exception e) {
                log.error("Failed to INSERT into BIRTHPLACE", e);
            }
        });

        log.info("All places of birth: {}", allPlacesOfBirth);
        LocalDate minDate = sourceDatabase.select("SELECT MIN(date_of_birth) FROM ((SELECT date_of_birth FROM hibd.student) UNION (SELECT date_of_birth FROM hibd.teacher))").mapTo(LocalDate.class).one();
        LocalDate maxDate = sourceDatabase.select("SELECT MAX(date_of_birth) FROM ((SELECT date_of_birth FROM hibd.student) UNION (SELECT date_of_birth FROM hibd.teacher))").mapTo(LocalDate.class).one();
        log.info("Min date: {}; max date: {}", minDate, maxDate);
        Semester minSemester = new SemesterImpl(YearMonth.of(minDate.getYear() - 1, Month.SEPTEMBER));
        Semester maxSemester = new SemesterImpl(YearMonth.of(maxDate.getYear() + 1, Month.MARCH));
        List<Semester> allSemesters = Stream.iterate(minSemester, semester -> semester.next().compareTo(maxSemester) <= 0, Semester::next).collect(toList());

        try (ProgressBar progressBar = new ProgressBar("Writing FactTable2", (long) allSemesters.size() * allPlacesOfBirth.size())) {
            StreamEx.of(allSemesters).cross(allPlacesOfBirth)
                    .forEach((entry) -> {
                        if (progressBar.getCurrent() >= 20_000) {
                            return;
                        }
                        var semester = entry.getKey();
                        var placeOfBirth = entry.getValue();
                        Map<String, ?> rowForFactTable2 = createRowForFactTable2(sourceDatabase, semester, placeOfBirth);
                        try {
                            warehouse.createUpdate("INSERT INTO orac3rd.facttable2 (id, num_of_people_1st_course, birthplaceid, timeid) " +
                                                   "VALUES (:id, :num_of_people_1st_course, :birthplaceid, :timeid)")
                                    .bindMap(rowForFactTable2)
                                    .execute();
                            log.info("Successful INSERT into FACTTABLE2 for {} and {}: {}", semester, placeOfBirth, rowForFactTable2);
                        } catch (Exception e) {
                            log.error("Failed to INSERT into FACTTABLE2: {}", rowForFactTable2, e);
                        } finally {
                            progressBar.step();
                        }
                    });
            progressBar.close();
        }
    }

    private static List<Map<String, Object>> findAllBirthPlaces(Handle sourceDatabase) {
        return sourceDatabase.select("SELECT DISTINCT country_of_birth country, city_of_birth city FROM student " +
                                     "UNION " +
                                     "SELECT DISTINCT country_of_birth, city_of_birth FROM teacher")
                .mapToMap()
                .map(place -> {
                    place.put("id", ThreadLocalRandom.current().nextInt());
                    place.put("district", ALL_DISTRICTS.get(new Random().nextInt(ALL_DISTRICTS.size())));
                    return place;
                })
                .stream()
                .filter(map -> map.get("country") != null && map.get("city") != null)
                .collect(toList());
    }

    private static Map<String, ?> createRowForFactTable2(Handle sourceDatabase, Semester semester, Map<String, Object> placeOfBirth) {
        Integer numberOfStudentsBorn = sourceDatabase.select(
                "SELECT COUNT(1) FROM hibd.student " +
                "WHERE (date_of_birth BETWEEN :semester_start AND :semester_end) " +
                "AND (country_of_birth = :country) AND (city_of_birth = :city)")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .bindMap(placeOfBirth)
                .mapTo(Integer.class)
                .one();
        return Map.ofEntries(
                Map.entry("id", ThreadLocalRandom.current().nextInt()),
                Map.entry("timeid", semester.id()),
                Map.entry("birthplaceid", placeOfBirth.get("id")),
                Map.entry("num_of_people_1st_course", numberOfStudentsBorn)
        );
    }

    private void populateFactTable3(Handle warehouse, Handle sourceHandle) {
        List<Map<String, Object>> allPublishers = sourceHandle.select("SELECT DISTINCT edition_place city FROM hibd.publication WHERE edition_place IS NOT NULL")
                .mapToMap()
                .map(map -> {
                    map.put("id", map.get("city").hashCode());
                    map.put("publisher", publisherGenerator.nextPublisher());
                    map.put("country", "Россия");
                    return map;
                })
                .list();
        allPublishers.forEach(publisher -> {
            try {
                warehouse.createUpdate("INSERT INTO orac3rd.publisher (id, publisher, city, country) VALUES (:id, :publisher, :city, :country)")
                        .bindMap(publisher)
                        .execute();
                log.info("Successful INSERT into PUBLISHER: {}", publisher);
            } catch (Exception e) {
                log.error("Failed to INSERT into PUBLISHER: {}", publisher, e);
            }
        });

        LocalDate minDate = sourceHandle.select("SELECT MIN(publication_date) FROM hibd.publication WHERE publication_date IS NOT NULL").mapTo(LocalDate.class).one();
        LocalDate maxDate = sourceHandle.select("SELECT MAX(publication_date) FROM hibd.publication WHERE publication_date IS NOT NULL").mapTo(LocalDate.class).one();
        log.info("Min date: {}; max date: {}", minDate, maxDate);
        Semester minSemester = new SemesterImpl(YearMonth.of(minDate.getYear() - 1, Month.SEPTEMBER));
        Semester maxSemester = new SemesterImpl(YearMonth.of(maxDate.getYear() + 1, Month.MARCH));
        List<Semester> allSemesters = Stream.iterate(minSemester, semester -> semester.next().compareTo(maxSemester) <= 0, Semester::next).collect(toList());
        // TODO: 20/06/2021 These semesters may not be in DB_TIME table, insert?

        try (ProgressBar progressBar = new ProgressBar("Writing FactTable3", allPublishers.size() * allSemesters.size())) {
            int numRowsCreated = StreamEx.of(allPublishers).cross(allSemesters)
                    .mapKeyValue((publisher, semester) -> {
                        return generateRowForFactTable3(sourceHandle, publisher, semester);
                    })
                    .mapToInt(rowForFactTable3 -> {
                        try {
                            int updateCount = warehouse.createUpdate("INSERT INTO orac3rd.facttable3 (id, num_of_people_master, publisher_id, timeid) " +
                                                                     "VALUES (:id, :num_of_people_master, :publisher_id, :timeid)")
                                    .bindMap(rowForFactTable3)
                                    .execute();
                            log.info("Successful INSERT into FACTTABLE3: {}", rowForFactTable3);
                            return updateCount;
                        } catch (Exception e) {
                            log.info("Failed to INSERT into FACTTABLE3: {}", rowForFactTable3, e);
                            return 0;
                        } finally {
                            progressBar.step();
                        }
                    })
                    .sum();
            log.info("Created {} rows in FACTTABLE3", numRowsCreated);
        }
    }

    private Map<String, ?> generateRowForFactTable3(Handle sourceHandle, Map<String, Object> publisher, Semester semester) {
        Integer studentsCount = sourceHandle.select("SELECT COUNT(DISTINCT s.name)\n" +
                                                    "FROM hibd.publication p\n" +
                                                    "         JOIN hibd.coauthors_student cs" +
                                                    " ON p.id = cs.publication_id\n" +
                                                    "         JOIN hibd.student s ON cs.student_id = s.id\n" +
                                                    "WHERE (p.publication_date BETWEEN :semester_start AND :semester_end)\n" +
                                                    "  AND (p.edition_place = :city)")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .bind("city", publisher.get("city"))
                .mapTo(Integer.class)
                .one();
        Integer teachersCount = sourceHandle.select("SELECT COUNT(DISTINCT t.name)\n" +
                                                    "FROM hibd.publication p\n" +
                                                    "         JOIN hibd.coauthors_teacher ct" +
                                                    " ON p.id = ct.publication_id\n" +
                                                    "         JOIN hibd.teacher t ON ct.teacher_id = t.id\n" +
                                                    "WHERE (p.publication_date BETWEEN :semester_start AND :semester_end)\n" +
                                                    "  AND (p.edition_place = :city)")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .bind("city", publisher.get("city"))
                .mapTo(Integer.class)
                .one();
        Map<String, ?> rowForFactTable3 = Map.ofEntries(
                Map.entry("id", Objects.hash(publisher.get("id"), semester.id())),
                Map.entry("num_of_people_master", studentsCount + teachersCount), // TODO: Clarify meaning of "master"?
                Map.entry("publisher_id", publisher.get("id")),
                Map.entry("timeid", semester.id())
        );
        return rowForFactTable3;
    }

    private void populateFactTable4(Handle warehouse, Handle sourceDatabase) {
        log.info("Start populating FACTTABLE4");
        List<Dormitory> allCampuses = sourceDatabase.select(
                "WITH summary AS (\n" +
                "    SELECT h.id\n" +
                "         , h.address\n" +
                "         , h.number_of_rooms\n" +
                "         , ROW_NUMBER() OVER (PARTITION BY h.id ORDER BY h.number_of_rooms DESC ) AS rank\n" +
                "    FROM hibd.dorm h\n" +
                ")\n" +
                "SELECT *\n" +
                "FROM summary\n" +
                "WHERE rank = 1")
                .mapToBean(Dormitory.class)
                .list();
        log.info("Having total {} campuses (dormitories)", allCampuses.size());
        allCampuses.forEach(campus -> {
            try {
                warehouse.createUpdate("INSERT INTO orac3rd.campus (id, address)\n" +
                                       "VALUES (:id, :address)")
                        .bind("id", campus.getId())
                        .bind("address", campus.getAddress())
                        .execute();
                log.info("Successful INSERT into CAMPUS: {}", campus);
            } catch (Exception e) {
                log.error("Failed to INSERT into CAMPUS: {}", campus, e);
            }
        });

        LocalDate minDate = sourceDatabase.select("SELECT MIN(date_of_entry) FROM hibd.student_dorm_info").mapTo(LocalDate.class).one();
        LocalDate maxDate = sourceDatabase.select("SELECT MAX(date_of_contract_expire) FROM hibd.student_dorm_info").mapTo(LocalDate.class).one();
        log.info("Min date: {}; max date: {}", minDate, maxDate);
        Semester minSemester = new SemesterImpl(YearMonth.of(minDate.getYear() - 1, Month.SEPTEMBER));
        Semester maxSemester = new SemesterImpl(YearMonth.of(maxDate.getYear() + 1, Month.MARCH));
        List<Semester> allSemesters = Stream.iterate(minSemester, semester -> semester.next().compareTo(maxSemester) <= 0, Semester::next).collect(toList());

        try (ProgressBar progressBar = new ProgressBar("Writing FactTable4", (long) allCampuses.size() * allSemesters.size())) {
            int numRowsCreated = StreamEx.of(allCampuses).cross(allSemesters)
                    .mapKeyValue(((dormitory, semester) -> createRowForFactTable4(sourceDatabase, dormitory, semester)))
                    .mapToInt(row -> {
                        if (progressBar.getCurrent() < 20_000) {
                            try {
                                int updatedRows = warehouse.createUpdate(
                                        "INSERT INTO orac3rd.facttable4 (" +
                                                " id, avg_num_of_persons_in_one_room, students_with_only_5_marks\n" +
                                                "                               , students_with_only_4_5_marks, students_with_3_4_5_marks, num_of_students_with_debts\n" +
                                                "                               , campusid, timeid" +
                                                ")\n" +
                                                "VALUES (" +
                                                " :id, :avg_num_of_persons_in_one_room, :students_with_only_5_marks, :students_with_only_4_5_marks\n" +
                                                "       , :students_with_3_4_5_marks, :num_of_students_with_debts, :campusid, :timeid" +
                                                ")")
                                        .bindMap(row)
                                        .execute();
                                log.info("Successful INSERT into FACTTABLE4: {}", row);
                                return updatedRows;
                                //      }
                            } catch (Exception e) {
                                log.error("Failed to INSERT into FACTTABLE4: {}", row, e);
                                return 0;
                            } finally {
                                progressBar.step();
                            }
                        }
                        return 0;
                    })
                    .sum();
            log.info("Created {} rows in FACTTABLE4", numRowsCreated);
        }
    }

    private Map<String, ?> createRowForFactTable4(Handle sourceDatabase, Dormitory dormitory, Semester semester) {

        Double avgNumOfPersonsInOneRoom = sourceDatabase.select("SELECT AVG(COUNT(DISTINCT student_id))\n" +
                                                                "FROM hibd.student_dorm_info\n" +
                                                                "         JOIN hibd.room ON student_dorm_info.room_id = room.id\n" +
                                                                "         JOIN hibd.dorm ON room.dorm_id = dorm.id\n" +
                                                                "WHERE date_of_entry <= :semester_end\n" +
                                                                "  AND date_of_contract_expire >= :semester_end\n" +
                                                                "  AND dorm_id = :dorm_id\n" +
                                                                "GROUP BY room_id\n")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .bind("dorm_id", dormitory.getId())
                .mapTo(Double.class)
                .findOne().orElse(0.0D);

        Integer totalStudentsFromSemesterAndDorm = sourceDatabase.select(
                "SELECT COUNT(DISTINCT s.id)\n" +
                "FROM hibd.student s\n" +
                "         JOIN study_group sg ON s.study_group_id = sg.id\n" +
                "         JOIN student_dorm_info sdi ON s.id = sdi.student_id\n" +
                "         JOIN room r ON sdi.room_id = r.id\n" +
                "WHERE (:semester_end\n" +
                "    BETWEEN ADD_MONTHS(sg.start_study_date, -(sg.course_number - 1) * 12)\n" +
                "    AND ADD_MONTHS(sg.end_study_date, (6 - sg.course_number) * 12))\n" +
                "  AND (sdi.date_of_entry <= :semester_end)\n" +
                "  AND (sdi.date_of_contract_expire >= :semester_end)\n" +
                "  AND (r.dorm_id = :dorm_id" +
                ")")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .bind("dorm_id", dormitory.getId())
                .mapTo(Integer.class)
                .findOne().orElse(0);

        int studentsWith5Plus = (int) (totalStudentsFromSemesterAndDorm * studentsWith5PlusDistribution.sample());
        int studentsWith4Plus = (int) (totalStudentsFromSemesterAndDorm * studentsWith4PlusDistribution.sample());
        int studentsWith3Plus = (int) (totalStudentsFromSemesterAndDorm * studentsWith3PlusDistribution.sample());
        int studentsWithDebts = totalStudentsFromSemesterAndDorm - studentsWith3Plus;

        return Map.ofEntries(
                Map.entry("id", Objects.hash(dormitory.getId(), semester.id())),
                Map.entry("avg_num_of_persons_in_one_room", avgNumOfPersonsInOneRoom),
                Map.entry("students_with_only_5_marks", studentsWith5Plus),
                Map.entry("students_with_only_4_5_marks", studentsWith4Plus),
                Map.entry("students_with_3_4_5_marks", studentsWith3Plus),
                Map.entry("num_of_students_with_debts", studentsWithDebts),
                Map.entry("campusid", dormitory.getId()),
                Map.entry("timeid", semester.id())
        );
    }

    private int countStudentsWithMinMark(Handle sourceDatabase, Semester semester, Dormitory dormitory,
                                         int minMark, int minPoints) {
        return sourceDatabase.select("SELECT COUNT(DISTINCT record_book.student_id)\n" +
                                     "FROM hibd.record_book\n" +
                                     "         JOIN hibd.student ON record_book.student_id = student.id\n" +
                                     "         JOIN hibd.student_dorm_info ON student.id = student_dorm_info.student_id\n" +
                                     "         JOIN hibd.room ON student_dorm_info.room_id = room.id\n" +
                                     "         " +
                                     "JOIN hibd.dorm ON room.dorm_id = dorm.id\n" +
                                     "WHERE dorm.id = :dorm_id\n" +
                                     "AND ((record_book.date_of_completion BETWEEN :semester_start AND :semester_end)\n" +
                                     "    OR (hibd.record_book.exam_date BETWEEN :semester_start AND :semester_end)" +
                                     ")\n" +
                                     "GROUP BY record_book.student_id\n" +
                                     "HAVING MIN(mark) >= :min_mark\n" +
                                     "   AND MIN(points) >= :min_points")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .bind("dorm_id", dormitory.getId())
                .bind("min_mark", minMark)
                .bind("min_points", minPoints)
                .mapTo(Integer.class)
                .findOne().orElse(0);
    }

    private static void populateDbTime(org.jdbi.v3.core.Handle warehouse) {
        ALL_SEMESTERS.parallelStream().forEach(semester -> {
            try {
                warehouse.createUpdate("INSERT INTO orac3rd.db_time (id, term, year) VALUES (:id, :term, :year)")
                        .bind("id", semester.id())
                        .bind("term", semester.semesterNumberInAcademicYear())
                        .bind("year", semester.yearOfStart())
                        .execute();
            } catch (Exception e) {
                log.error("Failed to INSERT a semester into DB_TIME: {}", semester, e);
            }
        });
    }
}
