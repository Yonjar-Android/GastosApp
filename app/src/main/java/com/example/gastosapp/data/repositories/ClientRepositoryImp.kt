package com.example.gastosapp.data.repositories

import com.example.gastosapp.data.database.dao.ClientDao
import com.example.gastosapp.data.database.entities.ClientEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientRepositoryImp @Inject constructor(
    private val clientDao: ClientDao
): ClientRepository {

    override suspend fun insert(client: ClientEntity): Long {
        return clientDao.insert(client)
    }

    override suspend fun update(client: ClientEntity) {
        clientDao.update(client)
    }

    override suspend fun delete(client: ClientEntity) {
        clientDao.delete(client)
    }

    override suspend fun getClientById(id: Long): ClientEntity? {
        return clientDao.getClientById(id)
    }

    override fun getAllClients(): Flow<List<ClientEntity>> {
        return clientDao.getAllClients()
    }
}