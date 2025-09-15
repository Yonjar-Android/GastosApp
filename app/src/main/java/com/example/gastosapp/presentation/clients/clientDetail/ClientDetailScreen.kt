package com.example.gastosapp.presentation.clients.clientDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.gastosapp.data.database.entities.ExpenseWithDetails

@Composable
fun ClientDetailScreen(
    clientId: Long,
    clientViewModel: ClientDetailViewModel = hiltViewModel(),
    navController: NavHostController
) {
    LaunchedEffect(clientId) {
        clientViewModel.setClientId(clientId)
    }

    val expenses: LazyPagingItems<ExpenseWithDetails> =
        clientViewModel.expenses.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxWidth(fraction = 0.95f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Client Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 32.dp),
                textAlign = TextAlign.Center
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        if (expenses.itemCount != 0) {
            Text(
                text = "Client Name: ${expenses[0]?.clientFirstName} ${expenses[0]?.clientLastName}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(
                    count = expenses.itemCount,
                    key = expenses.itemKey{ expense -> expense.id }
                ) {
                    val expenseItem = expenses[it]
                    if (expenseItem != null) {
                        ItemExpense(expenseItem)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemExpense(details: ExpenseWithDetails) {
    Row(
        modifier = Modifier.fillMaxSize(fraction = 0.9f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = details.productName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Payment: C$ ${details.payment}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0XFF1AA708)
            )
        }

        Column {
            if (details.status) {
                Text(
                    text = "Paid", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = Color(0XFF1AA708)
                )
            } else {
                Text(
                    text = "Pending", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }

        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}