package com.yamal.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class Presenter<InternalState, UiState, Intent, Effect> : ViewModel() {
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
        viewModelScope.launch {
            effects.emit(effectBlock())
        }
    }

    fun getEffects() = effects.asSharedFlow()

    abstract fun processIntent(intent: Intent)
}
