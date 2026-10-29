package com.rainyday.domain.model

import java.time.LocalDate

data class DailyForecast(
    val date: LocalDate,
    val startingBalance: Double,
    val obligations: Double,
    val incomeReceived: Double,
    val projectedBalance: Double,
    val buffer: Double,
    val daysToNextIncome: Int?,
    val condition: ForecastCondition,
    val transactions: List<Transaction>
)
