package com.example.gastosapp.data.repositories

import com.example.gastosapp.data.database.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insertProduct(product: ProductEntity)
    suspend fun updateProduct(product: ProductEntity)
    suspend fun deleteProduct(product: ProductEntity)
    fun getAllProducts(): Flow<List<ProductEntity>>

}