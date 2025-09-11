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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.presentation.products.ProductViewModel
import com.example.gastosapp.presentation.expenses.TextFieldEdit

@Composable
fun ProductScreen(
    productViewModel: ProductViewModel = hiltViewModel()
) {

    val products = productViewModel.products.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // field values
    var productName by remember { mutableStateOf("") }

    var productToModify by remember { mutableStateOf<ProductEntity?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Products", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        TextFieldEdit(
            value = productName,
            onValueChange = { productName = it },
            title = "Product Name"
        )

        Button(
            onClick = {
                if (productName.isEmpty()) {
                    Toast.makeText(context, "Product name is required", Toast.LENGTH_SHORT).show()
                } else {
                    productViewModel.insertClient(
                        ProductEntity(
                            productName = productName
                        )
                    )
                    // clean values
                    productName = ""
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
            Text(text = "Add product", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Product", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.95f)
                .padding(start = 16.dp),
        ) {
            items(products.value) { product ->

                Row(modifier = Modifier.fillMaxWidth(fraction = 0.95f), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = product.productName, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                Row {
                    IconButton(
                        onClick = {
                            productToModify = product
                            showEditDialog = true
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }

                    IconButton(
                        onClick = {
                            productToModify = product
                            showDeleteDialog = true
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }}

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (showEditDialog) {
        DialogProductEdit(
            context = context,
            product = productToModify!!,
            onDismiss = { showEditDialog = false },
            onSave = { product ->
                productViewModel.updateClient(product)
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        DialogProductDelete(
            product = productToModify!!,
            onDismiss = { showDeleteDialog = false },
            onDelete = { product ->
                productViewModel.deleteClient(product)
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun DialogProductEdit(
    context: Context,
    product: ProductEntity,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit
) {
    var productName by remember { mutableStateOf(product.productName) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Edit product", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldEdit(value = productName, onValueChange = { productName = it }, title = "Product's Name")

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {

                        if (productName.isEmpty()){
                            Toast.makeText(context, "Product name is required", Toast.LENGTH_SHORT).show()
                        } else{
                            onSave(product.copy(productName = productName))
                        }

                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF1A80E5),
                        contentColor = Color.White
                    )) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun DialogProductDelete(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onDelete: (ProductEntity) -> Unit
) {
    val numberGenerator = (100000..999999).random()

    var validNumber by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Delete product", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    buildAnnotatedString {
                        append("Would you like to delete the product: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${product.productName}?")
                        }
                    },
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("$numberGenerator")

                Spacer(modifier = Modifier.height(8.dp))

                TextFieldEdit(
                    value = validNumber,
                    onValueChange = { validNumber = it },
                    title = "Enter the number to confirm"
                )


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onDelete(product)
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF1A80E5),
                        contentColor = Color.White
                    ),
                        enabled = validNumber == numberGenerator.toString()) {
                        Text("Save")
                    }
                }
            }
        }
    }
}