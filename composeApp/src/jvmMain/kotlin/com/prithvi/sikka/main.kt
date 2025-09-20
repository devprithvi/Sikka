package com.prithvi.sikka

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.prithvi.sikka.viewmodel.ExpenseViewModel
import com.prithvi.sikka.cache.DatabaseDriverFactory
import com.prithvi.sikka.network.ExpanseApi

fun main() = application {

    val driverFactory = DatabaseDriverFactory()
    val api = ExpanseApi()
    val sdk = SikkaSDK(driverFactory, api)
    val expenseViewModel = ExpenseViewModelImpl(sdk)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Sikka",
    ) {
        App(expenseViewModel)
    }
}

