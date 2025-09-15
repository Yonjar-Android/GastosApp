package com.example.gastosapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    override suspend fun getExpenseById(id: Long): ExpenseWithDetails? {
        return expenseDao.getExpenseById(id)
    }

    override fun getAllExpenses(): Flow<PagingData<ExpenseWithDetails>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 20),
            pagingSourceFactory = { expenseDao.getAllExpensesWithDetails() }
        ).flow
    }

    override fun getExpensesByClientId(clientId: Long): Flow<PagingData<ExpenseWithDetails>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 20),
            pagingSourceFactory = { expenseDao.getExpensesByClientId(clientId) }
        ).flow
    }

    override suspend fun getPaymentsByMonth(year: Int): List<Double> {
        val rawResults = expenseDao.getPaymentsByMonth(year.toString())
        val totalsByMonth = rawResults.associate { it.month.toInt() to (it.total ?: 0.0) }

        return (1..12).map { month ->
            totalsByMonth[month] ?: 0.0
        }
    }

    override suspend fun getAvailableYears(): List<String> {
        return expenseDao.getAvailableYears()
    }

    override suspend fun getCostsByMonth(year: Int): List<Double> {
        val rawResults = expenseDao.getPaymentsByMonth(year.toString())
        val totalsByMonth = rawResults.associate { it.month.toInt() to (it.totalCost ?: 0.0) }

        return (1..12).map { month ->
            totalsByMonth[month] ?: 0.0
        }
    }


}