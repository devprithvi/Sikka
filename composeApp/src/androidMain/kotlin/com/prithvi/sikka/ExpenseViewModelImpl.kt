package com.prithvi.sikka

import com.prithvi.sikka.viewmodel.ExpenseScreenState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prithvi.sikka.entity.ExpenseEntity
import com.prithvi.sikka.viewmodel.ExpenseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ExpanseViewModelImp(private val sdk: SikkaSDK) : ViewModel(), ExpenseViewModel {

    private val _state = MutableStateFlow(ExpenseScreenState())
    override val stateI: StateFlow<ExpenseScreenState> = _state

    init {
        loadExpensesI()
    }

    override fun loadExpensesI() {
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


    override fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            try {
                sdk.updateExpense(expense)
                loadExpensesI() // Refresh list
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
    override fun deleteExpenseI(expenseId: Int) {
        viewModelScope.launch {
            try {
                sdk.deleteExpense(expenseId)
                loadExpensesI() // Refresh the list
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
    override fun deleteAllExpensesI() {
        viewModelScope.launch {
            try {
                sdk.deleteAllExpenses()
                loadExpensesI() // Refresh the list
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
    override fun deleteSelectedExpenses(expenseIds: List<Int>) {
        viewModelScope.launch {
            try {
                sdk.deleteExpenses(expenseIds)
                loadExpensesI() // Refresh the list
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete selected expenses: ${e.message}"
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun addExpenseI(amount: Double, categoryId: String, description: String) {
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
                loadExpensesI() // Refresh list
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


