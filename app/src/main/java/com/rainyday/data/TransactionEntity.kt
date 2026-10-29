/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rainyday.domain.model.RecurrenceRule
import com.rainyday.domain.model.Transaction
import com.rainyday.domain.model.TransactionType
import java.time.LocalDate

/**
 * Room Database Entity representing a stored transaction record in SQLite table "transactions".
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: String,
    val startDate: String,
    val recurrence: String,
) {
    /**
     * Converts this database entity into a clean domain model instance.
     */
    fun toDomainModel(): Transaction {
        return Transaction(
            id = id,
            title = title,
            amount = amount,
            type = TransactionType.valueOf(type),
            startDate = LocalDate.parse(startDate),
            recurrence = RecurrenceRule.valueOf(recurrence),
        )
    }

    companion object {
        /**
         * Creates a Room entity instance from a domain model instance.
         */
        fun fromDomainModel(domain: Transaction): TransactionEntity {
            return TransactionEntity(
                id = domain.id,
                title = domain.title,
                amount = domain.amount,
                type = domain.type.name,
                startDate = domain.startDate.toString(),
                recurrence = domain.recurrence.name,
            )
        }
    }
}
