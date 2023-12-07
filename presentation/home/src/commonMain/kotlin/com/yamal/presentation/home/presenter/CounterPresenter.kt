package com.yamal.presentation.home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.yamal.feature.home.api.HomeRepository
import com.yamal.feature.home.api.model.CounterModel
import kotlinx.coroutines.launch

object CounterScreen {

    data class CounterState(
        val count: Int,
        val event: (CounterEvent) -> Unit,
    ) : CircuitUiState

    sealed interface CounterEvent : CircuitUiEvent {
        data object Increment : CounterEvent
        data object Decrement : CounterEvent
    }
}

class CounterPresenter(private val homeRepository: HomeRepository) :
    Presenter<CounterScreen.CounterState> {
    @Composable
    override fun present(): CounterScreen.CounterState {
        val counter by homeRepository.counterFlow().collectAsState(CounterModel(0))

        val coroutineScope = rememberCoroutineScope()
        return CounterScreen.CounterState(counter.number) { event ->
            when (event) {
                CounterScreen.CounterEvent.Increment -> coroutineScope.launch {
                    homeRepository.setCounter(counter.number + 1)
                }

                CounterScreen.CounterEvent.Decrement -> coroutineScope.launch {
                    homeRepository.setCounter(counter.number - 1)
                }
            }
        }
    }
}
