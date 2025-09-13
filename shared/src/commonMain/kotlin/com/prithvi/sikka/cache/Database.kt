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
        return dbQuery.selectAllExpenses().executeAsList().map {
            mapAddAllExpanses(
                id = it.id.toInt(),
                amount = it.amount,
                mDate = it.date.toString(),
                categoryId = it.categoryId.toString(),
                description = it.description,
            )
        }
    }
    private fun mapAddAllExpanses(
        id: Int,
        amount: Double,
        mDate: String,
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