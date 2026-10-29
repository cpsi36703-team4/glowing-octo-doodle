/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.domain.model

import java.time.LocalDate

/**
 * Data structure holding calculated financial forecast metrics for a specific calendar date.
 *
 * @property date The evaluation date.
 * @property startingBalance Balance at the beginning of the date.
 * @property obligations Total sum of expenses due on this date.
 * @property incomeReceived Total sum of income received on this date.
 * @property projectedBalance End-of-day projected balance.
 * @property buffer Difference between projected balance and comfort floor threshold.
 * @property daysToNextIncome Number of days until the next expected income occurrence.
 * @property condition Weather forecast category derived from balance and buffer metrics.
 * @property transactions List of transactions scheduled for this date.
 */
data class DailyForecast(
    val date: LocalDate,
    val startingBalance: Double,
    val obligations: Double,
    val incomeReceived: Double,
    val projectedBalance: Double,
    val buffer: Double,
    val daysToNextIncome: Int?,
    val condition: ForecastCondition,
    val transactions: List<Transaction>,
)
