package com.example.gastosapp.presentation.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.data.repositories.ClientRepository
import com.example.gastosapp.data.repositories.ExpenseRepository
import com.example.gastosapp.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    clientRepository: ClientRepository,
    productRepository: ProductRepository
): ViewModel() {

    val clients: StateFlow<List<ClientEntity>>
    = clientRepository.getAllClients().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val products: StateFlow<List<ProductEntity>>
    = productRepository.getAllProducts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun insertExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            expenseRepository.insert(expense)
        }
    }
}