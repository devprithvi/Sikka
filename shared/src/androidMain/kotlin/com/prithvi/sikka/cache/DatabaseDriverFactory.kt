package com.prithvi.sikka.cache

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory actual constructor(private val context: Any) {
    actual fun createDriver(): SqlDriver {
        // Cast Any to Context for Android
        val androidContext = context as Context
        return AndroidSqliteDriver(AppDatabase.Schema, androidContext, "sikka.db")
    }
}
