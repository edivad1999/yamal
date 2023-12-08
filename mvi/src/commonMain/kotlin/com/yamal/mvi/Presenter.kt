package com.yamal.mvi

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.Flow

interface Presenter<State, Effect> : ScreenModel {

    val effects: Flow<Effect>

    @Composable
    fun present(): State
}
