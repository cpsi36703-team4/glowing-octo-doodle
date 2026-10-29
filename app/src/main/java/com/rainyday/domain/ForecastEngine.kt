/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.domain

import com.rainyday.domain.model.DailyForecast
import com.rainyday.domain.model.ForecastCondition
import com.rainyday.domain.model.Transaction
import com.rainyday.domain.model.TransactionType
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Transparent calculation engine for computing daily financial weather forecasts.
 * Evaluates projected balances, obligations, buffer thresholds, and condition categories.
 */
object ForecastEngine {

    /**
     * Calculates daily forecasts starting from [startDate] for a target timeframe of [daysAhead].
     *
     * @param initialBalance Starting cash balance.
     * @param comfortFloor Safety floor threshold chosen by the user.
     * @param transactions List of active transactions to evaluate.
     * @param startDate Beginning date for forecast timeline evaluation (defaults to today).
     * @param daysAhead Total number of days to project forward (defaults to 30).
     * @return List of calculated [DailyForecast] instances for each projected date.
     */
    fun calculateForecasts(
        initialBalance: Double,
        comfortFloor: Double,
        transactions: List<Transaction>,
        startDate: LocalDate = LocalDate.now(),
        daysAhead: Int = 30,
    ): List<DailyForecast> {
        val forecasts = ArrayList<DailyForecast>(daysAhead)
        var currentBalance = initialBalance

        // Pre-filter income transactions for fast proximity checks during iteration.
        val incomeTransactions = transactions.filter { it.type == TransactionType.INCOME }

        for (dayOffset in 0 until daysAhead) {
            val date = startDate.plusDays(dayOffset.toLong())
            val dayTransactions = transactions.filter { it.occursOn(date) }

            // Compute total income and expenses landing on this date in a single pass.
            var obligations = 0.0
            var incomeReceived = 0.0
            for (tx in dayTransactions) {
                if (tx.type == TransactionType.EXPENSE) {
                    obligations += tx.amount
                } else if (tx.type == TransactionType.INCOME) {
                    incomeReceived += tx.amount
                }
            }

            // Calculate projected balance and comfort buffer.
            val projectedBalance = (currentBalance + incomeReceived) - obligations
            val buffer = projectedBalance - comfortFloor

            // Calculate days until next expected income arrival.
            val daysToNextIncome = calculateDaysToNextIncome(date, incomeTransactions)

            // Map metrics to financial weather condition.
            val condition = evaluateCondition(projectedBalance, buffer, obligations, comfortFloor)

            forecasts.add(
                DailyForecast(
                    date = date,
                    startingBalance = currentBalance,
                    obligations = obligations,
                    incomeReceived = incomeReceived,
                    projectedBalance = projectedBalance,
                    buffer = buffer,
                    daysToNextIncome = daysToNextIncome,
                    condition = condition,
                    transactions = dayTransactions,
                )
            )

            currentBalance = projectedBalance
        }

        return forecasts
    }

    /**
     * Categorizes projected balance and buffer into a [ForecastCondition] weather status.
     */
    private fun evaluateCondition(
        projectedBalance: Double,
        buffer: Double,
        obligations: Double,
        comfortFloor: Double,
    ): ForecastCondition {
        return when {
            // Negative balance represents severe financial deficit.
            projectedBalance < 0 -> ForecastCondition.STORM

            // Balance dipping below comfort floor indicates rain condition.
            buffer < 0 -> ForecastCondition.RAIN

            // Approaching comfort floor or large pending obligations indicates cloudy condition.
            buffer < (comfortFloor * 0.25) || (obligations > 0 && obligations >= buffer) -> ForecastCondition.CLOUDY

            // Healthy balance slightly above comfort floor indicates fair condition.
            buffer < comfortFloor -> ForecastCondition.FAIR

            // Strong cash buffer well above comfort floor represents clear condition.
            else -> ForecastCondition.CLEAR
        }
    }

    /**
     * Finds the number of days until the next scheduled income occurrence on or after [currentDate].
     */
    private fun calculateDaysToNextIncome(currentDate: LocalDate, incomeTransactions: List<Transaction>): Int? {
        if (incomeTransactions.isEmpty()) return null
        var minDays: Int? = null

        for (tx in incomeTransactions) {
            var checkDate = currentDate
            val limitDate = currentDate.plusDays(90)
            while (checkDate <= limitDate) {
                if (tx.occursOn(checkDate)) {
                    val days = ChronoUnit.DAYS.between(currentDate, checkDate).toInt()
                    if (minDays == null || days < minDays) {
                        minDays = days
                    }
                    break
                }
                checkDate = checkDate.plusDays(1)
            }
        }
        return minDays
    }
}
