package com.prithvi.sikka

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.prithvi.sikka.cache.DatabaseDriverFactory
import com.prithvi.sikka.entity.ExpenseEntity
import com.prithvi.sikka.network.ExpanseApi
import com.prithvi.sikka.viewmodel.ExpenseScreenState
import com.prithvi.sikka.viewmodel.ExpenseViewModel
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTime::class)
fun main() {
    val scope = CoroutineScope(Dispatchers.Main)

    val sdk = SikkaSDK(databaseDriverFactory = DatabaseDriverFactory())
    // Create a mock ViewModel for WASM demo
    val mockViewModel = object : ExpenseViewModel {
        init {
            loadExpensesI()
        }

        override val stateI = kotlinx.coroutines.flow.MutableStateFlow(
            ExpenseScreenState(
                expenses = listOf(
                    ExpenseEntity(
                        1,
                        250.0,
                        "Food",
                        "Lunch",
                        Clock.System.now().toEpochMilliseconds()
                    ),
                    ExpenseEntity(
                        2,
                        50.0,
                        "Transport",
                        "Bus fare",
                        Clock.System.now().toEpochMilliseconds()
                    )
                )
            )
        )

        override fun loadExpensesI() {
            scope.launch {
                val expenses = sdk.getExpanses()
                stateI.value = stateI.value.copy(expenses = expenses, isLoading = false)
            }
        }

        override fun addExpenseI(amount: Double, categoryId: String, description: String) {
            scope.launch {
                try {
                    val newExpense = ExpenseEntity(
                        id = (stateI.value.expenses.maxOfOrNull { it.id } ?: 0) + 1,
                        amount = amount,
                        categoryId = categoryId,
                        description = description,
                        date = Clock.System.now().toEpochMilliseconds()
                    )
                    sdk.addExpense(newExpense)
                    loadExpensesI() // Refresh list
                    stateI.value = stateI.value.copy(expenses = stateI.value.expenses + newExpense)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }

        override fun updateExpense(expense: ExpenseEntity) {
            scope.launch {
                try {
                    sdk.updateExpense(expense)
                    loadExpensesI() // Refresh list
                } catch (e: Exception) {
                    stateI.value = stateI.value.copy(
                        error = "Failed to update expense: ${e.message}"
                    )
                }
            }
        }

        override fun deleteExpenseI(expenseId: Int) {
            scope.launch {
                try {
                    sdk.deleteAllExpenses()
                    loadExpensesI() // Refresh the list
                    // Optional: Show success message
                } catch (e: Exception) {
                    stateI.value = stateI.value.copy(
                        error = "Failed to delete all expenses: ${e.message}"
                    )
                }
            }
        }

        override fun deleteAllExpensesI() {
            scope.launch {
                try {
                    sdk.deleteAllExpenses()
                    loadExpensesI() // Refresh the list
                    // Optional: Show success message
                } catch (e: Exception) {
                    stateI.value = stateI.value.copy(
                        error = "Failed to delete all expenses: ${e.message}"
                    )
                }
            }
        }

        override fun deleteSelectedExpenses(expenseIds: List<Int>) {
            scope.launch {
                try {
                    sdk.deleteExpenses(expenseIds)
                    loadExpensesI() // Refresh the list
                } catch (e: Exception) {
                    stateI.value = stateI.value.copy(
                        error = "Failed to delete selected expenses: ${e.message}"
                    )
                }
            }
        }
    }
    ComposeViewport(document.body!!) {
        App(viewModel = mockViewModel)
    }
}