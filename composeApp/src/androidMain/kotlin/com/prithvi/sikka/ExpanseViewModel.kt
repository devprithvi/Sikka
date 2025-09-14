package com.prithvi.sikka

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prithvi.sikka.entity.ExpenseEntity
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ExpanseViewModel(private val sdk: SikkaSDK) : ViewModel() {
    private val _state = mutableStateOf(ExpenseScreenState())
    val state: State<ExpenseScreenState> = _state

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val expenses = sdk.getExpanses()  // ✅ Direct database call
                _state.value = _state.value.copy(
                    isLoading = false,
                    expenses = expenses
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    expenses = emptyList(),
                    error = e.message
                )
            }
        }
    }
    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            try {
                sdk.updateExpense(expense)
                loadExpenses() // Refresh list
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to update expense: ${e.message}"
                )
            }
        }
    }

    /**
     * Delete a single expense
     */
    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch {
            try {
                sdk.deleteExpense(expenseId)
                loadExpenses() // Refresh the list
                // Optional: Show success message
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete expense: ${e.message}"
                )
            }
        }
    }

    /**
     * Delete all expenses (with confirmation)
     */
    fun deleteAllExpenses() {
        viewModelScope.launch {
            try {
                sdk.deleteAllExpenses()
                loadExpenses() // Refresh the list
                // Optional: Show success message
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete all expenses: ${e.message}"
                )
            }
        }
    }

    /**
     * Delete multiple expenses
     */
    fun deleteSelectedExpenses(expenseIds: List<Int>) {
        viewModelScope.launch {
            try {
                sdk.deleteExpenses(expenseIds)
                loadExpenses() // Refresh the list
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete selected expenses: ${e.message}"
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun addExpense(amount: Double, categoryId: String, description: String) {
        viewModelScope.launch {
            try {
                val expense = ExpenseEntity(
                    id = 0,
                    amount = amount,
                    categoryId = categoryId,
                    description = description,
                    date = Clock.System.now().toEpochMilliseconds(),
                )
                sdk.addExpense(expense)
                loadExpenses() // Refresh list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Clear any error messages
     */
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}



data class ExpenseScreenState(
    val isLoading: Boolean = false,
    val expenses: List<ExpenseEntity> = emptyList(),
    val error: String? = null
)