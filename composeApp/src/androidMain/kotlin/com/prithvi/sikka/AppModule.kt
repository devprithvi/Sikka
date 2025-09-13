package com.prithvi.sikka

import com.prithvi.sikka.cache.AndroidDatabaseDriverFactory
import com.prithvi.sikka.network.ExpanseApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ExpanseApi> { ExpanseApi() }
    single<SikkaSDK> {
        SikkaSDK(
            databaseDriverFactory = AndroidDatabaseDriverFactory(
                androidContext()
            ), api = get()
        )
    }
    viewModel { ExpanseViewModel(sdk = get()) }
}