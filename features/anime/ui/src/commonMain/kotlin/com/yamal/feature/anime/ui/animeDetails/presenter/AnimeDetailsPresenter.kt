package com.yamal.feature.anime.ui.animeDetails.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeDetails
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Stable
data class AnimeDetailsUi(
    val anime: AnimeDetails? = null,
    val error: String? = null,
    val loading: Boolean = false,
)

class AnimeDetailsPresenter(
    private val animeRepository: AnimeRepository,
    private val animeId: Int,
) : Presenter<AnimeDetailsUi, AnimeDetailsUi, AnimeDetailsIntent, Nothing>() {

    private fun getAnimeDetails() {
        updateInternalState {
            it.copy(
                loading = true
            )
        }
        viewModelScope.launch {
            animeRepository.getAnimeDetails(animeId).fold(
                ifLeft = { apiError ->
                    updateInternalState {
                        it.copy(
                            error = apiError,
                            loading = false
                        )
                    }
                }, ifRight = { animeDetails ->
                updateInternalState {
                    it.copy(
                        anime = animeDetails,
                        loading = false
                    )
                }
            })
        }
    }

    override fun initialInternalState(): AnimeDetailsUi = AnimeDetailsUi()

    override val state: StateFlow<AnimeDetailsUi> = getInternalState()

    override fun processIntent(intent: AnimeDetailsIntent) {
        when (intent) {
            AnimeDetailsIntent.Refresh -> {
                getAnimeDetails()
            }
        }
    }
}

sealed interface AnimeDetailsIntent {

    object Refresh : AnimeDetailsIntent
}
