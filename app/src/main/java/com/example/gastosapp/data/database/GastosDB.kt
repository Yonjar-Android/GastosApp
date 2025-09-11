package com.example.gastosapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gastosapp.data.database.dao.ClientDao
import com.example.gastosapp.data.database.dao.ExpenseDao
import com.example.gastosapp.data.database.dao.ProductDao
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ProductEntity

@Database(entities = [ClientEntity::class, ProductEntity::class, ExpenseEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class GastosDB : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun productDao(): ProductDao
    abstract fun expenseDao(): ExpenseDao
}
