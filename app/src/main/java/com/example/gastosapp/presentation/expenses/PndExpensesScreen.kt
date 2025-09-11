package com.example.gastosapp.presentation.expenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gastosapp.data.database.entities.ExpenseEntity

@Composable
fun PndExpensesScreen(viewModel: PndExpensesViewModel = hiltViewModel()) {

    val expenses by viewModel.expenses.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Pending Expenses", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(expenses) { expense ->
                ExpenseItem(expense)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: ExpenseEntity){
    Row(
        modifier = Modifier.fillMaxWidth(fraction = 0.95f)

            .clickable{

            }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Client: Abdiel Obando", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Product: Recarga Claro", fontSize = 16.sp)
        }

        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center) {
            Text(text = "Total: ${expense.payment} C$", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}