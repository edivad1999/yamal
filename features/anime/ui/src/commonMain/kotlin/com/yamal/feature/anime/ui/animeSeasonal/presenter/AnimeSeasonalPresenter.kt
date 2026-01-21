package com.yamal.feature.anime.ui.animeSeasonal.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.yamal.designSystem.components.daySelector.Day
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeSeasonYamal
import com.yamal.feature.anime.api.model.SeasonYamal
import com.yamal.feature.anime.ui.core.presenterPager
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlinx.datetime.DayOfWeek as KotlinDayOfWeek

/**
 * Represents days of the week for broadcast filtering.
 */
enum class DayOfWeek(
    val displayName: String,
    val abbreviation: String,
    val apiValue: String,
) {
    Monday("Monday", "MON", "monday"),
    Tuesday("Tuesday", "TUE", "tuesday"),
    Wednesday("Wednesday", "WED", "wednesday"),
    Thursday("Thursday", "THU", "thursday"),
    Friday("Friday", "FRI", "friday"),
    Saturday("Saturday", "SAT", "saturday"),
    Sunday("Sunday", "SUN", "sunday"),
    ;

    companion object {
        fun fromApiValue(value: String?): DayOfWeek? = entries.find { it.apiValue.equals(value, ignoreCase = true) }

        fun fromIndex(index: Int): DayOfWeek = entries[index]
    }
}

enum class ViewMode {
    List,
    Grid,
}

enum class MediaTypeFilter {
    All,
    TV,
    ONA,
    Movie,
    Special,
    OVA,
}

enum class SeriesFilter {
    All,
    NewEpisodesOnly,
    ContinuingSeries,
}

@Stable
data class AnimeSeasonalUi(
    val animeSeason: List<Pair<AnimeSeasonYamal, Flow<PagingData<AnimeForListYamal>>>>,
    val selectedDayIndex: Int = getCurrentDayIndex(),
    val weekDays: List<Day> = generateCurrentWeekDays(),
    val viewMode: ViewMode = ViewMode.List,
    val selectedSeasonIndex: Int = calculateCurrentSeasonIndex(5),
    val mediaTypeFilter: MediaTypeFilter = MediaTypeFilter.All,
    val seriesFilter: SeriesFilter = SeriesFilter.All,
)

class AnimeSeasonalPresenter(
    private val animeRepository: AnimeRepository,
) : Presenter<AnimeSeasonalUi, AnimeSeasonalUi, AnimeSeasonalIntent, Nothing>() {
    private val animeSeasons =
        AnimeSeasonYamal.generateAnimeSeasons(5).map { season ->
            season to
                presenterPager(viewModelScope) {
                    animeRepository.getSeasonal(season.season, season.year)
                }
        }

    override fun initialInternalState(): AnimeSeasonalUi = AnimeSeasonalUi(animeSeasons)

    override val state: StateFlow<AnimeSeasonalUi> = getInternalState()

    override fun processIntent(intent: AnimeSeasonalIntent) {
        when (intent) {
            is AnimeSeasonalIntent.SelectDayIndex -> {
                updateInternalState { it.copy(selectedDayIndex = intent.index) }
            }

            is AnimeSeasonalIntent.SelectSeasonIndex -> {
                updateInternalState { it.copy(selectedSeasonIndex = intent.index) }
            }

            is AnimeSeasonalIntent.SetViewMode -> {
                updateInternalState { it.copy(viewMode = intent.mode) }
            }

            is AnimeSeasonalIntent.SetMediaTypeFilter -> {
                updateInternalState { it.copy(mediaTypeFilter = intent.filter) }
            }

            is AnimeSeasonalIntent.SetSeriesFilter -> {
                updateInternalState { it.copy(seriesFilter = intent.filter) }
            }
        }
    }
}

sealed interface AnimeSeasonalIntent {
    data class SelectDayIndex(
        val index: Int,
    ) : AnimeSeasonalIntent

    data class SelectSeasonIndex(
        val index: Int,
    ) : AnimeSeasonalIntent

    data class SetViewMode(
        val mode: ViewMode,
    ) : AnimeSeasonalIntent

    data class SetMediaTypeFilter(
        val filter: MediaTypeFilter,
    ) : AnimeSeasonalIntent

    data class SetSeriesFilter(
        val filter: SeriesFilter,
    ) : AnimeSeasonalIntent
}

fun AnimeSeasonYamal.Companion.generateAnimeSeasons(years: Int): List<AnimeSeasonYamal> {
    val currentYear =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .year
    val animeSeasons =
        (currentYear - years..currentYear).flatMap { year ->
            SeasonYamal.entries.map {
                AnimeSeasonYamal(year = year.toString(), it)
            }
        }
    return animeSeasons
}

/**
 * Get the current day index (0 = Monday, 6 = Sunday)
 */
fun getCurrentDayIndex(): Int {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return when (today.dayOfWeek) {
        KotlinDayOfWeek.MONDAY -> 0
        KotlinDayOfWeek.TUESDAY -> 1
        KotlinDayOfWeek.WEDNESDAY -> 2
        KotlinDayOfWeek.THURSDAY -> 3
        KotlinDayOfWeek.FRIDAY -> 4
        KotlinDayOfWeek.SATURDAY -> 5
        KotlinDayOfWeek.SUNDAY -> 6
    }
}

/**
 * Generate the current week's days starting from Monday.
 */
fun generateCurrentWeekDays(): List<Day> {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val todayDate = today.date

    // Find Monday of current week
    val daysFromMonday = getCurrentDayIndex()
    val monday = todayDate.minus(daysFromMonday, DateTimeUnit.DAY)

    return DayOfWeek.entries.mapIndexed { index, dayOfWeek ->
        val date = monday.plus(index, DateTimeUnit.DAY)
        @Suppress("DEPRECATION")
        Day(
            dayOfWeek = dayOfWeek.abbreviation,
            dayOfMonth = date.dayOfMonth,
        )
    }
}

/**
 * Calculate the index of the current season in the generated anime seasons list.
 * @param yearsBack number of years to look back
 * @return index of the current season in the list
 */
fun calculateCurrentSeasonIndex(yearsBack: Int): Int {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentYear = today.year

    @Suppress("DEPRECATION")
    val currentMonth = today.monthNumber

    // Determine current season based on month
    val currentSeason =
        when (currentMonth) {
            in 1..3 -> SeasonYamal.Winter
            in 4..6 -> SeasonYamal.Spring
            in 7..9 -> SeasonYamal.Summer
            else -> SeasonYamal.Fall
        }

    // Calculate index: seasons are ordered from (currentYear - yearsBack) to currentYear
    // Each year has 4 seasons: Winter(0), Spring(1), Summer(2), Fall(3)
    val yearsFromStart = currentYear - (currentYear - yearsBack)
    val seasonIndex = currentSeason.ordinal

    return yearsFromStart * 4 + seasonIndex
}
