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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel

    @MockK
    private lateinit var productRepository: ProductRepository

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        every { productRepository.getAllProducts() } returns flowOf(PagingData.empty())
        viewModel = ProductViewModel(productRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    /*@Test
    fun `insertProduct should call repository insertProduct function`() = runTest {

    }*/

}