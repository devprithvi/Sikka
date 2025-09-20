package com.prithvi.sikka

import app.cash.sqldelight.db.SqlDriver
import com.prithvi.sikka.cache.AppDatabase

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

