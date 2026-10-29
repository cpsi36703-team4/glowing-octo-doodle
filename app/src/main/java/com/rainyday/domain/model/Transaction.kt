/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.domain.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Indicates whether a transaction represents incoming funds or an outgoing obligation.
 */
enum class TransactionType {
    INCOME,
    EXPENSE,
}

/**
 * Defines the recurrence pattern for scheduled transactions.
 */
enum class RecurrenceRule {
    ONCE,
    WEEKLY,
    MONTHLY,
}

/**
 * Core domain model representing a single scheduled or recurring transaction.
 *
 * @property id Unique identifier for the transaction.
 * @property title Human-readable description (e.g., "Rent", "Salary").
 * @property amount Monetary value of the transaction.
 * @property type Specifies if the transaction is INCOME or EXPENSE.
 * @property startDate The date of initial occurrence.
 * @property recurrence Defines how frequently the transaction repeats.
 */
data class Transaction(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val startDate: LocalDate,
    val recurrence: RecurrenceRule = RecurrenceRule.ONCE,
) {
    /**
     * Determines whether this transaction lands on the specified [targetDate].
     *
     * @param targetDate The date to evaluate for transaction occurrence.
     * @return True if the transaction occurs on [targetDate], false otherwise.
     */
    fun occursOn(targetDate: LocalDate): Boolean {
        // Transactions cannot occur before their scheduled start date.
        if (targetDate < startDate) return false

        return when (recurrence) {
            // One-off transaction occurs exactly on its start date.
            RecurrenceRule.ONCE -> targetDate == startDate

            // Weekly transactions recur every 7 days from the start date.
            RecurrenceRule.WEEKLY -> {
                val daysBetween = ChronoUnit.DAYS.between(startDate, targetDate)
                (daysBetween % 7) == 0L
            }

            // Monthly transactions recur on the same day of the month, adjusting for shorter months.
            RecurrenceRule.MONTHLY -> {
                targetDate.dayOfMonth == startDate.dayOfMonth ||
                    (startDate.dayOfMonth > targetDate.lengthOfMonth() && targetDate.dayOfMonth == targetDate.lengthOfMonth())
            }
        }
    }
}
