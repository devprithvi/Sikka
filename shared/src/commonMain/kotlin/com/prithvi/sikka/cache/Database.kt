package com.prithvi.sikka.cache

import com.prithvi.sikka.entity.ExpenseEntity

/**
 * This class's visibility is set to internal,
 * which means it is only accessible
 * from within the multiplatform module.
 */
internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllExpanses(): List<ExpenseEntity> {
        return dbQuery.selectAllExpenses().executeAsList().map { expense ->
            ExpenseEntity(
                id = expense.id.toInt(),
                amount = expense.amount,
                categoryId = expense.categoryId ?: "",
                description = expense.description,
                date = expense.date,  // ✅ Already Long from database
                createdAt = expense.createdAt
            )
        }
    }
    internal fun updateExpense(expense: ExpenseEntity) {
        dbQuery.updateExpense(
            id = expense.id.toLong(),
            amount = expense.amount,
            categoryId = expense.categoryId,
            description = expense.description,
            date = expense.date,
            createdAt = expense.createdAt
        )
    }

    internal fun deleteExpense(expenseId: Int) {
        dbQuery.deleteExpenseById(expenseId.toLong())
    }

    internal fun clearAllExpenses() {
        dbQuery.removeAllExpenses()
    }

    /**
     * Delete multiple expenses by IDs
     */
    internal fun deleteExpenses(expenseIds: List<Int>) {
        dbQuery.transaction {
            expenseIds.forEach { id ->
                dbQuery.deleteExpenseById(id.toLong())
            }
        }
    }

    /**
     * Check if expense exists (optional - for validation)
     */
    internal fun getExpenseById(expenseId: Int): ExpenseEntity? {
        return try {
            val expense = dbQuery.selectExpenseById(expenseId.toLong()).executeAsOneOrNull()
            expense?.let {
                ExpenseEntity(
                    id = it.id.toInt(),
                    amount = it.amount,
                    categoryId = it.categoryId ?: "",
                    description = it.description,
                    date = it.date,
                    createdAt = it.createdAt
                )
            }
        } catch (e: Exception) {
            null // Return null if expense doesn't exist
        }
    }

    private fun mapAddAllExpanses(
        id: Int,
        amount: Double,
        mDate: Long,
        categoryId: String,
        description: String,
    ): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            amount = amount,
            categoryId = categoryId,
            description = description,
            date = mDate
        )
    }

    internal fun insertExpense(expense: ExpenseEntity) {
        dbQuery.insertExpense(
            amount = expense.amount,
            categoryId = expense.categoryId,
            description = expense.description,
            date = expense.date,
            createdAt = expense.createdAt
        )
    }

    internal fun clearAndCreateExpanses(
        expenses: List<ExpenseEntity>
    ) {
        dbQuery.transaction {
            dbQuery.removeAllExpenses()
            expenses.forEach { expense ->
                dbQuery.insertExpense(
                    amount = expense.amount,
                    date = expense.date.toLong(),
                    categoryId = expense.categoryId,
                    description = expense.description,
                    createdAt = expense.createdAt
                )
            }
        }
    }

}