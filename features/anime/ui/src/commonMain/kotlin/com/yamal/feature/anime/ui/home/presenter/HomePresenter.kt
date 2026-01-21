package com.yamal.feature.anime.ui.home.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.mvi.Presenter
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Stable
data class HomeState(
    val heroAnimes: List<AnimeForListYamal> = emptyList(),
    val trendingAnimes: List<AnimeForListYamal> = emptyList(),
    val topAnimes: List<AnimeForListYamal> = emptyList(),
    val upcomingAnimes: List<AnimeForListYamal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface HomeIntent {
    data object LoadData : HomeIntent

    data object Refresh : HomeIntent
}

class HomePresenter(
    private val animeRepository: AnimeRepository,
) : Presenter<HomeState, HomeState, HomeIntent, Nothing>() {
    init {
        loadData()
    }

    private fun loadData() {
        updateInternalState { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            // Fetch all data in parallel
            val suggestionsDeferred = async { animeRepository.getAnimeSuggestions(limit = 5) }
            val trendingDeferred = async { animeRepository.getTrendingAnime(limit = 10) }
            val topDeferred = async { animeRepository.getTopAnime(limit = 10) }
            val upcomingDeferred = async { animeRepository.getUpcomingAnime(limit = 5) }

            val suggestions = suggestionsDeferred.await()
            val trending = trendingDeferred.await()
            val top = topDeferred.await()
            val upcoming = upcomingDeferred.await()

            // Update state with results
            updateInternalState { currentState ->
                var newState = currentState.copy(isLoading = false)

                suggestions.fold(
                    ifLeft = { /* Keep empty or show error */ },
                    ifRight = { newState = newState.copy(heroAnimes = it) },
                )

                trending.fold(
                    ifLeft = { /* Keep empty or show error */ },
                    ifRight = { newState = newState.copy(trendingAnimes = it) },
                )

                top.fold(
                    ifLeft = { /* Keep empty or show error */ },
                    ifRight = { newState = newState.copy(topAnimes = it) },
                )

                upcoming.fold(
                    ifLeft = { /* Keep empty or show error */ },
                    ifRight = { newState = newState.copy(upcomingAnimes = it) },
                )

                // If all failed, show error
                if (suggestions.isLeft() && trending.isLeft() && top.isLeft() && upcoming.isLeft()) {
                    newState = newState.copy(error = "Failed to load data. Please try again.")
                }

                newState
            }
        }
    }

    override fun initialInternalState(): HomeState = HomeState()

    override val state: StateFlow<HomeState> = getInternalState()

    override fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadData, HomeIntent.Refresh -> loadData()
        }
    }
}
