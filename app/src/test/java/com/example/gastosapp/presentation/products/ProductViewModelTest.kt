package com.example.gastosapp.presentation.products

import androidx.paging.PagingData
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.data.repositories.ProductRepository
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
class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel

    @MockK
    private lateinit var productRepository: ProductRepository

    val dispatcher = StandardTestDispatcher()

    val product = ProductEntity(
        productName = "Test Product"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        every { productRepository.getAllProducts() } returns flowOf(PagingData.empty())
        viewModel = ProductViewModel(productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertProduct_shouldCallRepository_insertProductFunction() = runTest {
        coEvery { productRepository.insertProduct(product) } just runs

        viewModel.onProductNameChange("Test Product")
        viewModel.insertProduct()
        advanceUntilIdle()
        assertEquals("Test Product", viewModel.productName)
        coVerify(exactly = 1) { productRepository.insertProduct(product) }
    }

    @Test
    fun updateProduct_shouldCallRepository_updateProductFunction() = runTest {
        // Given
        coEvery {
            productRepository.updateProduct(
                product
                    .copy(productName = "Test Product2")
            )
        } just runs

        // When
        viewModel.openEditDialog(product)
        viewModel.onProductNameEditChange("Test Product2")
        viewModel.updateProduct()
        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) {
            productRepository.updateProduct(
                product
                    .copy(productName = "Test Product2")
            )
        }
        assertEquals("Test Product2", viewModel.productNameEdit)

    }

    @Test
    fun deleteProduct_shouldCallRepository_deleteProductFunction() = runTest {
        // Given
        coEvery { productRepository.deleteProduct(product) } just runs

        //When
        viewModel.openDeleteDialog(product)
        viewModel.deleteProduct()
        advanceUntilIdle()

        //Then
        coVerify(exactly = 1) { productRepository.deleteProduct(product) }

    }
}

