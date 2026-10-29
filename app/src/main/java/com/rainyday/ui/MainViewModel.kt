/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rainyday.data.AppDatabase
import com.rainyday.data.TransactionRepository
import com.rainyday.domain.ForecastEngine
import com.rainyday.domain.model.DailyForecast
import com.rainyday.domain.model.RecurrenceRule
import com.rainyday.domain.model.Transaction
import com.rainyday.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Active navigation tab choices.
 */
enum class ActiveTab {
    TIMELINE,
    CALENDAR,
}

/**
 * UI State container holding user preference settings, transactions, forecasts, and dialog visibility.
 */
data class UiState(
    val initialBalance: Double = 1200.00,
    val comfortFloor: Double = 500.00,
    val activeTab: ActiveTab = ActiveTab.TIMELINE,
    val transactions: List<Transaction> = emptyList(),
    val forecasts: List<DailyForecast> = emptyList(),
    val isAddEditOpen: Boolean = false,
    val editingTransaction: Transaction? = null,
)

/**
 * Primary ViewModel managing local repository interaction, state flow, sample data seeding, and forecast recalculation.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)

        viewModelScope.launch {
            repository.allTransactions.collect { transactions ->
                if (transactions.isEmpty()) {
                    seedSampleData()
                } else {
                    recalculateForecasts(transactions)
                }
            }
        }
    }

    private fun recalculateForecasts(transactions: List<Transaction>) {
        val currentState = _uiState.value
        val forecasts = ForecastEngine.calculateForecasts(
            initialBalance = currentState.initialBalance,
            comfortFloor = currentState.comfortFloor,
            transactions = transactions,
            startDate = LocalDate.now(),
            daysAhead = 60,
        )
        _uiState.value = currentState.copy(
            transactions = transactions,
            forecasts = forecasts,
        )
    }

    fun setActiveTab(tab: ActiveTab) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun setInitialBalance(balance: Double) {
        _uiState.value = _uiState.value.copy(initialBalance = balance)
        recalculateForecasts(_uiState.value.transactions)
    }

    fun setComfortFloor(floor: Double) {
        _uiState.value = _uiState.value.copy(comfortFloor = floor)
        recalculateForecasts(_uiState.value.transactions)
    }

    fun openAddTransaction() {
        _uiState.value = _uiState.value.copy(isAddEditOpen = true, editingTransaction = null)
    }

    fun openEditTransaction(transaction: Transaction) {
        _uiState.value = _uiState.value.copy(isAddEditOpen = true, editingTransaction = transaction)
    }

    fun dismissAddEditDialog() {
        _uiState.value = _uiState.value.copy(isAddEditOpen = false, editingTransaction = null)
    }

    fun saveTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        startDate: LocalDate,
        recurrence: RecurrenceRule,
    ) {
        viewModelScope.launch {
            val editing = _uiState.value.editingTransaction
            if (editing != null) {
                repository.update(
                    editing.copy(
                        title = title,
                        amount = amount,
                        type = type,
                        startDate = startDate,
                        recurrence = recurrence,
                    ),
                )
            } else {
                repository.insert(
                    Transaction(
                        title = title,
                        amount = amount,
                        type = type,
                        startDate = startDate,
                        recurrence = recurrence,
                    ),
                )
            }
            dismissAddEditDialog()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    private suspend fun seedSampleData() {
        val today = LocalDate.now()
        val sample = listOf(
            Transaction(
                title = "Paycheck",
                amount = 1800.00,
                type = TransactionType.INCOME,
                startDate = today.plusDays(5),
                recurrence = RecurrenceRule.WEEKLY,
            ),
            Transaction(
                title = "Rent",
                amount = 950.00,
                type = TransactionType.EXPENSE,
                startDate = today.plusDays(2),
                recurrence = RecurrenceRule.MONTHLY,
            ),
            Transaction(
                title = "Electric Bill",
                amount = 110.00,
                type = TransactionType.EXPENSE,
                startDate = today.plusDays(3),
                recurrence = RecurrenceRule.MONTHLY,
            ),
            Transaction(
                title = "Internet",
                amount = 65.00,
                type = TransactionType.EXPENSE,
                startDate = today.plusDays(4),
                recurrence = RecurrenceRule.MONTHLY,
            ),
        )
        sample.forEach { repository.insert(it) }
    }
}
