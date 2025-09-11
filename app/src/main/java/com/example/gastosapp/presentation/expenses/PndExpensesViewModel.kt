package com.example.gastosapp.presentation.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PndExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    val expenses: StateFlow<List<ExpenseEntity>> =
        expenseRepository.getAllExpenses()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


}