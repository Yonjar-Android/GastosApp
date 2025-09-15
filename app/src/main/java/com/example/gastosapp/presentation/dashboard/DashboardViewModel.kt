package com.example.gastosapp.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _paymentsByMonth = MutableStateFlow<List<Double>>(emptyList())
    val paymentsByMonth: StateFlow<List<Double>> = _paymentsByMonth.asStateFlow()

    private val _costByMonth = MutableStateFlow<List<Double>>(emptyList())
    val costByMonth: StateFlow<List<Double>> = _costByMonth.asStateFlow()

    private val _availableYears = MutableStateFlow<List<String>>(emptyList())
    val availableYears: StateFlow<List<String>> = _availableYears.asStateFlow()

    init {
        viewModelScope.launch {
            _paymentsByMonth.value = expenseRepository.getPaymentsByMonth()
            _costByMonth.value = expenseRepository.getCostsByMonth()
            _availableYears.value = expenseRepository.getAvailableYears()
        }
    }

    fun getPaymentsByMonth(year: Int = LocalDate.now().year) {
        viewModelScope.launch {
            _paymentsByMonth.value = expenseRepository.getPaymentsByMonth(year)
        }
    }

    fun getCostsByMonth(year: Int = LocalDate.now().year) {
        viewModelScope.launch {
            _costByMonth.value = expenseRepository.getCostsByMonth(year)
        }
    }

    fun getAvailableYears()  {
        viewModelScope.launch {
            _availableYears.value = expenseRepository.getAvailableYears()
        }
    }


}