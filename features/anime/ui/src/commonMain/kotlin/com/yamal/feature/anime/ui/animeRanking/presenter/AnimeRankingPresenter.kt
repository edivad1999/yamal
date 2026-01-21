package com.yamal.feature.anime.ui.animeRanking.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.ui.core.presenterPager
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class AnimeRankingUi(
    val ranking: Flow<PagingData<AnimeForListYamal>>,
)

class AnimeRankingPresenter(
    private val animeRepository: AnimeRepository,
) : Presenter<AnimeRankingUi, AnimeRankingUi, Nothing, Nothing>() {
    private val animeRanking: Flow<PagingData<AnimeForListYamal>> =
        presenterPager(viewModelScope) {
            animeRepository.getRanking()
        }

    override fun initialInternalState(): AnimeRankingUi = AnimeRankingUi(animeRanking)

    override val state: StateFlow<AnimeRankingUi> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}
