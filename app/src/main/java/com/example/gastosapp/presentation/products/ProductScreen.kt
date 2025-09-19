package com.example.gastosapp.presentation.products

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowSizeClass
import com.example.gastosapp.R
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.presentation.expenses.TextFieldEdit

@Composable
fun ProductScreen(
    productViewModel: ProductViewModel = hiltViewModel()
) {

    val products: LazyPagingItems<ProductEntity> =
        productViewModel.products.collectAsLazyPagingItems()

    val context = LocalContext.current

    val windowsSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    when {
        // Screen >= 840dp
        windowsSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
            ProductScreenExpanded(
                productViewModel,
                context,
                products
            )
        }
        // Screen >= 600dp
        windowsSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            ProductScreenExpanded(productViewModel, context, products)
        }
        // Screen < 600dp
        else -> {
            ProductScreenCompact(
                productViewModel,
                context,
                products
            )
        }
    }

    if (productViewModel.showEditDialog && productViewModel.productToEdit != null) {
        DialogProductEdit(
            context = context,
            productViewModel = productViewModel,
            onDismiss = { productViewModel.closeEditDialog() }
        )
    }

    if (productViewModel.showDeleteDialog && productViewModel.productToEdit != null) {
        DialogProductDelete(
            onDismiss = { productViewModel.closeDeleteDialog() },
            productViewModel = productViewModel
        )
    }
}

@Composable
fun ProductScreenExpanded(
    productViewModel: ProductViewModel,
    context: Context,
    products: LazyPagingItems<ProductEntity>
) {

    Row {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductForm(productViewModel, context)
        }

        Spacer(modifier = Modifier.height(24.dp))


        Column(
            modifier = Modifier.weight(1f)
        ) {
            ProductList(
                productViewModel,
                products,
                textModifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ProductScreenCompact(
    productViewModel: ProductViewModel,
    context: Context,
    products: LazyPagingItems<ProductEntity>
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProductForm(productViewModel, context)

        Spacer(modifier = Modifier.height(24.dp))

        ProductList(
            productViewModel,
            products,
            textModifier = Modifier.align(Alignment.Start)
        )

    }
}

@Composable
fun ProductList(
    productViewModel: ProductViewModel,
    products: LazyPagingItems<ProductEntity>,
    textModifier: Modifier
) {
    Text(
        text = stringResource(R.string.createProductStr),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = textModifier
            .padding(start = 24.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.95f)
            .padding(start = 16.dp),
    ) {
        items(
            count = products.itemCount,
            key = products.itemKey { product -> product.id }
        ) { product ->

            val productValue = products[product]


            if (productValue != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(fraction = 0.95f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = productValue.productName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        IconButton(
                            onClick = {
                                productViewModel.openEditDialog(productValue)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }

                        IconButton(
                            onClick = {
                                productViewModel.openDeleteDialog(productValue)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductForm(
    productViewModel: ProductViewModel,
    context: Context
) {
    Text(
        text = stringResource(R.string.createProductStr),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 24.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    TextFieldEdit(
        value = productViewModel.productName,
        onValueChange = { productViewModel.onProductNameChange(it) },
        title = stringResource(R.string.productNameStr)
    )

    val messageValidation = stringResource(R.string.productNameRqStr)

    Button(
        onClick = {
            if (productViewModel.productName.isEmpty()) {
                Toast.makeText(
                    context, messageValidation, Toast.LENGTH_SHORT
                ).show()
            } else {
                productViewModel.insertProduct()
                // clean values
                productViewModel.onProductNameChange("")
            }
        },
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(fraction = 0.9f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0XFF1A80E5),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(R.string.saveStr),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DialogProductEdit(
    context: Context,
    productViewModel: ProductViewModel,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.editProductStr),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldEdit(
                    value = productViewModel.productNameEdit,
                    onValueChange = { productViewModel.onProductNameEditChange(it) },
                    title = stringResource(R.string.productNameStr)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            stringResource(R.string.cancelStr),
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    val messageValidation = stringResource(R.string.productNameRqStr)

                    Button(
                        onClick = {

                            if (productViewModel.productNameEdit.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    messageValidation,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                productViewModel.updateProduct()
                                productViewModel.closeEditDialog()
                            }

                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0XFF1A80E5),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            stringResource(R.string.saveStr),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DialogProductDelete(
    productViewModel: ProductViewModel,
    onDismiss: () -> Unit
) {
    val numberGenerator = (100000..999999).random()

    var validNumber by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.deleteProductStr),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
                            color = Color.Black)) {
                            append("${stringResource(R.string.wouldYouLikeDelProdStr)}:")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
                            color = Color.Black)) {
                            append(" ${productViewModel.productToEdit?.productName}?")
                        }
                    },
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("$numberGenerator",
                    color = Color.Black,)

                Spacer(modifier = Modifier.height(8.dp))

                TextFieldEdit(
                    value = validNumber,
                    onValueChange = { validNumber = it },
                    title = stringResource(R.string.enterNumberAboveStr)
                )


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancelStr),
                            color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            productViewModel.deleteProduct()
                            productViewModel.closeDeleteDialog()
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        enabled = validNumber == numberGenerator.toString()
                    ) {
                        Text(stringResource(R.string.deleteStr))
                    }
                }
            }
        }
    }
}