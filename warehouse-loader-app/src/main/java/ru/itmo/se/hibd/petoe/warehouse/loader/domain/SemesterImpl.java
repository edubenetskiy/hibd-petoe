package ru.itmo.se.hibd.petoe.warehouse.loader.domain;

import java.time.LocalDate;
import java.time.YearMonth;

public final class SemesterImpl implements Semester {

    private final YearMonth firstMonth;

    public SemesterImpl(YearMonth firstMonth) {
        switch (firstMonth.getMonth()) {
            case SEPTEMBER:
            case MARCH:
                break;
            default:
                throw new IllegalArgumentException("wrong start month for semester: " + firstMonth);
        }
        this.firstMonth = firstMonth;
    }

    @Override public int id() {
        return firstMonth.getYear() * 12 + firstMonth.getMonthValue();
    }

    @Override public Semester next() {
        return new SemesterImpl(firstMonth.plus(LENGTH));
    }

    @Override public Semester previous() {
        return new SemesterImpl(firstMonth.minus(LENGTH));
    }

    @Override public LocalDate minDate() {
        return firstMonth.atDay(1);
    }

    @Override public LocalDate maxDateExclusive() {
        return firstMonth.plus(LENGTH).atDay(1);
    }

    @Override public int yearOfStart() {
        return firstMonth.minusMonths(6).getYear();
    }

    @Override public int semesterNumberInAcademicYear() {
        return (firstMonth.getMonthValue() > 6) ? 1 : 2;
    }

    @Override public YearMonth startYearMonth() {
        return this.firstMonth;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SemesterImpl)) return false;
        final Semester other = (Semester) o;
        final Object this$firstMonth = this.startYearMonth();
        final Object other$firstMonth = other.startYearMonth();
        if (this$firstMonth == null ? other$firstMonth != null : !this$firstMonth.equals(other$firstMonth))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $firstMonth = this.startYearMonth();
        result = result * PRIME + ($firstMonth == null ? 43 : $firstMonth.hashCode());
        return result;
    }

    public String toString() {
        return "Semester(start=" + this.startYearMonth() + ")";
    }
}
