package com.yamal.feature.anime.ui.home.presenter

import androidx.compose.runtime.Stable
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.StateFlow

@Stable
class HomeState

class HomePresenter(
    private val animeRepository: AnimeRepository,
) : Presenter<HomeState, HomeState, Nothing, Nothing>() {
    override fun initialInternalState(): HomeState = HomeState()

    override val state: StateFlow<HomeState> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}
