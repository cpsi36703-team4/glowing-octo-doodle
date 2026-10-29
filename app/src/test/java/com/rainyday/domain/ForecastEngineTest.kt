package com.rainyday.domain

import com.rainyday.domain.model.ForecastCondition
import com.rainyday.domain.model.RecurrenceRule
import com.rainyday.domain.model.Transaction
import com.rainyday.domain.model.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ForecastEngineTest {

    private val startDate = LocalDate.of(2026, 10, 1)

    @Test
    fun calculateForecasts_clearConditionWhenBufferIsStrong() {
        val transactions = listOf(
            Transaction(
                title = "Paycheck",
                amount = 2000.0,
                type = TransactionType.INCOME,
                startDate = startDate,
                recurrence = RecurrenceRule.MONTHLY
            )
        )

        val forecasts = ForecastEngine.calculateForecasts(
            initialBalance = 1500.0,
            comfortFloor = 500.0,
            transactions = transactions,
            startDate = startDate,
            daysAhead = 7
        )

        assertEquals(7, forecasts.size)
        val day1 = forecasts[0]
        assertEquals(3500.0, day1.projectedBalance, 0.01)
        assertEquals(3000.0, day1.buffer, 0.01)
        assertEquals(ForecastCondition.CLEAR, day1.condition)
    }

    @Test
    fun calculateForecasts_stormConditionWhenBalanceNegative() {
        val transactions = listOf(
            Transaction(
                title = "Heavy Rent",
                amount = 1200.0,
                type = TransactionType.EXPENSE,
                startDate = startDate,
                recurrence = RecurrenceRule.ONCE
            )
        )

        val forecasts = ForecastEngine.calculateForecasts(
            initialBalance = 500.0,
            comfortFloor = 300.0,
            transactions = transactions,
            startDate = startDate,
            daysAhead = 3
        )

        val day1 = forecasts[0]
        assertEquals(-700.0, day1.projectedBalance, 0.01)
        assertEquals(ForecastCondition.STORM, day1.condition)
    }

    @Test
    fun calculateForecasts_rainConditionWhenBelowComfortFloor() {
        val transactions = listOf(
            Transaction(
                title = "Bill",
                amount = 300.0,
                type = TransactionType.EXPENSE,
                startDate = startDate,
                recurrence = RecurrenceRule.ONCE
            )
        )

        val forecasts = ForecastEngine.calculateForecasts(
            initialBalance = 600.0,
            comfortFloor = 500.0,
            transactions = transactions,
            startDate = startDate,
            daysAhead = 3
        )

        val day1 = forecasts[0]
        assertEquals(300.0, day1.projectedBalance, 0.01)
        assertEquals(-200.0, day1.buffer, 0.01)
        assertEquals(ForecastCondition.RAIN, day1.condition)
    }

    @Test
    fun transaction_occursOnWeeklyAndMonthlyRecurrence() {
        val weeklyTx = Transaction(
            title = "Weekly Sub",
            amount = 10.0,
            type = TransactionType.EXPENSE,
            startDate = startDate,
            recurrence = RecurrenceRule.WEEKLY
        )

        assertTrue(weeklyTx.occursOn(startDate))
        assertTrue(weeklyTx.occursOn(startDate.plusDays(7)))
        assertTrue(weeklyTx.occursOn(startDate.plusDays(14)))

        val monthlyTx = Transaction(
            title = "Monthly Sub",
            amount = 50.0,
            type = TransactionType.EXPENSE,
            startDate = startDate,
            recurrence = RecurrenceRule.MONTHLY
        )

        assertTrue(monthlyTx.occursOn(startDate))
        assertTrue(monthlyTx.occursOn(startDate.plusMonths(1)))
    }

    @Test
    fun calculateDaysToNextIncome_returnsCorrectProximity() {
        val payDay = startDate.plusDays(4)
        val transactions = listOf(
            Transaction(
                title = "Salary",
                amount = 1000.0,
                type = TransactionType.INCOME,
                startDate = payDay,
                recurrence = RecurrenceRule.ONCE
            )
        )

        val forecasts = ForecastEngine.calculateForecasts(
            initialBalance = 500.0,
            comfortFloor = 200.0,
            transactions = transactions,
            startDate = startDate,
            daysAhead = 5
        )

        assertEquals(4, forecasts[0].daysToNextIncome)
        assertEquals(0, forecasts[4].daysToNextIncome)
    }
}
