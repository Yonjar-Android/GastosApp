package com.example.gastosapp.di

import android.app.Application
import androidx.room.Room
import com.example.gastosapp.data.database.GastosDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun providesDb(app: Application): GastosDB {
        return Room.databaseBuilder(
            app,
            GastosDB::class.java,
            "gastos_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesClientDao(db: GastosDB) = db.clientDao()

    @Provides
    @Singleton
    fun providesProductDao(db: GastosDB) = db.productDao()

    @Provides
    @Singleton
    fun providesExpenseDao(db: GastosDB) = db.expenseDao()

}