package com.example.gastosapp.data.repositories.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gastosapp.data.database.entities.ExpenseWithDetails

class FakeExpensePagingSource(
    private val expenses: List<ExpenseWithDetails>
) : PagingSource<Int, ExpenseWithDetails>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExpenseWithDetails> {
        return LoadResult.Page(
            data = expenses,
            prevKey = null,
            nextKey = null
        )

    }

    override fun getRefreshKey(state: PagingState<Int, ExpenseWithDetails>): Int? = null

}