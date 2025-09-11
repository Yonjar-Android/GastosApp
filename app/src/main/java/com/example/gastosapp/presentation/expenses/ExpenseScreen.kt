package com.example.gastosapp.presentation.expenses

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun MainExpenseScreen(navController: NavHostController){
    val tabs = listOf("Add Expense", "Pending Expenses")

    val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex.intValue,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = selectedTabIndex.intValue == index,
                    onClick = { selectedTabIndex.intValue = index }
                )
            }
        }

        when (selectedTabIndex.intValue) {
            0 -> ExpenseScreen()
            1 -> PndExpensesScreen()
        }
    }

}

@Composable
fun ExpenseScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 24.dp),
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