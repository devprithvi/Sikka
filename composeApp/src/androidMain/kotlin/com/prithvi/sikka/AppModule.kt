package com.prithvi.sikka

import com.prithvi.sikka.cache.DatabaseDriverFactory
import com.prithvi.sikka.network.ExpanseApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provide API instance
    single<ExpanseApi> { ExpanseApi() }

    // Provide DatabaseDriverFactory with Android context
    single<DatabaseDriverFactory> {
        DatabaseDriverFactory(androidContext()) // Pass Android context here
    }

    // Provide SikkaSDK with correct dependencies
    single<SikkaSDK> {
        SikkaSDK(
            databaseDriverFactory = get(), // Use get() to resolve DatabaseDriverFactory
            api = get() // Use get() to resolve ExpanseApi
        )
    }

    // Provide ViewModel
    viewModel<ExpanseViewModelImp> {
        ExpanseViewModelImp(sdk = get()) // Use get() to resolve SikkaSDK
    }
}