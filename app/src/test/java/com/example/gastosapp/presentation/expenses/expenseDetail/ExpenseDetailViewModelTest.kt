package com.example.gastosapp.presentation.expenses.expenseDetail

import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import com.example.gastosapp.data.repositories.ExpenseRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseDetailViewModelTest {

    private lateinit var viewModel: ExpenseDetailViewModel

    @MockK
    private lateinit var expenseRepository: ExpenseRepository

    val dispatcher = StandardTestDispatcher()

    val expenseWithDetails = ExpenseWithDetails(
        id = 1L,
        clientFirstName = "Juan",
        clientLastName = "Centeno",
        productName = "Claro",
        description = "Test",
        cost = 10.0,
        payment = 5.0,
        date = 1726410880000,
        status = true,
        clientId = 1L,
        productId = 1L
    )

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        viewModel = ExpenseDetailViewModel(expenseRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun getExpenseById_shouldCallRepository_getFunction() = runTest{
        //Given
        coEvery { expenseRepository.getExpenseById(1L) } returns expenseWithDetails
        //When
        viewModel.getExpenseById(1L)
        advanceUntilIdle()
        //Then
        assertNotNull(viewModel.expense.value)
        assert(expenseWithDetails.id == viewModel.expense.value?.id)
        coVerify(exactly = 1) { expenseRepository.getExpenseById(1L) }

    }

}