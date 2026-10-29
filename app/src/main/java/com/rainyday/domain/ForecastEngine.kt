package com.rainyday.domain

import com.rainyday.domain.model.DailyForecast
import com.rainyday.domain.model.ForecastCondition
import com.rainyday.domain.model.Transaction
import com.rainyday.domain.model.TransactionType
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object ForecastEngine {

    fun calculateForecasts(
        initialBalance: Double,
        comfortFloor: Double,
        transactions: List<Transaction>,
        startDate: LocalDate = LocalDate.now(),
        daysAhead: Int = 30,
    ): List<DailyForecast> {
        val forecasts = ArrayList<DailyForecast>(daysAhead)
        var currentBalance = initialBalance

        val incomeTransactions = transactions.filter { it.type == TransactionType.INCOME }

        for (dayOffset in 0 until daysAhead) {
            val date = startDate.plusDays(dayOffset.toLong())
            val dayTransactions = transactions.filter { it.occursOn(date) }

            var obligations = 0.0
            var incomeReceived = 0.0
            for (tx in dayTransactions) {
                if (tx.type == TransactionType.EXPENSE) {
                    obligations += tx.amount
                } else if (tx.type == TransactionType.INCOME) {
                    incomeReceived += tx.amount
                }
            }

            val projectedBalance = (currentBalance + incomeReceived) - obligations
            val buffer = projectedBalance - comfortFloor

            val daysToNextIncome = calculateDaysToNextIncome(date, incomeTransactions)

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

    private fun evaluateCondition(
        projectedBalance: Double,
        buffer: Double,
        obligations: Double,
        comfortFloor: Double
    ): ForecastCondition {
        return when {
            projectedBalance < 0 -> ForecastCondition.STORM
            buffer < 0 -> ForecastCondition.RAIN
            buffer < (comfortFloor * 0.25) || (obligations > 0 && obligations >= buffer) -> ForecastCondition.CLOUDY
            buffer < comfortFloor -> ForecastCondition.FAIR
            else -> ForecastCondition.CLEAR
        }
    }

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
