package com.yamal.platform.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.Either
import com.yamal.platform.network.api.ApiError
import com.yamal.platform.network.api.model.PagingData

class MalPagingSource<T : Any, R : Any>(
    private val apiCall: suspend (pageSize: Int, offset: Int) -> Either<ApiError, PagingData<T>>,
    private val map: (T) -> R,
) : PagingSource<Int, R>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, R> {
        val offset = params.key ?: 0
        val pageSize = params.loadSize

        val response = apiCall(pageSize, offset)
        return response.fold(ifLeft = {
            LoadResult.Error(it.throwable)
        }, ifRight = {
            LoadResult.Page(
                data =
                    it.data.map { networkType ->
                        map(networkType)
                    },
                prevKey = if (it.paging.previous == null) null else (offset - pageSize).coerceAtLeast(0),
                nextKey = if (it.paging.next == null) null else offset + pageSize,
            )
        })
    }

    override fun getRefreshKey(state: PagingState<Int, R>): Int? = state.anchorPosition
}
