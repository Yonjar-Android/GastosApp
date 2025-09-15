package com.example.gastosapp.presentation.expenses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.database.entities.ClientEntity
import com.example.gastosapp.data.database.entities.ExpenseEntity
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.data.repositories.ClientRepository
import com.example.gastosapp.data.repositories.ExpenseRepository
import com.example.gastosapp.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    clientRepository: ClientRepository,
    productRepository: ProductRepository
): ViewModel() {

    val clients: StateFlow<List<ClientEntity>>
    = clientRepository.getAllClients().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val products: StateFlow<List<ProductEntity>>
    = productRepository.getAllProducts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertExpense() {
        viewModelScope.launch {
            cost = costText.toDouble()
            payment = paymentText.toDouble()

            expenseRepository.insert(
                ExpenseEntity(
                    clientId = clientId,
                    productId = productId,
                    description = description,
                    cost = cost,
                    payment = payment,
                    date = 1726410880000
                )
            )
            // Clean values
            cleanValues()
        }
    }

    // Field Values
    var clientId by mutableStateOf(0L)
        private set

    var clientName by mutableStateOf("")
        private set

    var productId by mutableLongStateOf(0L)
        private set

    var productName by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var cost by mutableDoubleStateOf(0.0)
        private set

    var costText by mutableStateOf("")
    private set

    var payment by mutableDoubleStateOf(0.0)
        private set

    var paymentText by mutableStateOf("")
    private set

    fun onClientSelected(client: ClientEntity) {
        clientId = client.id
        clientName = "${client.firstName} ${client.lastName}"
    }

    fun onProductSelected(product: ProductEntity) {
        productId = product.id
        productName = product.productName
    }

    fun onDescriptionChanged(newDescription: String) {
        description = newDescription
    }

    fun onCostChanged(newCost: String) {
        costText = newCost
    }

    fun onPaymentChanged(newPayment: String) {
        paymentText = newPayment
    }

    // Dialog Values

    var openDialogClient by mutableStateOf(false)
    private set

    var openDialogProduct by mutableStateOf(false)
    private set

    fun openDialogClient(open: Boolean) {
        openDialogClient = open
    }

    fun onOpenDialogProductChange(open: Boolean) {
        openDialogProduct = open
    }

    private fun cleanValues(){
        clientId = 0L
        clientName = ""
        productId = 0L
        productName = ""
        description = ""
        cost = 0.0
        costText = ""
        payment = 0.0
        paymentText = ""
    }


}