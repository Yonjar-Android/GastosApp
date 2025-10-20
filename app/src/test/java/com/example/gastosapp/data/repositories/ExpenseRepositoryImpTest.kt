package com.example.gastosapp.data.repositories

import androidx.paging.PagingSource
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.gastosapp.data.database.dao.ExpenseDao
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ExpenseWithDetails
import com.example.gastosapp.data.database.entities.MonthlyTotalDb
import com.example.gastosapp.data.repositories.helpers.FakeExpensePagingSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class ExpenseRepositoryImpTest {

    @MockK
    lateinit var expenseDao: ExpenseDao

    lateinit var expenseRepositoryImp: ExpenseRepositoryImp

    val expense = ExpenseEntity(
        id = 1,
        productId = 1,
        cost = 100.0,
        date = System.currentTimeMillis(),
        clientId = 1,
        status = false,
        payment = 120.0,
        description = "Test expense"
    )

    val expenseWithDetails = ExpenseWithDetails(
        id = 1,
        productId = 1,
        cost = 100.0,
        date = System.currentTimeMillis(),
        clientId = 1,
        status = false,
        payment = 120.0,
        description = "Test expense",
        productName = "Recarga",
        clientFirstName = "Juan",
        clientLastName = "Perez"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        expenseRepositoryImp = ExpenseRepositoryImp(expenseDao)
    }

    @Test
    fun `insert function should be called one time and insert the expense successfully`() =
        runTest {
            // Given
            coEvery { expenseDao.insert(expense) } returns 1

            // When
            val response = expenseRepositoryImp.insert(expense)

            // Then
            coVerify(exactly = 1) { expenseDao.insert(expense) }
            assertTrue(response == 1L)
        }

    @Test
    fun `update function should be called one time and update the expense successfully`() =
        runTest {
            // Given
            coEvery { expenseDao.update(expense) } returns Unit

            // When
            expenseRepositoryImp.update(expense)

            // Then
            coVerify(exactly = 1) { expenseDao.update(expense) }
        }

    @Test
    fun `delete function should be called one time and delete the expense successfully`() =
        runTest {
            // Given
            coEvery { expenseDao.delete(expense) } returns Unit

            // When
            expenseRepositoryImp.delete(expense)

            // Then
            coVerify(exactly = 1) { expenseDao.delete(expense) }
        }

    @Test
    fun `getExpenseById should return a ExpenseEntity when the expense is found`() = runTest {
        // Given
        coEvery { expenseDao.getExpenseById(1) } returns expenseWithDetails

        // When
        val response = expenseRepositoryImp.getExpenseById(1)

        // Then
        coVerify(exactly = 1) { expenseDao.getExpenseById(1) }
        assertTrue(response == expenseWithDetails)
        assertTrue(response is ExpenseWithDetails)
    }

    @Test
    fun `getExpenseById should return null when the expense is not found`() = runTest {
        // Given
        coEvery { expenseDao.getExpenseById(1) } returns null

        // When
        val response = expenseRepositoryImp.getExpenseById(1)

        // Then
        coVerify(exactly = 1) { expenseDao.getExpenseById(1) }
        assertTrue(response == null)
    }

    @Test
    fun `get all expenses calls dao`() = runTest {
        // Given
        val pagingSource = mockk<PagingSource<Int, ExpenseWithDetails>>()
        coEvery { expenseDao.getAllExpensesWithDetails() } returns pagingSource

        // When
        val flow = expenseRepositoryImp.getAllExpenses()

        // Then

        turbineScope {
            flow.test {
                cancelAndConsumeRemainingEvents()
            }
        }

        coVerify(exactly = 1) { expenseDao.getAllExpensesWithDetails() }
    }

    @Test
    fun `get all expenses returns correct data from paging source`() = runTest {
        // Given
        val expenses = listOf<ExpenseWithDetails>(
            expenseWithDetails
        )
        val pagingSource = FakeExpensePagingSource(expenses)
        every { expenseDao.getAllExpensesWithDetails() } returns pagingSource

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
        assertTrue(page.data == expenses)

    }

    @Test
    fun `getExpensesByClientId should call dao`() = runTest {
        // Given
        val pagingSource = mockk<PagingSource<Int, ExpenseWithDetails>>()
        coEvery { expenseDao.getExpensesByClientId(1) } returns pagingSource

        // When
        val flow = expenseRepositoryImp.getExpensesByClientId(1)

        // Then

        turbineScope {
            flow.test {
                cancelAndConsumeRemainingEvents()
            }
        }

        coVerify(exactly = 1) { expenseDao.getExpensesByClientId(1) }
    }

    @Test
    fun `getExpensesByClientId returns correct data from paging source`() = runTest {
        // Given
        val expenses = listOf<ExpenseWithDetails>(
            expenseWithDetails
        )
        val pagingSource = FakeExpensePagingSource(expenses)
        every { expenseDao.getExpensesByClientId(1) } returns pagingSource

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
        assertTrue(page.data == expenses)

    }

    @Test
    fun `getPaymentByMonth should return a list of payments by month`() = runTest {
        // Given
        val listOfPayments = listOf<Double>(
            0.0, 0.0, 120.0, 200.0, 300.0, 150.0,
            0.0, 0.0, 190.0, 0.0, 1.0, 100.0
        )

        val listOfPaymentsDb = listOf<MonthlyTotalDb>(
            MonthlyTotalDb("01", 0.0, 0.0),
            MonthlyTotalDb("02", 0.0, 0.0),
            MonthlyTotalDb("03", 120.0, 0.0),
            MonthlyTotalDb("04", 200.0, 0.0),
            MonthlyTotalDb("05", 300.0, 0.0),
            MonthlyTotalDb("06", 150.0, 0.0),
            MonthlyTotalDb("07", 0.0, 0.0),
            MonthlyTotalDb("08", 0.0, 0.0),
            MonthlyTotalDb("09", 190.0, 0.0),
            MonthlyTotalDb("10", 0.0, 0.0),
            MonthlyTotalDb("11", 1.0, 0.0),
            MonthlyTotalDb("12", 100.0, 0.0)
        )

        coEvery { expenseDao.getPaymentsByMonth("2025") } returns listOfPaymentsDb

        // When
        val response = expenseRepositoryImp.getPaymentsByMonth(2025)

        // Then
        coVerify(exactly = 1) { expenseDao.getPaymentsByMonth("2025") }
        assertTrue(response == listOfPayments)
        assertEquals(response.size,listOfPayments.size)
    }

    @Test
    fun `getPaymentByMonth should return a list of 12 zeros when there are no payments`() = runTest {
        //Given
        coEvery { expenseDao.getPaymentsByMonth("2025") } returns emptyList()
        //When
        val response = expenseRepositoryImp.getPaymentsByMonth(2025)

        //Then
        coVerify(exactly = 1) { expenseDao.getPaymentsByMonth("2025") }
        assertTrue(response.size == 12)
        assertTrue(response.all { it == 0.0 })
    }

    @Test
    fun `getAvailableYears should return a list of available years`() = runTest {
        //Given
        val listOfYears = listOf<String>("2025","2024","2023")
        coEvery { expenseDao.getAvailableYears() } returns listOfYears

        //When
        val response = expenseRepositoryImp.getAvailableYears()

        //Then
        coVerify(exactly = 1) { expenseDao.getAvailableYears() }
        assertTrue(response == listOfYears)
    }

    @Test
    fun `getAvailableYears should return an empty list when there are no available years`() = runTest {
        //Given
        coEvery { expenseDao.getAvailableYears() } returns emptyList()
        //When

        val response = expenseRepositoryImp.getAvailableYears()

        //Then
        coVerify(exactly = 1) { expenseDao.getAvailableYears() }
        assertTrue(response.isEmpty())
    }

    @Test
    fun `getCostsByMonth should return a list of costs by month`() = runTest {
        // Given
        val listOfCosts = listOf<Double>(
            0.0, 0.0, 120.0, 200.0, 300.0, 150.0,
            0.0, 0.0, 190.0, 0.0, 1.0, 100.0
        )

        val listOfCostsDb = listOf<MonthlyTotalDb>(
            MonthlyTotalDb("01", 0.0, 0.0),
            MonthlyTotalDb("02", 0.0, 0.0),
            MonthlyTotalDb("03", 0.0, 120.0),
            MonthlyTotalDb("04", 0.0, 200.0),
            MonthlyTotalDb("05", 0.0, 300.0),
            MonthlyTotalDb("06", 0.0, 150.0),
            MonthlyTotalDb("07", 0.0, 0.0),
            MonthlyTotalDb("08", 0.0, 0.0),
            MonthlyTotalDb("09", 0.0, 190.0),
            MonthlyTotalDb("10", 0.0, 0.0),
            MonthlyTotalDb("11", 0.0, 1.0),
            MonthlyTotalDb("12", 0.0, 100.0)
        )

        coEvery { expenseDao.getPaymentsByMonth("2025") } returns listOfCostsDb

        // When
        val response = expenseRepositoryImp.getCostsByMonth(2025)

        // Then
        coVerify(exactly = 1) { expenseDao.getPaymentsByMonth("2025") }
        assertTrue(response == listOfCosts)
        assertEquals(response.size,listOfCostsDb.size)
    }

    @Test
    fun `getCostsByMonth should return an empty list when there are no costs`() = runTest {
        // Given

        coEvery { expenseDao.getPaymentsByMonth("2025") } returns emptyList()

        // When
        val response = expenseRepositoryImp.getCostsByMonth(2025)

        // Then
        coVerify(exactly = 1) { expenseDao.getPaymentsByMonth("2025") }
        assertTrue(response.size == 12)
        assertTrue(response.all { it == 0.0 })
    }

}