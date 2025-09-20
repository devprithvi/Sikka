package com.prithvi.sikka

import androidx.compose.ui.window.ComposeUIViewController
import com.prithvi.sikka.cache.DatabaseDriverFactory
import com.prithvi.sikka.entity.ExpenseEntity
import com.prithvi.sikka.network.ExpanseApi
import com.prithvi.sikka.viewmodel.ExpenseScreenState
import com.prithvi.sikka.viewmodel.ExpenseViewModel
import com.prithvi.sikka.viewmodel.ExpenseViewModelImpIos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController {
// Create platform-specific database driver factory
    val driverFactory = DatabaseDriverFactory()

    // Create API instance
    val api = ExpanseApi()

    // Create SDK with database and API
    val sdk = SikkaSDK(
        databaseDriverFactory = driverFactory,
        api = api
    )

    // Create ViewModel with real SDK
    val viewModel = ExpenseViewModelImpIos(sdk)
    App(viewModel)
}
