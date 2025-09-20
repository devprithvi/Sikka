package com.prithvi.sikka

import com.prithvi.sikka.cache.Database
import com.prithvi.sikka.cache.DatabaseDriverFactory
import com.prithvi.sikka.entity.ExpenseEntity
import com.prithvi.sikka.network.ExpanseApi

/**
 * This class will be the facade for the Database
 * and SpaceXApi classes.
 */
class SikkaSDK(databaseDriverFactory: DatabaseDriverFactory, val api: ExpanseApi? = null) {
    private val database = Database(databaseDriverFactory)

    /**
     *  Kotlin functions called from Swift should be marked with the
     *  @Throws annotation specifying a list of potential exception classes.
     */
    @Throws(Exception::class)
    suspend fun getExpanses(): List<ExpenseEntity> {
        return database.getAllExpanses()
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        database.updateExpense(expense)
    }

    suspend fun addExpense(expense: ExpenseEntity) {
        database.insertExpense(expense)
    }

    /**
     * Delete a single expense by ID
     * @param expenseId The ID of the expense to delete
     * @throws Exception if deletion fails
     */
    @Throws(Exception::class)
    suspend fun deleteExpense(expenseId: Int) {
        try {
            database.deleteExpense(expenseId)
        } catch (e: Exception) {
            throw Exception("Failed to delete expense with ID $expenseId: ${e.message}")
        }
    }

    /**
     * Delete all expenses from database
     * @throws Exception if deletion fails
     */
    @Throws(Exception::class)
    suspend fun deleteAllExpenses() {
        try {
            database.clearAllExpenses()
        } catch (e: Exception) {
            throw Exception("Failed to delete all expenses: ${e.message}")
        }
    }

    /**
     * Check if an expense exists
     * @param expenseId The ID to check
     * @return true if expense exists, false otherwise
     */
    suspend fun expenseExists(expenseId: Int): Boolean {
        return database.getExpenseById(expenseId) != null
    }

    /**
     * Delete multiple expenses (bonus function)
     * @param expenseIds List of expense IDs to delete
     */
    @Throws(Exception::class)
    suspend fun deleteExpenses(expenseIds: List<Int>) {
        try {
            database.deleteExpenses(expenseIds)
        } catch (e: Exception) {
            throw Exception("Failed to delete expenses: ${e.message}")
        }
    }
}