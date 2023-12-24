package com.yamal.presentation.home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

object CounterScreen {

    data class CounterState(
        val animeRanking: List<AnimeRanking> = emptyList(),
        val error: String? = null,
        val event: (CounterIntent) -> Unit,
    )

    sealed interface CounterIntent {
        data object Increment : CounterIntent
        data object Decrement : CounterIntent
    }
}

class CounterPresenter(private val animeRepository: AnimeRepository) : Presenter<CounterScreen.CounterState, Nothing> {

    override val effects: Flow<Nothing> = flowOf()

    @Composable
    override fun present(): CounterScreen.CounterState {
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

        return CounterScreen.CounterState(animeRanking, error) { event ->
            when (event) {
                CounterScreen.CounterIntent.Increment -> screenModelScope.launch {
                }

                CounterScreen.CounterIntent.Decrement -> screenModelScope.launch {
                }
            }
        }
    }
}
