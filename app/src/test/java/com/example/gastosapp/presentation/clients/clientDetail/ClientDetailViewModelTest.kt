package com.example.gastosapp.presentation.clients.clientDetail

import androidx.paging.PagingData
import com.example.gastosapp.data.repositories.ExpenseRepository
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClientDetailViewModelTest {
    private lateinit var viewModel: ClientDetailViewModel

    @MockK
    private lateinit var expenseRepository: ExpenseRepository

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        viewModel = ClientDetailViewModel(expenseRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun expenses_shouldNotRequestRepoWhenClientIdIsNull() = runTest {
        // Given: no hacemos stubbing para ningún id en particular

        // When: no seteamos clientId (queda null por defecto)
        advanceUntilIdle()

        // Then: repository no fue llamado
        verify { expenseRepository wasNot Called }
    }

}