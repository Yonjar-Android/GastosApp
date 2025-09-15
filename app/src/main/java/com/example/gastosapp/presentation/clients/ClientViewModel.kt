package com.example.gastosapp.presentation.clients

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.repositories.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository
): ViewModel() {

    val clientsPagedData: Flow<PagingData<ClientEntity>> =
        clientRepository.getAllClients()
            .cachedIn(viewModelScope)


    // Client Functions
    fun insertClient() {
        viewModelScope.launch {
            clientRepository.insert(
                ClientEntity(
                    firstName = firstName,
                    lastName = lastName
                )
            )
        }
    }

    fun updateClient() {
        viewModelScope.launch {
            clientRepository.update(
                clientToEdit!!.copy(
                    firstName = firstNameEdit,
                    lastName = lastNameEdit
                )
            )
        }
    }

    fun deleteClient() {
        viewModelScope.launch {
            clientRepository.delete(clientToEdit!!)
        }
    }

    // Fields
    var firstName by mutableStateOf("")
        private set

    var lastName by mutableStateOf("")
        private set

    fun onFirstNameChange(newValue: String) {
        firstName = newValue
    }

    fun onLastNameChange(newValue: String) {
        lastName = newValue
    }

    // Dialogs values
    var showEditDialog by mutableStateOf(false)
        private set

    var showDeleteDialog by mutableStateOf(false)
        private set

    var clientToEdit by mutableStateOf<ClientEntity?>(null)
        private set

    fun openEditDialog(client: ClientEntity) {
        clientToEdit = client
        showEditDialog = true
        firstNameEdit = client.firstName
        lastNameEdit = client.lastName
    }

    var firstNameEdit by mutableStateOf("")
    private set

    var lastNameEdit by mutableStateOf("")
    private set

    fun onFirstNameEditChange(newValue: String) {
        firstNameEdit = newValue
    }

    fun onLastNameEditChange(newValue: String) {
        lastNameEdit = newValue
    }

    fun closeEditDialog() {
        showEditDialog = false
        clientToEdit = null
    }

    fun openDeleteDialog(client: ClientEntity) {
        clientToEdit = client
        showDeleteDialog = true
    }

    fun closeDeleteDialog() {
        showDeleteDialog = false
        clientToEdit = null
    }

}