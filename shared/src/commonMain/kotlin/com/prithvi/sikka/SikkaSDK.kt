package com.prithvi.sikka

import com.prithvi.sikka.cache.Database
import com.prithvi.sikka.cache.DatabaseDriverFactory
import com.prithvi.sikka.entity.ExpenseEntity
import com.prithvi.sikka.network.ExpanseApi

/**
 * This class will be the facade for the Database
 * and SpaceXApi classes.
 */
class SikkaSDK(databaseDriverFactory: DatabaseDriverFactory, val api: ExpanseApi) {
    private val database = Database(databaseDriverFactory)

    /**
     *  Kotlin functions called from Swift should be marked with the
     *  @Throws annotation specifying a list of potential exception classes.
     */
    @Throws(Exception::class)
    suspend fun getExpanses(forceReload: Boolean): List<ExpenseEntity> {
        val cachedExpanses = database.getAllExpanses()
        return if (cachedExpanses.isNotEmpty() && !forceReload) {
            cachedExpanses
        } else {
            api.getAllExpanses().also {
                database.clearAndCreateExpanses(it)
            }
        }
    }
}