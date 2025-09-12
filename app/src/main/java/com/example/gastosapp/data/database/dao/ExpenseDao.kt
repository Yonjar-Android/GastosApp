package com.example.gastosapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Query("""
    SELECT e.id, e.description, e.cost, e.payment, e.date, 
           e.clientId, e.productId,
           c.firstName AS clientFirstName, 
           c.lastName AS clientLastName, 
           p.productName AS productName
    FROM expenses e
    INNER JOIN clients c ON e.clientId = c.id
    INNER JOIN products p ON e.productId = p.id
    WHERE e.status = 0
""")
    fun getAllExpensesWithDetails(): Flow<List<ExpenseWithDetails>>

    @Query("SELECT * FROM expenses WHERE clientId = :clientId")
    suspend fun getExpensesByClient(clientId: Long): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE productId = :productId")
    suspend fun getExpensesByProduct(productId: Long): List<ExpenseEntity>
}