package com.example.gastosapp.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.mahmoud.composecharts.barchart.BarChart
import com.mahmoud.composecharts.barchart.BarChartEntity
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getPaymentsByMonth()
        viewModel.getCostsByMonth()
        viewModel.getAvailableYears()
    }

    val paymentsByMonth by viewModel.paymentsByMonth.collectAsStateWithLifecycle()
    val costsByMonth by viewModel.costByMonth.collectAsStateWithLifecycle()

    val windowsSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    when {
        // Screen >= 840dp
        windowsSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        ) -> {
            DashboardScreenExpanded(
                paymentsByMonth, costsByMonth,
                viewModel
            )
        }
        // Screen >= 600dp
        windowsSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> {
            DashboardScreenExpanded(
                paymentsByMonth, costsByMonth,
                viewModel
            )
        }
        // Screen < 600dp
        else -> {
            DashboardScreenCompact(paymentsByMonth, costsByMonth, viewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreenExpanded(
    paymentsByMonth: List<Double>,
    costByMonth: List<Double>,
    viewModel: DashboardViewModel
) {

    var year by rememberSaveable { mutableIntStateOf(LocalDate.now().year) }

    Row(
        modifier = Modifier.fillMaxSize(fraction = 0.95f),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DashboardToShow(
                paymentsByMonth,
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                viewModel = viewModel,
                onChangeYear = { yearSelected ->
                    viewModel.getPaymentsByMonth(yearSelected)
                    viewModel.getCostsByMonth(yearSelected)
                    year = yearSelected
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SummaryInfo(paymentsByMonth, costByMonth, year = year)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreenCompact(
    paymentsByMonth: List<Double>,
    costsByMonth: List<Double>,
    viewModel: DashboardViewModel
) {

    var year by rememberSaveable { mutableIntStateOf(LocalDate.now().year) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardToShow(paymentsByMonth, viewModel = viewModel, onChangeYear = { yearSelected ->
            viewModel.getPaymentsByMonth(yearSelected)
            viewModel.getCostsByMonth(yearSelected)
            year = yearSelected
        })
        Spacer(modifier = Modifier.height(24.dp))
        SummaryInfo(
            paymentsByMonth, costsByMonth,
            year = year
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardToShow(
    paymentsByMonth: List<Double>,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel,
    onChangeYear: (Int) -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }
    val years by viewModel.availableYears.collectAsStateWithLifecycle()

    Text(text = "Dashboard", fontSize = 18.sp, fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(16.dp))

    DropMenuInterface(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        years = years,
        onUpdateData = { year ->
            expanded = false
            onChangeYear.invoke(year) // Function to change the value of year shows in Summary and called viewModel functions
        }
    )


    Spacer(modifier = Modifier.height(16.dp))

    val barChartData = paymentsByMonth.mapIndexed { index, value ->
        BarChartEntity(
            label = "${index + 1}",
            value = value.toFloat(),
            color = Color.Blue
        )
    }

// Get the maximum value of payments and convert to Float
    val maxValue: Float = (paymentsByMonth.maxOrNull() ?: 1000.0).toFloat()

// Round up to a “nice” multiple of 1000
    val roundedMax: Float = ((maxValue + 999f) / 1000f).toInt() * 1000f  // 8921 -> 9000

// Divide by 5 to generate evenly spaced values
    val step: Float = roundedMax / 5f

// Generate vertical axis values
    val verticalAxisValues: List<Float> = List(6) { index -> index * step }

    BarChart(
        modifier = modifier,
        barChartData = barChartData,
        verticalAxisValues = verticalAxisValues
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryInfo(
    paymentsByMonth: List<Double>,
    costByMonth: List<Double>,
    year: Int = LocalDate.now().year
) {
    Text("Summary $year", fontSize = 24.sp, fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(16.dp))

    val totalCost = costByMonth.sum()
    val totalReceived = paymentsByMonth.sum()
    val netProfit = totalReceived - totalCost

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(fraction = 0.80f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0XFFDBE0E5), shape = RoundedCornerShape(12.dp))
                    .padding(10.dp),
            ) {
                Text(
                    "Total Owed", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "C$ $totalCost", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0XFFDBE0E5), shape = RoundedCornerShape(12.dp))
                    .padding(10.dp),

                ) {
                Text(
                    "Total Received", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "C$ $totalReceived", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start, softWrap = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(fraction = 0.80f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.80f)
                    .weight(1f)
                    .border(1.dp, Color(0XFFDBE0E5), shape = RoundedCornerShape(12.dp))
                    .padding(10.dp),

                ) {
                Text(
                    "Net Profit", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "C$ $netProfit", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start, softWrap = true
                )
            }
        }
    }
}

@Composable
fun DropMenuInterface(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    years: List<String>,
    onUpdateData: (Int) -> Unit
) {
    Box {
        Button(
            onClick = { onExpandedChange.invoke(true) }
        ) {
            Text("Select Year")
        }
        DropdownMenu(
            modifier = Modifier.align(Alignment.BottomEnd),
            expanded = expanded,
            onDismissRequest = { onExpandedChange.invoke(false) }
        ) {
            years.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onUpdateData.invoke(it.toInt())
                    }
                )
            }
        }

    }
}