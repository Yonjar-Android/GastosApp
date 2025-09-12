package com.example.gastosapp.presentation.expenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ExpenseWithDetails

@Composable
fun PndExpensesScreen(viewModel: PndExpensesViewModel = hiltViewModel()) {

    val expenses by viewModel.expenses.collectAsStateWithLifecycle()

    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    var expenseToModify by rememberSaveable { mutableStateOf<ExpenseEntity?>(null) }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Pending Expenses", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(expenses) { expense ->
                ExpenseItem(
                    expense,
                    onConfirm = {
                        expenseToModify = it
                        showConfirmDialog = true
                    },
                    onDelete = {
                        showDeleteDialog = true
                        expenseToModify = it
                    })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (showDeleteDialog) {
        DeleteDialog(onDismiss = { showDeleteDialog = false }, onConfirm = {
            showDeleteDialog = false
            if (expenseToModify != null) {
                viewModel.deleteExpense(expenseToModify!!)
            }
        })
    }

    if (expenseToModify != null && showConfirmDialog) {
        ConfirmDialog(onDismiss = { expenseToModify = null }, onConfirm = {
            expenseToModify?.let {
                viewModel.updateExpense(it)
            }
            expenseToModify = null
        })

    }
}

@Composable
fun ExpenseItem(
    expense: ExpenseWithDetails,
    onConfirm: (ExpenseEntity) -> Unit = {},
    onDelete: (ExpenseEntity) -> Unit = {}
) {

    var showActions by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.95f)

            .combinedClickable(
                onClick = { showActions = !showActions },
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Client: ${expense.clientFirstName} ${expense.clientLastName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Product: ${expense.productName}", fontSize = 16.sp)
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total: ${expense.payment} C$",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    AnimatedVisibility(
        showActions,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.95f),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onDelete(
                        ExpenseEntity(
                            id = expense.id,
                            description = expense.description,
                            cost = expense.cost,
                            payment = expense.payment,
                            date = expense.date,
                            clientId = expense.clientId,
                            productId = expense.productId
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Delete")
            }

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {

                    onConfirm(
                        ExpenseEntity(
                            id = expense.id,
                            description = expense.description,
                            cost = expense.cost,
                            payment = expense.payment,
                            date = expense.date,
                            clientId = expense.clientId,
                            productId = expense.productId,
                            status = true  // Status change to true meaning that the expense has been paid
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text("Confirm payment")
            }
        }
    }
}

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false) // 👈 clave
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.95f)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Are you sure you want to delete this expense?",
                fontSize = 16.sp, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Button(onClick = {
                    onConfirm.invoke()
                    onDismiss.invoke()
                }) {
                    Text(text = "Yes")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { onDismiss.invoke() }) {
                    Text(text = "No")
                }
            }

        }
    }
}

@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false) // 👈 clave
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.95f)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Are you sure you want to confirm this payment?",
                fontSize = 16.sp, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Button(onClick = {
                    onDismiss.invoke()
                }) {
                    Text(text = "No")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    onConfirm.invoke()
                    onDismiss.invoke()
                }) {
                    Text(text = "Yes")
                }
            }

        }
    }
}