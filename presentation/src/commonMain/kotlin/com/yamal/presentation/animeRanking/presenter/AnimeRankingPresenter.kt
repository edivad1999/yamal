package com.yamal.presentation.animeRanking.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.mvi.Presenter
import com.yamal.presentation.core.presenterPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class AnimeRankingUi(
    val ranking: Flow<PagingData<GenericAnime>>,
)

class AnimeRankingPresenter(private val animeRepository: AnimeRepository) : Presenter<AnimeRankingUi, AnimeRankingUi, Nothing, Nothing>() {

    private val animeRanking: Flow<PagingData<GenericAnime>> = animeRepository.getRanking().presenterPager(viewModelScope)

    override fun initialInternalState(): AnimeRankingUi = AnimeRankingUi(animeRanking)

    override val state: StateFlow<AnimeRankingUi> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}
