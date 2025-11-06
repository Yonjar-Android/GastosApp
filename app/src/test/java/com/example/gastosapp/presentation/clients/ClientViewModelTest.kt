package com.example.gastosapp.presentation.clients

import androidx.paging.PagingData
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.repositories.ClientRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClientViewModelTest {
    private lateinit var viewModel: ClientViewModel

    @MockK
    private lateinit var clientRepository: ClientRepository

    val dispatcher = StandardTestDispatcher()

    val client = ClientEntity(
        firstName = "Test",
        lastName = "Test"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        every { clientRepository.getAllClients() } returns flowOf(PagingData.empty<ClientEntity>())
        viewModel = ClientViewModel(clientRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertClient_shouldCallRepository_insertFunction() = runTest {
        // Given
        coEvery { clientRepository.insert(client) } returns 1L

        // When
        viewModel.onFirstNameChange("Test")
        viewModel.onLastNameChange("Test")
        viewModel.insertClient()
        advanceUntilIdle()
        // Then
        assert(viewModel.firstName == "Test")
        assert(viewModel.lastName == "Test")
        coVerify(exactly = 1) { clientRepository.insert(client) }
    }

    @Test
    fun updateClient_shouldCallRepository_updateFunction() = runTest {
        //Given
        coEvery { clientRepository.update(client.copy(firstName = "Test2", lastName = "Test2")) } just runs
        //When
        viewModel.openEditDialog(client)
        viewModel.onFirstNameEditChange("Test2")
        viewModel.onLastNameEditChange("Test2")
        viewModel.updateClient()
        advanceUntilIdle()
        //Then
        assert(viewModel.firstNameEdit == "Test2")
        assert(viewModel.lastNameEdit == "Test2")
        coVerify(exactly = 1) { clientRepository.update(client.copy(firstName = "Test2", lastName = "Test2")) }
    }

    @Test
    fun deleteClient_shouldCallRepository_deleteFunction() = runTest {
        val clientToDelete = ClientEntity(
            id = 1L,
            firstName = "Test",
            lastName = "Test"
        )
        //Given
        coEvery { clientRepository.delete(clientToDelete) } just runs
        //When
        viewModel.openDeleteDialog(clientToDelete)
        viewModel.deleteClient()
        advanceUntilIdle()
        //Then
        assertEquals(clientToDelete, viewModel.clientToEdit)
        coVerify(exactly = 1) { clientRepository.delete(clientToDelete) }

    }

}