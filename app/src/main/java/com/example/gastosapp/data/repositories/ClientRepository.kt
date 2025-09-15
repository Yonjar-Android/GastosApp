package com.example.gastosapp.data.repositories

import androidx.paging.PagingData
import com.example.gastosapp.data.database.entities.ClientEntity
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    suspend fun insert(client: ClientEntity): Long
    suspend fun update(client: ClientEntity)
    suspend fun delete(client: ClientEntity)
    suspend fun getClientById(id: Long): ClientEntity?

    fun getAllClients(): Flow<PagingData<ClientEntity>>
}