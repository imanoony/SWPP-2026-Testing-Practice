package com.example.swpp.tododapp.tasks

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueDateUtilsTest {
    private val today = LocalDate.of(2026, 10, 20)

    @Test
    fun dueDateLabel_completedFuture_returnsClear() {
        assertEquals("CLEAR", dueDateLabel(LocalDate.of(2026, 10, 21), today, true))
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        assertEquals("CLEAR", dueDateLabel(null, today, true))
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        assertEquals("", dueDateLabel(null, today, false))
    }

    @Test
    fun dueDateLabel_futureDueDate_returnsCountdown() {
        assertEquals("D-3", dueDateLabel(LocalDate.of(2026, 10, 23), today, false))
    }

    @Test
    fun dueDateLabel_pastDueDate_returnsElapsedDays() {
        assertEquals("D+3", dueDateLabel(LocalDate.of(2026, 10, 17), today, false))
    }

    @Test
    fun dueDateLabel_dueToday_returnsDayLabel() {
        assertEquals("D-Day", dueDateLabel(today, today, false))
    }

    @Test
    fun dueDateLabel_oneYearFuture_returnsFullDistance() {
        assertEquals("D-365", dueDateLabel(LocalDate.of(2027, 10, 20), today, false))
    }

    @Test
    fun dueDateLabel_monthBoundary_returnsCalendarDayDistance() {
        assertEquals("D-29", dueDateLabel(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 1, 31), false))
    }

    @Test
    fun dueDateLabel_nullDueDate_doesNotThrowAndReturnsEmpty() {
        assertEquals("", dueDateLabel(null, today, false))
    }

    @Test
    fun dueDateLabel_completedToday_returnsClear() {
        assertEquals("CLEAR", dueDateLabel(today, today, true))
    }

    @Test
    fun dueDateLabel_completedOverdue_returnsClear() {
        assertEquals("CLEAR", dueDateLabel(LocalDate.of(2026, 10, 17), today, true))
    }

    @Test
    fun dueDateLabel_adjacentDays_returnsSignedOneDayLabels() {
        assertEquals("D+1", dueDateLabel(LocalDate.of(2026, 10, 19), today, false))
        assertEquals("D-Day", dueDateLabel(today, today, false))
        assertEquals("D-1", dueDateLabel(LocalDate.of(2026, 10, 21), today, false))
    }

    @Test
    fun dueDateLabel_monthBoundaryPast_returnsCalendarDayDistance() {
        assertEquals("D+29", dueDateLabel(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 3, 1), false))
    }

    @Test
    fun dueDateLabel_leapYearFutureBoundary_countsLeapDay() {
        assertEquals("D-2", dueDateLabel(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 2, 28), false))
    }

    @Test
    fun dueDateLabel_leapYearPastBoundary_countsLeapDay() {
        assertEquals("D+2", dueDateLabel(LocalDate.of(2024, 2, 28), LocalDate.of(2024, 3, 1), false))
    }

    @Test
    fun dueDateLabel_nonLeapYearBoundary_countsOneDay() {
        assertEquals("D-1", dueDateLabel(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 2, 28), false))
    }

    @Test
    fun dueDateLabel_yearBoundaryFuture_countsOneDay() {
        assertEquals("D-1", dueDateLabel(LocalDate.of(2027, 1, 1), LocalDate.of(2026, 12, 31), false))
    }

    @Test
    fun dueDateLabel_yearBoundaryPast_countsOneDay() {
        assertEquals("D+1", dueDateLabel(LocalDate.of(2026, 12, 31), LocalDate.of(2027, 1, 1), false))
    }
}
