package com.prithvi.sikka.viewmodel

import com.prithvi.sikka.entity.ExpenseEntity
import kotlinx.coroutines.flow.StateFlow

interface ExpenseViewModel {
    val stateI: StateFlow<ExpenseScreenState>
    fun loadExpensesI()
    fun addExpenseI(amount: Double, categoryId: String, description: String)
    fun deleteExpenseI(expenseId: Int)
    fun deleteAllExpensesI()
    fun updateExpense(expense: ExpenseEntity)
    fun deleteSelectedExpenses(expenseIds: List<Int>)
}