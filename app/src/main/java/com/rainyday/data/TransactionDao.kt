/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for executing Room database queries against the "transactions" table.
 */
@Dao
interface TransactionDao {
    /**
     * Retrieves all stored transaction records ordered ascending by start date.
     * Emits a new list via reactive [Flow] whenever the database updates.
     */
    @Query("SELECT * FROM transactions ORDER BY startDate ASC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    /**
     * Inserts a new transaction record or replaces an existing record on primary key conflict.
     * @return Row ID of the inserted record.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    /**
     * Updates an existing transaction record.
     */
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    /**
     * Deletes a specific transaction record.
     */
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    /**
     * Deletes a transaction record matching the specified primary key ID.
     */
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}
