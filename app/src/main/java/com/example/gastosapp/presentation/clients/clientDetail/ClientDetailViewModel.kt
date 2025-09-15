package com.example.gastosapp.presentation.clients.clientDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import com.example.gastosapp.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@HiltViewModel
class ClientDetailViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _clientId = MutableStateFlow<Long?>(null)

    // cuando el clientId cambia, se dispara un nuevo flujo de PagingData
    @OptIn(ExperimentalCoroutinesApi::class)
    val expenses: Flow<PagingData<ExpenseWithDetails>> =
        _clientId
            .filterNotNull()
            .flatMapLatest { id ->
                expenseRepository.getExpensesByClientId(id)
            }
            .cachedIn(viewModelScope)

    fun setClientId(clientId: Long) {
        _clientId.value = clientId
    }

}