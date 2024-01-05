package core

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

val CombinedLoadStates.isAnyLoading: Boolean
    get() = append is LoadState.Loading || refresh is LoadState.Loading || prepend is LoadState.Loading
