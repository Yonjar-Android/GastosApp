package com.example.gastosapp.data.repositories

import com.example.gastosapp.data.database.dao.ProductDao
import com.example.gastosapp.data.database.entities.ProductEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImp @Inject constructor(
    private val productDao: ProductDao
): ProductRepository {
    override suspend fun insertProduct(product: ProductEntity) {
        productDao.insert(product)
    }

    override suspend fun updateProduct(product: ProductEntity) {
        productDao.update(product)
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        productDao.delete(product)
    }

    override fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }
}