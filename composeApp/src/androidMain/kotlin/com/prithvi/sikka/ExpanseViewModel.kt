package com.prithvi.sikka

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.prithvi.sikka.entity.ExpenseEntity
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExpanseViewModel(private val sdk: SikkaSDK) : ViewModel() {
    private val _state = mutableStateOf(ExpanseScreenState())
    val state: State<ExpanseScreenState> = _state

    init {
        loadExpanses()
    }

    fun loadExpanses() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, expanses = emptyList())
            try {
                val expanses = sdk.getExpanses(forceReload = true)
                _state.value = _state.value.copy(isLoading = false, expanses = expanses)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, expanses = emptyList())
            }
        }
    }
}

data class ExpanseScreenState(
    val isLoading: Boolean = false,
    val expanses: List<ExpenseEntity> = emptyList()
)