package com.example.gastosapp.presentation.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {
    val products: Flow<PagingData<ProductEntity>>
    = productRepository.getAllProducts()
        .cachedIn(viewModelScope)

    fun insertProduct(){
        viewModelScope.launch {
            productRepository.insertProduct(
                ProductEntity(
                    productName = productName
                )
            )
        }
    }

    fun updateProduct(){
        viewModelScope.launch {
            productRepository.updateProduct(
                productToEdit!!.copy(
                    productName = productNameEdit
                )
            )
        }
    }

    fun deleteProduct(){
        viewModelScope.launch {
            productRepository.deleteProduct(productToEdit!!)
        }
    }

    // Fields

    var productName by mutableStateOf("")
        private set

    fun onProductNameChange(value: String){
        productName = value
    }

    var productToEdit by mutableStateOf<ProductEntity?>(null)
    private set

    // Dialog values

    var showEditDialog by mutableStateOf(false)
    private set

    var productNameEdit by mutableStateOf("")
    private set

    fun onProductNameEditChange(value: String){
        productNameEdit = value
    }

    fun openEditDialog(product: ProductEntity){
        productNameEdit = product.productName
        showEditDialog = true
        productToEdit = product
    }

    fun closeEditDialog(){
        showEditDialog = false
        productToEdit = null
    }

    var showDeleteDialog by mutableStateOf(false)
        private set

    fun openDeleteDialog(product: ProductEntity){
        showDeleteDialog = true
        productToEdit = product
    }

    fun closeDeleteDialog(){
        showDeleteDialog = false
        productToEdit = null
    }
}