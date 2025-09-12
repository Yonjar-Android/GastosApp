package com.example.gastosapp.data.repositories

import com.example.gastosapp.data.database.dao.ExpenseDao
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class ExpenseRepositoryImp @Inject constructor(
    private val expenseDao: ExpenseDao
): ExpenseRepository {
    override suspend fun insert(expense: ExpenseEntity): Long {
        return expenseDao.insert(expense)
    }

    override suspend fun update(expense: ExpenseEntity) {
        expenseDao.update(expense)
    }

    override suspend fun delete(expense: ExpenseEntity) {
        expenseDao.delete(expense)
    }

    override suspend fun getExpenseById(id: Long): ExpenseEntity? {
        return expenseDao.getExpenseById(id)
    }

    override fun getAllExpenses(): Flow<List<ExpenseWithDetails>> {
        return expenseDao.getAllExpensesWithDetails()
    }

    override suspend fun getExpensesByClient(clientId: Long): List<ExpenseEntity> {
        return emptyList()
    }

    override suspend fun getExpensesByProduct(productId: Long): List<ExpenseEntity> {
        return emptyList()
    }

}