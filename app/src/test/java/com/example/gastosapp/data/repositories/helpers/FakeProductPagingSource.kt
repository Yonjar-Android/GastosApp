package com.example.gastosapp.data.repositories.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gastosapp.data.database.entities.ProductEntity

class FakeProductPagingSource(
private val products: List<ProductEntity>
): PagingSource<Int, ProductEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductEntity> {
        return LoadResult.Page(
            data = products,
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ProductEntity>): Int? = null
}