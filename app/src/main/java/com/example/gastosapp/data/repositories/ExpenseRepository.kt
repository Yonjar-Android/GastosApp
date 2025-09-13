package com.example.gastosapp.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    suspend fun insert(expense: ExpenseEntity): Long
    suspend fun update(expense: ExpenseEntity)
    suspend fun delete(expense: ExpenseEntity)
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    fun getAllExpenses(): Flow<List<ExpenseWithDetails>>
    suspend fun getExpensesByClient(clientId: Long): List<ExpenseEntity>
    suspend fun getExpensesByProduct(productId: Long): List<ExpenseEntity>

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPaymentsByMonth(year: Int = LocalDate.now().year): List<Double>

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCostsByMonth(year: Int = LocalDate.now().year): List<Double>
}