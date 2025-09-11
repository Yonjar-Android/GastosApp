package com.example.gastosapp.presentation.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.repositories.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository
): ViewModel() {

    val clients: StateFlow<List<ClientEntity>> =
        clientRepository.getAllClients()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun insertClient(client: ClientEntity) {
        viewModelScope.launch {
            clientRepository.insert(client)
        }
    }

    fun updateClient(client: ClientEntity) {
        viewModelScope.launch {
            clientRepository.update(client)
        }
    }

    fun deleteClient(client: ClientEntity) {
        viewModelScope.launch {
            clientRepository.delete(client)
        }
    }

    fun getClientById(id: Long) {
        viewModelScope.launch {
            clientRepository.getClientById(id)
        }
    }

}