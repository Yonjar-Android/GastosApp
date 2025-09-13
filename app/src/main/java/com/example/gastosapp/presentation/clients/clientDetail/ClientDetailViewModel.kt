package com.example.gastosapp.presentation.clients.clientDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import com.example.gastosapp.data.repositories.ClientRepository
import com.example.gastosapp.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ClientDetailViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {


    private val _expenses = MutableStateFlow<List<ExpenseWithDetails>>(emptyList())
    val expenses: StateFlow<List<ExpenseWithDetails>> = _expenses

    fun getExpensesByClient(clientId: Long) {
        viewModelScope.launch {
            expenseRepository.getExpensesByClientId(clientId)
                .collect { expensesList ->
                    _expenses.value = expensesList
                }
        }
    }

}