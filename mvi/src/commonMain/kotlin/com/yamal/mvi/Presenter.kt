package com.yamal.mvi

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class Presenter<InternalState, UiState, Intent, Effect> : ScreenModel {
    abstract fun initialInternalState(): InternalState

    private val internalState: MutableStateFlow<InternalState> by lazy {
        MutableStateFlow(initialInternalState())
    }

    fun getInternalState() = internalState.asStateFlow()

    fun updateInternalState(reduce: (InternalState) -> InternalState) {
        internalState.update {
            reduce(it)
        }
    }

    abstract val state: StateFlow<UiState>

    private val effects: MutableSharedFlow<Effect> = MutableSharedFlow()

    fun launchEffect(effectBlock: suspend () -> Effect) {
        screenModelScope.launch {
            effects.emit(effectBlock())
        }
    }

    fun getEffects() = effects.asSharedFlow()

    abstract fun processIntent(intent: Intent)
}
