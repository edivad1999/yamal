package com.yamal.feature.anime.ui.core

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope

/**
 * Creates a pager flow from a PagingSource factory function.
 * Use this with repository methods that return PagingSource.
 */
fun <R : Any> presenterPager(
    coroutineScope: CoroutineScope,
    pagingSourceFactory: () -> PagingSource<Int, R>,
) = Pager(
    config = PagingConfig(pageSize = 10),
    pagingSourceFactory = pagingSourceFactory,
).flow.cachedIn(coroutineScope)
