package com.yamal.presentation.home.presenter

import androidx.compose.runtime.Stable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeRanking
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

@Stable
data class HomeState(
    val animeRanking: Flow<PagingData<AnimeRanking>>,
)

class HomePresenter(private val animeRepository: AnimeRepository) : Presenter<HomeState, HomeState, Nothing, Nothing>() {
    private val animeRanking =
        Pager(PagingConfig(10), pagingSourceFactory = {
            animeRepository.getRanking()
        }).flow.cachedIn(screenModelScope)

    override fun initialInternalState(): HomeState = HomeState(animeRanking)

    override val state: StateFlow<HomeState> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}
