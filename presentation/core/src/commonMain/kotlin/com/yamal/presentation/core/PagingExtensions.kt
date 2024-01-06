package com.yamal.presentation.core

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yamal.feature.core.MalPagingSource
import kotlinx.coroutines.CoroutineScope

fun <T : Any, R : Any> MalPagingSource<T, R>.presenterPager(coroutineScope: CoroutineScope) =
    Pager(PagingConfig(10), pagingSourceFactory = {
        this
    }).flow.cachedIn(coroutineScope)
