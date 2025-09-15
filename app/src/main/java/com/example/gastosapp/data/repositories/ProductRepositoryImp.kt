package com.example.gastosapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    override fun getAllProducts(): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 20),
            pagingSourceFactory = {
                productDao.getAllProducts()
            }
        ).flow
    }
}