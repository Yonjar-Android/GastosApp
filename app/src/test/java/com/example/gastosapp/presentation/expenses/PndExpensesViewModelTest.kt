package com.example.gastosapp.presentation.expenses

import androidx.paging.PagingData
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.repositories.ExpenseRepository
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PndExpensesViewModelTest {

    private lateinit var pndExpensesViewModel: PndExpensesViewModel

    @MockK
    private lateinit var expenseRepository: ExpenseRepository

    val expense = ExpenseEntity(
        id = 1L,
        clientId = 1L,
        productId = 1L,
        description = "Test",
        cost = 10.0,
        payment = 5.0,
        date = 1726410880000
    )

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        every { expenseRepository.getAllExpenses() } returns flowOf(PagingData.empty())
        pndExpensesViewModel = PndExpensesViewModel(expenseRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun updateExpense_shouldCallExpenseRepositoryUpdate() = runTest {
        //Given
        coEvery { expenseRepository.update(expense) } just runs

        //When
        pndExpensesViewModel.updateExpense(expense)
        advanceUntilIdle()

        //Then
        assert(expense.id == 1L)
        coVerify(exactly = 1) { expenseRepository.update(expense) }
    }

    @Test
    fun deleteExpense_shouldCallExpenseRepositoryDelete() = runTest {
        //Given
        coEvery { expenseRepository.delete(expense) } just runs

        //When
        pndExpensesViewModel.deleteExpense(expense)
        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { expenseRepository.delete(expense) }
    }

}