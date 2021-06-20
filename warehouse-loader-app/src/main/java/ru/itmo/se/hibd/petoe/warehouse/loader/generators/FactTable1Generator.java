package ru.itmo.se.hibd.petoe.warehouse.loader.generators;

import org.apache.commons.lang3.Range;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import ru.itmo.se.hibd.petoe.warehouse.loader.domain.Semester;

import java.util.Map;

public class FactTable1Generator {
    public static Map<String, ?> createRowForFactTable1(org.jdbi.v3.core.Handle sourceHandle, Semester semester) {
        Integer conferenceCount = sourceHandle.select("SELECT COUNT(1) FROM conference WHERE conference.conference_date BETWEEN :semester_start AND :semester_end")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .mapTo(Integer.class)
                .one();

        Integer publicationCount = sourceHandle.select("SELECT COUNT(1) FROM publication WHERE publication_date BETWEEN :semester_start AND :semester_end")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .mapTo(Integer.class)
                .one();

        Integer libraryCardCount = sourceHandle.select("SELECT COUNT(1) FROM library_card WHERE receive_date BETWEEN :semester_start AND :semester_end")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .mapTo(Integer.class)
                .one();

        Integer peopleInCampusCount = sourceHandle.select("SELECT COUNT(DISTINCT student_id) FROM student_dorm_info " +
                                                          "WHERE student_dorm_info.date_of_entry <= :semester_start " +
                                                          "AND student_dorm_info.date_of_contract_expire >= :semester_end")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .mapTo(Integer.class)
                .one();

        Integer peopleOutsideCampusCount = sourceHandle.select("SELECT COUNT(DISTINCT student_id) FROM student_dorm_info " +
                                                               "WHERE NOT (" +
                                                               "   student_dorm_info.date_of_entry <= :semester_start " +
                                                               "   AND student_dorm_info.date_of_contract_expire >= :semester_end" +
                                                               ")")
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .mapTo(Integer.class)
                .one();

        Integer totalDiploma = sourceHandle.select(
                "SELECT COUNT(student.id) FROM student JOIN study_group sg ON student.study_group_id = sg.id WHERE sg.end_study_date BETWEEN :semester_start AND :semester_end"
        )
                .bind("semester_start", semester.minDate())
                .bind("semester_end", semester.maxDateExclusive())
                .mapTo(Integer.class)
                .one();

        Double fractionOfDiplomaWithHonor = Range.between(0.0, 1.0).fit(new ExponentialDistribution(0.1).sample());
        Integer diplomaWithHonor = (int) Math.floor(totalDiploma * fractionOfDiplomaWithHonor);
        Integer diplomaWithoutHonor = totalDiploma - diplomaWithHonor;

        Map<String, ?> row = Map.ofEntries(
                Map.entry("id", semester.id()),
                Map.entry("diploma_with_honor", diplomaWithHonor),
                Map.entry("usual_diploma", diplomaWithoutHonor),
                Map.entry("num_of_publications", publicationCount),
                Map.entry("num_of_student_lib_cards", libraryCardCount),
                Map.entry("num_of_conferences", conferenceCount),
                Map.entry("num_of_employee_lib_cards", libraryCardCount),
                Map.entry("people_live_in_campus", peopleInCampusCount),
                Map.entry("people_not_live_in_campus", peopleOutsideCampusCount),
                Map.entry("timeid", semester.id())
        );
        return row;
    }
}
