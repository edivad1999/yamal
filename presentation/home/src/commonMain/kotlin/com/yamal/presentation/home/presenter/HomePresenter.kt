package com.yamal.presentation.home.presenter

import androidx.compose.runtime.Composable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeRanking
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

object HomeScreen {

    data class HomeState(
        val animeRanking: Flow<PagingData<AnimeRanking>>,
        val event: (Nothing) -> Unit,
    )
}

class HomePresenter(private val animeRepository: AnimeRepository) : Presenter<HomeScreen.HomeState, Nothing> {

    override val effects: Flow<Nothing> = flowOf()

    private val animeRanking = Pager(PagingConfig(10), pagingSourceFactory = {
        animeRepository.getRanking()
    }).flow.cachedIn(screenModelScope)


    @Composable override fun present(): HomeScreen.HomeState {
        return HomeScreen.HomeState(animeRanking) { _ ->

        }
    }
}
