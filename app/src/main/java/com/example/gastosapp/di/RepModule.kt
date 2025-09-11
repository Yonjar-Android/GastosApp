package com.example.gastosapp.di

import com.example.gastosapp.data.database.dao.ClientDao
import com.example.gastosapp.data.database.dao.ExpenseDao
import com.example.gastosapp.data.database.dao.ProductDao
import com.example.gastosapp.data.repositories.ClientRepository
import com.example.gastosapp.data.repositories.ClientRepositoryImp
import com.example.gastosapp.data.repositories.ExpenseRepository
import com.example.gastosapp.data.repositories.ExpenseRepositoryImp
import com.example.gastosapp.data.repositories.ProductRepository
import com.example.gastosapp.data.repositories.ProductRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepModule {
    @Provides
    @Singleton
    fun provideClientRepository(clientDao: ClientDao): ClientRepository {
        return ClientRepositoryImp(clientDao)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImp(expenseDao)
    }

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepository {
        return ProductRepositoryImp(productDao)
    }

}