package com.prithvi.sikka.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class ExpenseEntity @OptIn(ExperimentalTime::class) constructor(
    @SerialName("id")
    val id: Int,
    @SerialName("amount")
    val amount: Double,
    @SerialName("categoryId")
    val categoryId: String,
    @SerialName("description")
    val description: String,
    @SerialName("date")
    val date: String,
    @SerialName("createdAt")
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
) {
    var expanseDate = Instant.parse(date).toLocalDateTime(TimeZone.UTC).date

}


