package com.example.gastosapp.data.repositories

import androidx.paging.PagingSource
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.gastosapp.data.database.dao.ClientDao
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.repositories.helpers.FakeClientPagingSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ClientRepositoryImpTest {

    @MockK
    lateinit var clientDao: ClientDao

    lateinit var clientRepositoryImp: ClientRepositoryImp

    val client = ClientEntity(
        id = 1,
        firstName = "Juan",
        lastName = "Perez",
    )

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        clientRepositoryImp = ClientRepositoryImp(clientDao)
    }

    @Test
    fun insertFunction_shouldBeCalledOneTimeAndInsertTheClientSuccessfully() = runTest {
        // Given
        coEvery { clientDao.insert(client) } returns 1

        // When
        val response = clientRepositoryImp.insert(client)

        // Then
        coVerify(exactly = 1) { clientDao.insert(client) }
        assert(response == 1L)
    }

    @Test
    fun updateFunction_shouldBeCalledOneTime_andUpdateTheClientSuccessfully() = runTest {
        // Given
        coEvery { clientDao.update(client) } returns Unit

        // When
        clientRepositoryImp.update(client)

        // Then
        coVerify(exactly = 1) { clientDao.update(client) }
    }

    @Test
    fun deleteFunction_shouldBeCalledOneTime_andDeleteTheClientSuccessfully() = runTest {

        // Given
        coEvery { clientDao.delete(client) } returns Unit

        // When
        clientRepositoryImp.delete(client)

        // Then
        coVerify(exactly = 1) { clientDao.delete(client) }
    }

    @Test
    fun getClientById_shouldReturnAClientEntity_whenTheClientIsFound() = runTest {
        // Given
        coEvery { clientDao.getClientById(1) } returns client

        // When
        val response = clientRepositoryImp.getClientById(1)

        // Then
        coVerify(exactly = 1) { clientDao.getClientById(1) }
        assertEquals(client, response)
        assertTrue(client.id == 1L)
    }

    @Test
    fun getClientById_shouldReturnNull_whenTheClientIsNotFound() = runTest {
        // Given
        coEvery { clientDao.getClientById(1) } returns null

        // When
        val response = clientRepositoryImp.getClientById(1)

        // Then
        coVerify(exactly = 1) { clientDao.getClientById(1) }
        assertEquals(null, response)
    }

    @Test
    fun getAllClients_callsDao() = runTest {
        // Given
        val pagingSource = mockk<PagingSource<Int, ClientEntity>>()
        every { clientDao.getAllClients() } returns pagingSource

        // When
        val flow = clientRepositoryImp.getAllClients()

        // Then
        turbineScope {
            flow.test {
                cancelAndConsumeRemainingEvents()
            }
        }

        verify(exactly = 1) { clientDao.getAllClients() }
    }

    @Test
    fun getAllClients_returnsCorrectData_fromPagingSource() = runTest {
        // Given
        val clients = listOf(ClientEntity(1, "Juan", "Perez"))
        val pagingSource = FakeClientPagingSource(clients)
        every { clientDao.getAllClients() } returns pagingSource

        // When
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(clients, page.data)
    }

}