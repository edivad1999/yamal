package com.yamal.presentation.home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.home.api.HomeRepository
import com.yamal.feature.home.api.model.CounterModel
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

object CounterScreen {

    data class CounterState(
        val count: Int,
        val event: (CounterIntent) -> Unit,
    )

    sealed interface CounterIntent {
        data object Increment : CounterIntent
        data object Decrement : CounterIntent
    }
}

class CounterPresenter(private val homeRepository: HomeRepository) : Presenter<CounterScreen.CounterState, Nothing> {

    override val effects: Flow<Nothing> = flowOf()

    @Composable
    override fun present(): CounterScreen.CounterState {
        val counter by homeRepository.counterFlow().collectAsState(CounterModel(0))

        return CounterScreen.CounterState(counter.number) { event ->
            when (event) {
                CounterScreen.CounterIntent.Increment -> screenModelScope.launch {
                    homeRepository.setCounter(counter.number + 1)
                }

                CounterScreen.CounterIntent.Decrement -> screenModelScope.launch {
                    homeRepository.setCounter(counter.number - 1)
                }
            }
        }
    }
}
