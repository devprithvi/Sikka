package com.prithvi.sikka.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory actual constructor(private val context: Any) {
    actual fun createDriver(): SqlDriver {
        val databasePath = createDatabasePath()
        val driver = JdbcSqliteDriver("jdbc:sqlite:$databasePath")

        // Initialize schema if database is empty
        try {
            AppDatabase.Schema.create(driver)
        } catch (e: Exception) {
            // Schema already exists, continue
        }

        return driver
    }

    private fun createDatabasePath(): String {
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            System.getProperty("os.name").lowercase().contains("win") -> {
                File(System.getenv("APPDATA") ?: userHome, "Sikka")
            }
            System.getProperty("os.name").lowercase().contains("mac") -> {
                File(userHome, "Library/Application Support/Sikka")
            }
            else -> {
                File(userHome, ".local/share/Sikka")
            }
        }

        appDataDir.mkdirs()
        return File(appDataDir, "sikka.db").absolutePath
    }
}
