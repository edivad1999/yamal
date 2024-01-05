package com.yamal.presentation.animeRanking.presenter

import androidx.compose.runtime.Stable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class AnimeRankingUi(
    val ranking: Flow<PagingData<GenericAnime>>,
)

class AnimeRankingPresenter(private val animeRepository: AnimeRepository) : Presenter<AnimeRankingUi, AnimeRankingUi, Nothing, Nothing>() {
    private val animeRanking =
        Pager(PagingConfig(10), pagingSourceFactory = {
            animeRepository.getRanking()
        }).flow.cachedIn(screenModelScope)

    override fun initialInternalState(): AnimeRankingUi = AnimeRankingUi(animeRanking)

    override val state: StateFlow<AnimeRankingUi> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}
