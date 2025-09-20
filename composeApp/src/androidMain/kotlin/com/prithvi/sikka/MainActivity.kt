package com.prithvi.sikka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.prithvi.sikka.entity.ExpenseEntity
import com.prithvi.sikka.viewmodel.ExpenseScreenState
import com.prithvi.sikka.viewmodel.ExpenseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: ExpanseViewModelImp by viewModel()
            App(viewModel = viewModel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val fakeViewModel = object : ExpenseViewModel {
        override val stateI = MutableStateFlow(
            ExpenseScreenState(
                expenses = listOf(
                    // Add sample ExpenseEntity objects here for preview
                )
            )
        )
        override fun loadExpensesI() {}
        override fun deleteExpenseI(expenseId: Int) {}
        override fun deleteAllExpensesI() {}
        override fun updateExpense(expense: ExpenseEntity) {}

        override fun deleteSelectedExpenses(expenseIds: List<Int>) {}
        override fun addExpenseI(amount: Double, categoryId: String, description: String) {}
    }
    App(viewModel = fakeViewModel)
}