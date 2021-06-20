package ru.itmo.se.hibd.petoe.warehouse.loader.domain;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;

public interface Semester extends Comparable<Semester> {
    Period LENGTH = Period.ofMonths(6);

    int id();

    Semester next();

    Semester previous();

    int yearOfStart();

    YearMonth startYearMonth();

    int semesterNumberInAcademicYear();

    LocalDate minDate();

    LocalDate maxDateExclusive();

    @Override
    default int compareTo(Semester other) {
        return this.startYearMonth().compareTo(other.startYearMonth());
    }
}
