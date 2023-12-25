package com.yamal.presentation.home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeRanking
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

object HomeScreen {

    data class HomeState(
        val animeRanking: List<AnimeRanking> = emptyList(),
        val error: String? = null,
        val event: (CounterIntent) -> Unit,
    )

    sealed interface CounterIntent {
        data object Increment : CounterIntent
        data object Decrement : CounterIntent
    }
}

class HomePresenter(private val animeRepository: AnimeRepository) : Presenter<HomeScreen.HomeState, Nothing> {

    override val effects: Flow<Nothing> = flowOf()

    @Composable
    override fun present(): HomeScreen.HomeState {
        var animeRanking: List<AnimeRanking> by remember { mutableStateOf(emptyList()) }
        var error: String? by remember { mutableStateOf(null) }
        LaunchedEffect(Unit) {
            animeRepository.getRanking().onLeft {
                error = it
            }.onRight {
                error = null
                animeRanking = it
            }
        }

        return HomeScreen.HomeState(animeRanking, error) { event ->
            when (event) {
                HomeScreen.CounterIntent.Increment -> screenModelScope.launch {
                }

                HomeScreen.CounterIntent.Decrement -> screenModelScope.launch {
                }
            }
        }
    }
}
