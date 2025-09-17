package com.example.gastosapp.presentation.expenses

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowSizeClass
import com.example.gastosapp.R
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.database.entities.ProductEntity

@Composable
fun MainExpenseScreen(
    viewModel: ExpenseViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val tabs = listOf( stringResource(R.string.addExpenseStr),
        stringResource(R.string.pendingExpensesStr))

    val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            modifier = Modifier,
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
            0 -> ExpenseScreen(viewModel)
            1 -> PndExpensesScreen(navController = navController)
        }
    }

}

@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel) {

    val clients: LazyPagingItems<ClientEntity> =
        viewModel.clientsPagedData.collectAsLazyPagingItems()

    val products: LazyPagingItems<ProductEntity> = viewModel.products.collectAsLazyPagingItems()

    val context = LocalContext.current

    val windowsSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            // Screen >= 840dp
            windowsSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
            ) -> {

                FormExpense(
                    viewModel, context,
                    modifier = Modifier.weight(1f)
                )
            }
            // Screen >= 600dp
            windowsSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
            ) -> {
                FormExpense(
                    viewModel, context,
                    modifier = Modifier.weight(1f)
                )
            }

            // Screen < 600dp
            else -> {
                FormExpense(viewModel, context, modifier = Modifier.weight(1f))
            }
        }
    }

    if (viewModel.openDialogClient) {
        TableClients(
            clients,
            onDismiss = { viewModel.openDialogClient(false) },
            onClientSelected = { client ->
                viewModel.onClientSelected(client)
                viewModel.openDialogClient(false)
            })
    }

    if (viewModel.openDialogProduct) {
        TableProducts(
            products,
            onDismiss = { viewModel.onOpenDialogProductChange(false) },
            onProductSelected = { product ->
                viewModel.onProductSelected(product)
                viewModel.onOpenDialogProductChange(false)
            })
    }
}

@Composable
fun FormExpense(
    viewModel: ExpenseViewModel,
    context: Context,
    modifier: Modifier = Modifier
) {

    Text(text = stringResource(R.string.recordExpenseStr), fontSize = 18.sp, fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(24.dp))

    TextFieldEdit(
        value = viewModel.clientName,
        onValueChange = {},
        title = stringResource(R.string.selectClientStr),
        readonlyValue = true,
        onClickListener = {
            viewModel.openDialogClient(true)
        }
    )

    TextFieldEdit(
        value = viewModel.productName,
        onValueChange = {},
        title = stringResource(R.string.productStr),
        readonlyValue = true,
        onClickListener = {
            viewModel.onOpenDialogProductChange(true)
        }
    )

    TextFieldEdit(
        value = viewModel.description,
        onValueChange = { viewModel.onDescriptionChanged(it) },
        title = stringResource(R.string.descriptionOptStr)
    )

    TextFieldEdit(
        value = viewModel.costText,
        onValueChange = { viewModel.onCostChanged(it) },
        title = stringResource(R.string.costStr),
        keyBoardType = KeyboardType.Number
    )

    TextFieldEdit(
        value = viewModel.paymentText,
        onValueChange = { viewModel.onPaymentChanged(it) },
        title = stringResource(R.string.amountCollectStr),
        keyBoardType = KeyboardType.Number
    )

    Spacer(modifier = modifier)

    val response = validations(
        viewModel.clientName,
        viewModel.productName,
        viewModel.costText,
        viewModel.paymentText
    )

    Button(
        onClick = {
            if (response.isNotEmpty()) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
            } else {
                // create expense and clean values
                viewModel.insertExpense()

            }
        },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(40.dp)
            .fillMaxWidth(fraction = 0.9f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0XFF1A80E5),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = stringResource(R.string.saveStr), fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TextFieldEdit(
    modifier: Modifier = Modifier,
    value: String,
    title: String,
    readonlyValue: Boolean = false,
    keyBoardType: KeyboardType = KeyboardType.Text,
    onClickListener: () -> Unit = {},
    onValueChange: (String) -> Unit,

    ) {
    Box(
        modifier = Modifier
            .clickable {
                onClickListener.invoke()
            }
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = title) },
            modifier = modifier
                .height(56.dp)
                .fillMaxWidth(fraction = 0.9f)
                .border(
                    1.dp, Color(0XFFDBE0E5),
                    shape = RoundedCornerShape(12.dp)
                ),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.White,
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
            ),
            textStyle = TextStyle(fontSize = 16.sp),
            readOnly = readonlyValue,
            enabled = !readonlyValue, // If readonlyValue is true, disable the TextField
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType)
        )
    }


    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun TableClients(
    clients: LazyPagingItems<ClientEntity>, onDismiss: () -> Unit = {},
    onClientSelected: (ClientEntity) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(usePlatformDefaultWidth = false) // 👈 clave
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp)
        ) {
            if (clients.itemCount == 0) {
                Text(text = stringResource(R.string.noClientsFoundStr), modifier = Modifier.padding(24.dp))
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f)
                    ) {
                        items(
                            count = clients.itemCount,
                            key = clients.itemKey { client -> client.id }
                        ) { client ->
                            val clientValue = clients[client]
                            if (clientValue != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp), // espacio entre filas
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${clientValue.firstName} ${clientValue.lastName}",
                                        fontSize = 16.sp
                                    )

                                    Button(
                                        onClick = {
                                            onClientSelected.invoke(clientValue)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0XFF1A80E5),
                                            contentColor = Color.White
                                        )
                                        ) {
                                        Text(text = stringResource(R.string.selectStr))
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { onDismiss.invoke() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0XFF1A80E5),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.95f)

                    ) {
                        Text(text = stringResource(R.string.closeStr))
                    }
                }

            }
        }
    }
}

@Composable
fun TableProducts(
    products: LazyPagingItems<ProductEntity>, onDismiss: () -> Unit = {},
    onProductSelected: (ProductEntity) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(usePlatformDefaultWidth = false) // 👈 clave
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp)
        ) {
            if (products.itemCount == 0) {
                Text(text = stringResource(R.string.noProductsFoundStr), modifier = Modifier.padding(24.dp))
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f)
                    ) {
                        items(
                            count = products.itemCount,
                            key = products.itemKey { product -> product.id }
                        ) { product ->
                            val productValue = products[product]
                            if (productValue != null) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp), // espacio entre filas
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = productValue.productName,
                                        fontSize = 16.sp
                                    )

                                    Button(
                                        onClick = {
                                            onProductSelected.invoke(productValue)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0XFF1A80E5),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(text = stringResource(R.string.selectStr))
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { onDismiss.invoke() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0XFF1A80E5),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.95f)

                    ) {
                        Text(text = stringResource(R.string.closeStr))
                    }
                }

            }
        }
    }
}

@Composable
fun validations(
    clientName: String,
    product: String,
    cost: String,
    payment: String
): String {
    if (clientName.isEmpty()) {
        return stringResource(R.string.selectClientMsgStr)
    }
    if (product.isEmpty()) {
        return stringResource(R.string.selectProductMsgStr)
    }

    val costValue = cost.toDoubleOrNull()
    if (costValue == null) return stringResource(R.string.costMsgStr)

    val paymentValue = payment.toDoubleOrNull()
    if (paymentValue == null) return stringResource(R.string.paymentMsgStr)

    if (costValue <= 0.0) {
        return stringResource(R.string.validCostStr)
    }
    if (paymentValue <= 0.0) {
        return stringResource(R.string.validPaymentStr)
    }
    return ""
}
