package com.example.gastosapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosapp.data.database.entities.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
    interface ClientDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(client: ClientEntity): Long

        @Update
        suspend fun update(client: ClientEntity)

        @Delete
        suspend fun delete(client: ClientEntity)

        @Query("SELECT * FROM clients WHERE id = :id")
        suspend fun getClientById(id: Long): ClientEntity?

        @Query("SELECT * FROM clients")
        fun getAllClients(): Flow<List<ClientEntity>>
    }
