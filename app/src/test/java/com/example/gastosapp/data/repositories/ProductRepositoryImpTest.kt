package com.example.gastosapp.data.repositories

import androidx.paging.PagingSource
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.gastosapp.data.database.dao.ProductDao
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.data.repositories.helpers.FakeProductPagingSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryImpTest {

    @MockK
    lateinit var productDao: ProductDao

    lateinit var productRepositoryImp: ProductRepositoryImp

    val product = ProductEntity(
        id = 1,
        productName = "Recarga",
    )

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        productRepositoryImp = ProductRepositoryImp(productDao)
    }

    @Test
    fun insertFunction_shouldBeCalledOneTime_andInsertTheProductSuccessfully() = runTest {
        //Given
        coEvery { productDao.insert(product) } returns 1

        //When
        val response = productRepositoryImp.insertProduct(product)

        //Then
        coVerify(exactly = 1) { productDao.insert(product) }

    }

    @Test
    fun updateFunction_shouldBeCalledOneTime_andUpdateTheProductSuccessfully() = runTest {
        //Given
        coEvery { productDao.update(product) } returns Unit

        //When
        productRepositoryImp.updateProduct(product)

        //Then
        coVerify(exactly = 1) { productDao.update(product) }
    }

    @Test
    fun deleteFunction_shouldBeCalledOneTime_andDeleteTheProductSuccessfully() = runTest {

        //Given
        coEvery { productDao.delete(product) } returns Unit

        //When
        productRepositoryImp.deleteProduct(product)

        //Then
        coVerify(exactly = 1) { productDao.delete(product) }

    }

    @Test
    fun getAllProducts_callsDao() = runTest {
        //Given
        val pagingSource = mockk<PagingSource<Int, ProductEntity>>()
        every { productDao.getAllProducts() } returns pagingSource

        //When
        val flow = productRepositoryImp.getAllProducts()

        // Then
        turbineScope {
            flow.test {
                cancelAndConsumeRemainingEvents()
            }
        }

        verify(exactly = 1) { productDao.getAllProducts() }

    }

    @Test
    fun getAllProducts_returnsCorrectData_fromPagingSource() = runTest {
        //Given
        val products = listOf(ProductEntity(1, "Recarga"),
            ProductEntity(2, "Gasolina"))
        val pagingSource = FakeProductPagingSource(products)

        every { productDao.getAllProducts() } returns pagingSource

        //When
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        //Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(products, page.data)

    }

}