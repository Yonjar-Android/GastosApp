package com.example.gastosapp.presentation.clients

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.presentation.expenses.TextFieldEdit

@Composable
fun ClientScreen(clientViewModel: ClientViewModel = hiltViewModel()) {

    val clients = clientViewModel.clients.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val windowsSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    when {
        // Screen >= 840dp
        windowsSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        ) -> {
            ClientScreenExpanded(
                clientViewModel,
                context,
                clients.value
            )
        }
        // Screen >= 600dp
        windowsSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> {
            ClientScreenExpanded(
                clientViewModel,
                context,
                clients.value
            )
        }
        // Screen < 600dp
        else -> {
            ClientScreenCompact(clientViewModel, context, clients.value)
        }
    }

    if (clientViewModel.showEditDialog && clientViewModel.clientToEdit != null) {
        DialogClientEdit(
            viewModel = clientViewModel,
            context = context,
            onDismiss = { clientViewModel.closeEditDialog() },

            )
    }

    if (clientViewModel.showDeleteDialog && clientViewModel.clientToEdit != null) {
        DialogClientDelete(
            viewModel = clientViewModel,
            onDismiss = { clientViewModel.closeDeleteDialog() },
        )
    }

}

@Composable
fun ClientScreenExpanded(
    clientViewModel: ClientViewModel,
    context: Context,
    clients: List<ClientEntity>
) {

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClientForm(clientViewModel, context)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            ClientsList(clientViewModel, clients, textModifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun ClientScreenCompact(
    clientViewModel: ClientViewModel,
    context: Context,
    clients: List<ClientEntity>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClientForm(clientViewModel, context)

        Spacer(modifier = Modifier.height(24.dp))

        ClientsList(clientViewModel, clients, textModifier = Modifier.align(Alignment.Start))
    }
}

@Composable
fun ClientForm(
    clientViewModel: ClientViewModel,
    context: Context
) {
        Text(text = "Create Client", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        TextFieldEdit(
            value = clientViewModel.firstName,
            onValueChange = { clientViewModel.onFirstNameChange(it) },
            title = "First Name"
        )

        TextFieldEdit(
            value = clientViewModel.lastName,
            onValueChange = { clientViewModel.onLastNameChange(it) },
            title = "Last Name"
        )

        Button(
            onClick = {
                if (clientViewModel.firstName.isEmpty()) {
                    Toast.makeText(context, "First name is required", Toast.LENGTH_SHORT).show()
                } else {
                    clientViewModel.insertClient()
                    // clean values
                    clientViewModel.onFirstNameChange("")
                    clientViewModel.onLastNameChange("")
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
            Text(text = "Save", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
}

@Composable
fun ClientItem(client: ClientEntity, openDialog: () -> Unit, openDialogDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(fraction = 0.95f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${client.firstName} ${client.lastName}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Row {
            IconButton(
                onClick = {
                    openDialog.invoke()
                }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }

            IconButton(
                onClick = {
                    openDialogDelete.invoke()
                }
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun ClientsList(
    clientViewModel: ClientViewModel, clients: List<ClientEntity>,
    textModifier: Modifier
) {
    Text(
        text = "Clients", fontSize = 24.sp, fontWeight = FontWeight.Bold,
        modifier = textModifier.padding(start = 24.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.95f)
            .padding(start = 16.dp),
    ) {
        items(clients) { client ->
            ClientItem(client, openDialogDelete = {
                clientViewModel.openDeleteDialog(client)
            }, openDialog = {
                clientViewModel.openEditDialog(client)
            })

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DialogClientEdit(
    viewModel: ClientViewModel,
    context: Context,
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
                Text("Edit client", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldEdit(
                    value = viewModel.firstNameEdit,
                    onValueChange = { viewModel.onFirstNameEditChange(it) },
                    title = "First Name"
                )
                TextFieldEdit(
                    value = viewModel.lastNameEdit,
                    onValueChange = { viewModel.onLastNameEditChange(it) },
                    title = "Last Name"
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
                    Button(
                        onClick = {

                            if (viewModel.firstNameEdit.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "First name is required",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.updateClient()
                                viewModel.closeEditDialog()
                            }

                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0XFF1A80E5),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun DialogClientDelete(
    viewModel: ClientViewModel,
    onDismiss: () -> Unit,
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
                Text("Delete client", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    buildAnnotatedString {
                        append("Would you like to delete the user: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${viewModel.clientToEdit?.firstName} ${viewModel.clientToEdit?.lastName}?")
                        }
                    },
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("$numberGenerator")

                Spacer(modifier = Modifier.height(8.dp))

                TextFieldEdit(
                    value = validNumber,
                    onValueChange = { validNumber = it },
                    title = "Enter the number above to confirm"
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
                    Button(
                        onClick = {
                            if (viewModel.clientToEdit != null) {
                                viewModel.deleteClient()
                                viewModel.closeDeleteDialog()
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        enabled = validNumber == numberGenerator.toString()
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}