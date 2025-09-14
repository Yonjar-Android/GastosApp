package com.example.gastosapp.presentation.expenses.expenseDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import com.example.gastosapp.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _expense = MutableStateFlow<ExpenseWithDetails?>(null)
    val expense: StateFlow<ExpenseWithDetails?> = _expense

    fun getExpenseById(id: Long){
        viewModelScope.launch {
            _expense.value = expenseRepository.getExpenseById(id)

        }
    }

}