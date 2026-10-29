/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for domain [Transaction] model date recurrence calculations.
 */
class TransactionTest {

    private val startDate = LocalDate.of(2026, 1, 31)

    @Test
    fun occursOn_returnsFalse_forDatesBeforeStartDate() {
        val transaction = Transaction(
            title = "Gym",
            amount = 50.0,
            type = TransactionType.EXPENSE,
            startDate = startDate,
            recurrence = RecurrenceRule.MONTHLY,
        )

        assertFalse(transaction.occursOn(startDate.minusDays(1)))
    }

    @Test
    fun occursOn_handlesMonthlyRecurrenceOnShortMonths() {
        val transaction = Transaction(
            title = "Rent",
            amount = 1200.0,
            type = TransactionType.EXPENSE,
            startDate = startDate,
            recurrence = RecurrenceRule.MONTHLY,
        )

        assertTrue(transaction.occursOn(LocalDate.of(2026, 1, 31)))
        assertTrue(transaction.occursOn(LocalDate.of(2026, 2, 28)))
        assertFalse(transaction.occursOn(LocalDate.of(2026, 2, 27)))
        assertTrue(transaction.occursOn(LocalDate.of(2026, 3, 31)))
        assertFalse(transaction.occursOn(LocalDate.of(2026, 3, 28)))
    }

    @Test
    fun occursOn_handlesWeeklyRecurrenceAcrossMonthBoundaries() {
        val start = LocalDate.of(2026, 10, 28)
        val transaction = Transaction(
            title = "Weekly Coffee",
            amount = 15.0,
            type = TransactionType.EXPENSE,
            startDate = start,
            recurrence = RecurrenceRule.WEEKLY,
        )

        assertTrue(transaction.occursOn(start))
        assertTrue(transaction.occursOn(start.plusDays(7)))
        assertTrue(transaction.occursOn(start.plusDays(14)))
        assertFalse(transaction.occursOn(start.plusDays(5)))
    }
}
