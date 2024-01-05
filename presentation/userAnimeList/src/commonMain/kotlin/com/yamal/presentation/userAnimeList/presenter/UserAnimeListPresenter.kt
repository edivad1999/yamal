package com.yamal.presentation.userAnimeList.presenter

import androidx.compose.runtime.Stable
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.StateFlow

@Stable
class UserAnimeListUi()

class UserAnimeListPresenter(private val animeRepository: AnimeRepository) : Presenter<UserAnimeListUi, UserAnimeListUi, Nothing, Nothing>() {
    override fun initialInternalState(): UserAnimeListUi = UserAnimeListUi()

    override val state: StateFlow<UserAnimeListUi> = getInternalState()

    override fun processIntent(intent: Nothing) {}
}
