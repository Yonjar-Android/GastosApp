package com.example.gastosapp.presentation.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import com.example.gastosapp.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PndExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    val expenses: Flow<PagingData<ExpenseWithDetails>> =
        expenseRepository.getAllExpenses()
            .cachedIn(viewModelScope)

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            expenseRepository.update(expense)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            expenseRepository.delete(expense)
        }
    }

}