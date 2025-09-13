package com.prithvi.sikka.network

import com.prithvi.sikka.entity.ExpenseEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * This class executes network requests and deserializes JSON
 * responses into entities from the com.jetbrains.spacetutorial.entity package.
 * The Ktor HttpClient instance initializes and stores the httpClient property.
 */
class ExpanseApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getAllExpanses(): List<ExpenseEntity> {
        return listOf(
            ExpenseEntity(
                id = 1,
                amount = 100.0,
                categoryId = "food",
                description = "Lunch",
                date = "2024-06-01"
            ),
            ExpenseEntity(
                id = 2,
                amount = 200.0,
                categoryId = "food",
                description = "Lunch",
                date = "2024-06-01"
            ),
            ExpenseEntity(
                id = 3,
                amount = 300.0,
                categoryId = "food",
                description = "Lunch",
                date = "2024-06-01"
            ),

        )
        //httpClient.get("https://api.spacexdata.com/v5/launches").body()
    }
    suspend fun addAllExpanses(): List<ExpenseEntity> {
        return httpClient.get("https://api.spacexdata.com/v5/launches").body()
    }
}