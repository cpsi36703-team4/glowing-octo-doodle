package com.rainyday.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rainyday.domain.model.RecurrenceRule
import com.rainyday.domain.model.Transaction
import com.rainyday.domain.model.TransactionType
import com.rainyday.ui.screens.AddEditTransactionDialog
import com.rainyday.ui.screens.CalendarScreen
import com.rainyday.ui.screens.TimelineScreen
import com.rainyday.ui.theme.RainyDayTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RainyDayTheme {
                val uiState by viewModel.uiState.collectAsState()
                MainAppScreen(
                    uiState = uiState,
                    onTabSelected = viewModel::setActiveTab,
                    onAddClick = viewModel::openAddTransaction,
                    onEditTransaction = viewModel::openEditTransaction,
                    onDeleteTransaction = viewModel::deleteTransaction,
                    onDismissDialog = viewModel::dismissAddEditDialog,
                    onSaveTransaction = viewModel::saveTransaction
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    uiState: UiState,
    onTabSelected: (ActiveTab) -> Unit,
    onAddClick: () -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    onDismissDialog: () -> Unit,
    onSaveTransaction: (title: String, amount: Double, type: TransactionType, startDate: LocalDate, recurrence: RecurrenceRule) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rainy Day",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = uiState.activeTab == ActiveTab.TIMELINE,
                    onClick = { onTabSelected(ActiveTab.TIMELINE) },
                    icon = { Icon(Icons.Default.ViewList, contentDescription = "Timeline") },
                    label = { Text("Timeline") }
                )
                NavigationBarItem(
                    selected = uiState.activeTab == ActiveTab.CALENDAR,
                    onClick = { onTabSelected(ActiveTab.CALENDAR) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar") },
                    label = { Text("Calendar") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (uiState.activeTab) {
                ActiveTab.TIMELINE -> TimelineScreen(
                    uiState = uiState,
                    onEditTransaction = onEditTransaction,
                    onDeleteTransaction = onDeleteTransaction
                )
                ActiveTab.CALENDAR -> CalendarScreen(
                    uiState = uiState
                )
            }
        }

        if (uiState.isAddEditOpen) {
            AddEditTransactionDialog(
                transaction = uiState.editingTransaction,
                onDismiss = onDismissDialog,
                onSave = onSaveTransaction
            )
        }
    }
}
