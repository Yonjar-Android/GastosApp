package com.example.gastosapp.data.repositories

import com.example.gastosapp.data.database.entities.ExpenseEntity

interface ExpenseRepository {
    suspend fun insert(expense: ExpenseEntity): Long
    suspend fun update(expense: ExpenseEntity)
    suspend fun delete(expense: ExpenseEntity)
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    suspend fun getAllExpenses(): List<ExpenseEntity>
    suspend fun getExpensesByClient(clientId: Long): List<ExpenseEntity>
    suspend fun getExpensesByProduct(productId: Long): List<ExpenseEntity>
}