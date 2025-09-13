package com.example.gastosapp.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _paymentsByMonth = MutableStateFlow<List<Double>>(emptyList())
    val paymentsByMonth: StateFlow<List<Double>> = _paymentsByMonth.asStateFlow()

    private val _costByMonth = MutableStateFlow<List<Double>>(emptyList())
    val costByMonth: StateFlow<List<Double>> = _costByMonth.asStateFlow()

    init {
        viewModelScope.launch {
            _paymentsByMonth.value = expenseRepository.getPaymentsByMonth()
            _costByMonth.value = expenseRepository.getCostsByMonth()
        }
    }

    fun getPaymentsByMonth() {
        viewModelScope.launch {
            _paymentsByMonth.value = expenseRepository.getPaymentsByMonth()
        }
    }

    fun getCostsByMonth() {
        viewModelScope.launch {
            _costByMonth.value = expenseRepository.getCostsByMonth()
        }
    }


}