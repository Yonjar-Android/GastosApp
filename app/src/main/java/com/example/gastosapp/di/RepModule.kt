package com.example.gastosapp.di

import com.example.gastosapp.data.database.dao.ClientDao
import com.example.gastosapp.data.database.dao.ExpenseDao
import com.example.gastosapp.data.repositories.ClientRepository
import com.example.gastosapp.data.repositories.ClientRepositoryImp
import com.example.gastosapp.data.repositories.ExpenseRepository
import com.example.gastosapp.data.repositories.ExpenseRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepModule {
    @Provides
    fun provideClientRepository(clientDao: ClientDao): ClientRepository {
        return ClientRepositoryImp(clientDao)
    }

    @Provides
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImp(expenseDao)
    }

}