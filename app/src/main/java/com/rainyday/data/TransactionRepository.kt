/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.data

import com.rainyday.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository layer exposing transaction data streams and database mutation operations to ViewModels.
 */
class TransactionRepository(private val transactionDao: TransactionDao) {

    /**
     * Reactive stream emitting the full list of domain [Transaction] items sorted by start date.
     */
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions().map { entities ->
        entities.map { it.toDomainModel() }
    }

    /**
     * Inserts a new domain transaction into local database storage.
     */
    suspend fun insert(transaction: Transaction): Long {
        return transactionDao.insertTransaction(TransactionEntity.fromDomainModel(transaction))
    }

    /**
     * Updates an existing domain transaction in local database storage.
     */
    suspend fun update(transaction: Transaction) {
        transactionDao.updateTransaction(TransactionEntity.fromDomainModel(transaction))
    }

    /**
     * Deletes a domain transaction from local database storage.
     */
    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(TransactionEntity.fromDomainModel(transaction))
    }

    /**
     * Deletes a transaction by its primary key ID.
     */
    suspend fun deleteById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }
}
