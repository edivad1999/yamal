package com.yamal.presentation.animeSeasonal.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeSeason
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.feature.anime.api.model.Season
import com.yamal.mvi.Presenter
import com.yamal.presentation.core.presenterPager
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
        AnimeSeason.generateAnimeSeasons(5).map {
            it to animeRepository.getSeasonal(it.season, it.year).presenterPager(viewModelScope)
        }

    override fun initialInternalState(): AnimeSeasonalUi = AnimeSeasonalUi(animeSeasons)

    override val state: StateFlow<AnimeSeasonalUi> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}

fun AnimeSeason.Companion.generateAnimeSeasons(years: Int): List<AnimeSeason> {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val animeSeasons =
        (currentYear - years..currentYear).map { year ->
            Season.entries.map {
                AnimeSeason(year = year.toString(), it)
            }
        }.flatten()
    return animeSeasons
}
