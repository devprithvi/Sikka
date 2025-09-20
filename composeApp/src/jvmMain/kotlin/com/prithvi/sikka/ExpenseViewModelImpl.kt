package com.prithvi.sikka

import androidx.lifecycle.viewModelScope
import com.prithvi.sikka.entity.ExpenseEntity
import com.prithvi.sikka.viewmodel.ExpenseScreenState
import com.prithvi.sikka.viewmodel.ExpenseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ExpenseViewModelImpl(private val sdk: SikkaSDK) : ExpenseViewModel {
    private val _stateI = MutableStateFlow(ExpenseScreenState())
    override val stateI: StateFlow<ExpenseScreenState> get() = _stateI

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        loadExpensesI()
    }

    override fun loadExpensesI() {
        scope.launch {
            val expenses = sdk.getExpanses()
            _stateI.value = _stateI.value.copy(expenses = expenses, isLoading = false)
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun addExpenseI(
        amount: Double,
        categoryId: String,
        description: String
    ) {
        scope.launch {
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

    override fun deleteExpenseI(expenseId: Int) {
        scope.launch {
            try {
                sdk.deleteAllExpenses()
                loadExpensesI() // Refresh the list
                // Optional: Show success message
            } catch (e: Exception) {
                _stateI.value = _stateI.value.copy(
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
                _stateI.value = _stateI.value.copy(
                    error = "Failed to delete all expenses: ${e.message}"
                )
            }
        }
    }

    override fun updateExpense(expense: ExpenseEntity) {
        scope.launch {
            try {
                sdk.updateExpense(expense)
                loadExpensesI() // Refresh list
            } catch (e: Exception) {
                _stateI.value = _stateI.value.copy(
                    error = "Failed to update expense: ${e.message}"
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
                _stateI.value = _stateI.value.copy(
                    error = "Failed to delete selected expenses: ${e.message}"
                )
            }
        }
    }
}