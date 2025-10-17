package com.example.gastosapp.data.repositories.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gastosapp.data.database.entities.ClientEntity

class FakeClientPagingSource(
    private val clients: List<ClientEntity>
): PagingSource<Int, ClientEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClientEntity> {
        return LoadResult.Page(
            data = clients,
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ClientEntity>): Int? = null

}