package com.yamal.platform.datasource.implementation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yamal.platform.jikan.api.JikanRateLimiter
import com.yamal.platform.jikan.api.model.pagination.JikanPaginatedResponse

/**
 * PagingSource for Jikan API which uses page-based pagination.
 */
class JikanPagingSource<T : Any, R : Any>(
    private val rateLimiter: JikanRateLimiter,
    private val apiCall: suspend (page: Int, limit: Int) -> JikanPaginatedResponse<T>,
    private val mapper: (T) -> R,
) : PagingSource<Int, R>() {
    companion object {
        /** Jikan API maximum items per page */
        const val MAX_PAGE_SIZE = 25
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, R> {
        val page = params.key ?: 1
        // Jikan API has a max limit of 25 items per page
        val limit = params.loadSize.coerceAtMost(MAX_PAGE_SIZE)

        return try {
            rateLimiter.acquire()
            val response = apiCall(page, limit)

            LoadResult.Page(
                data = response.data.map(mapper),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.pagination.hasNextPage) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, R>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}
