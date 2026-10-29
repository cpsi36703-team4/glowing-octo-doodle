package com.rainyday.data

import com.rainyday.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun insert(transaction: Transaction): Long {
        return transactionDao.insertTransaction(TransactionEntity.fromDomainModel(transaction))
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.updateTransaction(TransactionEntity.fromDomainModel(transaction))
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(TransactionEntity.fromDomainModel(transaction))
    }

    suspend fun deleteById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }
}
