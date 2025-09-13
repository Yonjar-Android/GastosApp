package com.example.gastosapp.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    }

    val paymentsByMonth by viewModel.paymentsByMonth.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Dashboard", fontSize = 18.sp, fontWeight = FontWeight.Bold)

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
            barChartData = barChartData,
            verticalAxisValues = verticalAxisValues
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Summary ${LocalDate.now().year}", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        val totalReceived = paymentsByMonth.sum()

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
                    modifier = Modifier.weight(1f)
                        .border(1.dp, Color(0XFFDBE0E5), shape = RoundedCornerShape(12.dp))
                        .padding(10.dp),
                ) {
                    Text("Total Owed", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("C$ 0.00", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                        .border(1.dp, Color(0XFFDBE0E5), shape = RoundedCornerShape(12.dp))
                        .padding(10.dp),

                    ) {
                    Text("Total Received", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("C$ $totalReceived", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start, softWrap = true)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(fraction = 0.80f)
                        .weight(1f)
                        .border(1.dp, Color(0XFFDBE0E5), shape = RoundedCornerShape(12.dp))
                        .padding(10.dp),

                    ) {
                    Text("Net Profit", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("C$ $totalReceived", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start, softWrap = true)
                }
            }

        }

    }
}