package com.example.gastosapp.presentation.expenses

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpenseScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Record Expense", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        TextFieldEdit(
            value = "",
            onValueChange = {},
            title = "Select Client"
        )

        TextFieldEdit(
            value = "",
            onValueChange = {},
            title = "Product"
        )

        TextFieldEdit(
            value = "",
            onValueChange = {},
            title = "Description (Optional)"
        )

        TextFieldEdit(
            value = "",
            onValueChange = {},
            title = "Cost"
        )

        TextFieldEdit(
            value = "",
            onValueChange = {},
            title = "Amount to Collect"
        )

        Button(
            onClick = {

            },
            modifier = Modifier.height(40.dp)
                .fillMaxWidth(fraction = 0.9f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0XFF1A80E5),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Save", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Pending Expenses", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start).padding(start = 24.dp))

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(10) {
                ExpenseItem()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ExpenseItem(){
    Row(
        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
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
            Text(text = "Total: 300 C$", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TextFieldEdit(value: String, title: String,onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = title) },
        modifier = Modifier.height(56.dp)
            .fillMaxWidth(fraction = 0.9f)
            .border(1.dp, Color(0XFFDBE0E5),
                shape = RoundedCornerShape(12.dp)),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = TextStyle(fontSize = 16.sp)
    )

    Spacer(modifier = Modifier.height(24.dp))
}