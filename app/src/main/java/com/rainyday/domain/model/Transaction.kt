package com.rainyday.domain.model

import java.time.LocalDate

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class RecurrenceRule {
    ONCE,
    WEEKLY,
    MONTHLY
}

data class Transaction(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val startDate: LocalDate,
    val recurrence: RecurrenceRule = RecurrenceRule.ONCE,
) {
    /**
     * Checks if this transaction occurs on [targetDate].
     */
    fun occursOn(targetDate: LocalDate): Boolean {
        if (targetDate < startDate) return false
        return when (recurrence) {
            RecurrenceRule.ONCE -> targetDate == startDate
            RecurrenceRule.WEEKLY -> {
                val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, targetDate)
                (daysBetween % 7) == 0L
            }
            RecurrenceRule.MONTHLY -> {
                targetDate.dayOfMonth == startDate.dayOfMonth ||
                    (startDate.dayOfMonth > targetDate.lengthOfMonth() && targetDate.dayOfMonth == targetDate.lengthOfMonth())
            }
        }
    }
}
