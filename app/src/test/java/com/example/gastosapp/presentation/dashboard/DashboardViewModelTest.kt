package com.example.gastosapp.presentation.dashboard

import com.example.gastosapp.data.repositories.ExpenseRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
class DashboardViewModelTest {

    private lateinit var dashboardViewModel: DashboardViewModel

    @MockK
    private lateinit var expenseRepository: ExpenseRepository

    val paymentsByMonth =
        listOf(0.0, 0.0, 0.0, 50.0, 30.0, 100.0, 200.0, 300.0, 0.0, 300.0, 150.0, 0.0)

    val costByMonth = listOf(0.0, 0.0, 0.0, 40.0, 20.0, 50.0, 150.0, 250.0, 0.0, 250.0, 100.0, 0.0)

    val availableYears = listOf("2022", "2023")

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_shouldCallAllRepositoryFunctions_andReturnValues() = runTest {
        // Given
        dashboardViewModel = DashboardViewModel(expenseRepository)

        coEvery { expenseRepository.getPaymentsByMonth() } returns paymentsByMonth
        coEvery { expenseRepository.getCostsByMonth() } returns costByMonth
        coEvery { expenseRepository.getAvailableYears() } returns availableYears

        // When
        dashboardViewModel.getPaymentsByMonth()
        dashboardViewModel.getCostsByMonth()
        dashboardViewModel.getAvailableYears()
        advanceUntilIdle()

        // Then
        assert(dashboardViewModel.paymentsByMonth.value == paymentsByMonth)
        assert(dashboardViewModel.costByMonth.value == costByMonth)
        assert(dashboardViewModel.availableYears.value == availableYears)

    }

    @Test
    fun init_shouldCallAllRepositoryFunctions_andReturnZerosOrEmptyList_ifThereIsNoData() = runTest {
        // Given
        dashboardViewModel = DashboardViewModel(expenseRepository)

        coEvery { expenseRepository.getPaymentsByMonth() } returns List(12) {0.0}
        coEvery { expenseRepository.getCostsByMonth() } returns List(12) {0.0}
        coEvery { expenseRepository.getAvailableYears() } returns emptyList()

        // When
        dashboardViewModel.getPaymentsByMonth()
        dashboardViewModel.getCostsByMonth()
        dashboardViewModel.getAvailableYears()
        advanceUntilIdle()
        // Then
        assert(dashboardViewModel.paymentsByMonth.value == List(12) {0.0})
        assert(dashboardViewModel.costByMonth.value == List(12) {0.0})
        assert(dashboardViewModel.costByMonth.value.size == dashboardViewModel.paymentsByMonth.value.size)
        assert(dashboardViewModel.availableYears.value == listOf<String>())

    }

}