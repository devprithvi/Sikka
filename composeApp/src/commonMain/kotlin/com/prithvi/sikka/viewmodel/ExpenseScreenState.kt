package com.prithvi.sikka.viewmodel

import com.prithvi.sikka.entity.ExpenseEntity

data class ExpenseScreenState(
    val isLoading: Boolean = false,
    val expenses: List<ExpenseEntity> = emptyList(),
    val error: String? = null
)