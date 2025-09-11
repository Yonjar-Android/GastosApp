package com.example.gastosapp.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosapp.data.database.entities.ProductEntity
import com.example.gastosapp.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {
    val products: StateFlow<List<ProductEntity>>
    = productRepository.getAllProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertClient(product: ProductEntity){
        viewModelScope.launch {
            productRepository.insertProduct(product)
        }
    }

    fun updateClient(product: ProductEntity){
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }

    fun deleteClient(product: ProductEntity){
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

}