package com.yamal.presentation.home.presenter

import androidx.compose.runtime.Stable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.feature.anime.api.model.Season
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Stable
data class HomeState(
    val genericAnime: Flow<PagingData<GenericAnime>>,
    val animeSeason: List<Pair<AnimeSeason, Flow<PagingData<GenericAnime>>>>,
)

class HomePresenter(private val animeRepository: AnimeRepository) : Presenter<HomeState, HomeState, Nothing, Nothing>() {
    private val animeRanking =
        Pager(PagingConfig(10), pagingSourceFactory = {
            animeRepository.getRanking()
        }).flow.cachedIn(screenModelScope)

    private val animeSeasons =
        AnimeSeason.generateAnimeSeasons(10).map {
            it to
                Pager(PagingConfig(10), pagingSourceFactory = {
                    animeRepository.getSeasonal(it.season, it.year)
                }).flow.cachedIn(screenModelScope)
        }

    override fun initialInternalState(): HomeState = HomeState(animeRanking, animeSeasons)

    override val state: StateFlow<HomeState> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}

data class AnimeSeason(val year: String, val season: Season) {
    companion object {
        fun generateAnimeSeasons(years: Int): List<AnimeSeason> {
            val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
            val animeSeasons =
                (currentYear - years..currentYear).map { year ->
                    Season.entries.map {
                        AnimeSeason(year = year.toString(), it)
                    }
                }.flatten()
            return animeSeasons
        }
    }
}
