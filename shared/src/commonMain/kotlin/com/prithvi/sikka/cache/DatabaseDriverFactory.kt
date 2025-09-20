package com.prithvi.sikka.cache

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory(context: Any) {
    fun createDriver(): SqlDriver
}
