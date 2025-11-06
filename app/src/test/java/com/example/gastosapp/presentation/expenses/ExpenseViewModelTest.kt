package com.example.gastosapp.presentation.expenses

import androidx.paging.PagingData
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.data.repositories.ClientRepository
import com.example.gastosapp.data.repositories.ExpenseRepository
import com.example.gastosapp.data.repositories.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
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
class ExpenseViewModelTest {

    private lateinit var expenseViewModel: ExpenseViewModel

    @MockK
    private lateinit var expenseRepository: ExpenseRepository

    @MockK
    private lateinit var clientRepository: ClientRepository

    @MockK
    private lateinit var productRepository: ProductRepository

    val dispatcher = StandardTestDispatcher()

    val client = ClientEntity(id = 1L, firstName = "Test", lastName = "Test")

    val product = ProductEntity(id = 1L, productName = "Test")

    val expense = ExpenseEntity(
        id = 0L,
        clientId = 1L,
        productId = 1L,
        description = "Test",
        cost = 50.0,
        payment = 100.0,
        date = 1726410880000
    )
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        every { clientRepository.getAllClients() } returns flowOf(PagingData.empty())
        every { productRepository.getAllProducts() } returns flowOf(PagingData.empty())
        expenseViewModel = ExpenseViewModel(expenseRepository, clientRepository, productRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun insertExpense_shouldCallExpenseRepository_insertFunction() = runTest {
        // Given
        coEvery { expenseRepository.insert(any()) } returns 1L

        // When
        expenseViewModel.onClientSelected(client)
        expenseViewModel.onProductSelected(product)
        expenseViewModel.onCostChanged("50.0")
        expenseViewModel.onDescriptionChanged("Test")
        expenseViewModel.onPaymentChanged("100.0")
        expenseViewModel.insertExpense()
        advanceUntilIdle()

        // Then: verificamos por propiedades (ignorando id)
        coVerify(exactly = 1) {
            expenseRepository.insert(match {
                it.clientId == client.id &&
                        it.productId == product.id &&
                        it.cost == 50.0 &&
                        it.payment == 100.0 &&
                        it.description == "Test" &&
                        it.date == 1726410880000L
            })
        }

        // Y como el ViewModel limpia valores, asegura el reset
        assertEquals(0L, expenseViewModel.clientId)
        assertEquals("", expenseViewModel.description)
        assertEquals(0.0, expenseViewModel.cost, 0.0)
    }

}