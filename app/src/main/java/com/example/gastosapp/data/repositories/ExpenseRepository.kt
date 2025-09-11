package com.example.gastosapp.data.repositories

import com.example.gastosapp.data.database.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun insert(expense: ExpenseEntity): Long
    suspend fun update(expense: ExpenseEntity)
    suspend fun delete(expense: ExpenseEntity)
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    suspend fun getExpensesByClient(clientId: Long): List<ExpenseEntity>
    suspend fun getExpensesByProduct(productId: Long): List<ExpenseEntity>
}