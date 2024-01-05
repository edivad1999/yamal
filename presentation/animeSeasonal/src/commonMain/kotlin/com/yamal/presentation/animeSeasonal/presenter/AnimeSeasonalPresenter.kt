package com.yamal.presentation.animeSeasonal.presenter

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
data class AnimeSeasonalUi(
    val animeSeason: List<Pair<AnimeSeason, Flow<PagingData<GenericAnime>>>>,
)

class AnimeSeasonalPresenter(private val animeRepository: AnimeRepository) : Presenter<AnimeSeasonalUi, AnimeSeasonalUi, Nothing, Nothing>() {
    private val animeSeasons =
        AnimeSeason.generateAnimeSeasons(100).map {
            it to
                Pager(PagingConfig(10), pagingSourceFactory = {
                    animeRepository.getSeasonal(it.season, it.year)
                }).flow.cachedIn(screenModelScope)
        }

    override fun initialInternalState(): AnimeSeasonalUi = AnimeSeasonalUi(animeSeasons)

    override val state: StateFlow<AnimeSeasonalUi> = getInternalState()

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
