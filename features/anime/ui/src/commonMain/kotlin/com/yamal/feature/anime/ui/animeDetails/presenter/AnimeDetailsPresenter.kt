package com.yamal.feature.anime.ui.animeDetails.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Stable
data class AnimeDetailsUi(
    val anime: AnimeForDetailsYamal? = null,
    val error: String? = null,
    val loading: Boolean = false,
    val isUpdatingList: Boolean = false,
    val listUpdateSuccess: Boolean = false,
    val listUpdateError: String? = null,
)

class AnimeDetailsPresenter(
    private val animeRepository: AnimeRepository,
    private val animeId: Int,
) : Presenter<AnimeDetailsUi, AnimeDetailsUi, AnimeDetailsIntent, Nothing>() {
    private fun getAnimeDetails() {
        updateInternalState {
            it.copy(
                loading = true,
            )
        }
        viewModelScope.launch {
            animeRepository.getAnimeDetails(animeId).fold(
                ifLeft = { apiError ->
                    updateInternalState {
                        it.copy(
                            error = apiError,
                            loading = false,
                        )
                    }
                },
                ifRight = { animeDetails ->
                    updateInternalState {
                        it.copy(
                            anime = animeDetails,
                            loading = false,
                        )
                    }
                },
            )
        }
    }

    private fun updateListStatus(
        status: UserListStatusYamal?,
        score: Int,
        episodesWatched: Int,
    ) {
        updateInternalState {
            it.copy(
                isUpdatingList = true,
                listUpdateError = null,
            )
        }
        viewModelScope.launch {
            animeRepository
                .updateAnimeListStatus(
                    animeId = animeId,
                    status = status,
                    score = if (score > 0) score else null,
                    numWatchedEpisodes = episodesWatched,
                ).fold(
                    ifLeft = { error ->
                        updateInternalState {
                            it.copy(
                                isUpdatingList = false,
                                listUpdateError = error,
                            )
                        }
                    },
                    ifRight = { updatedStatus ->
                        updateInternalState { currentState ->
                            currentState.copy(
                                isUpdatingList = false,
                                listUpdateSuccess = true,
                                anime = currentState.anime?.copy(myListStatus = updatedStatus),
                            )
                        }
                    },
                )
        }
    }

    private fun deleteFromList() {
        updateInternalState {
            it.copy(
                isUpdatingList = true,
                listUpdateError = null,
            )
        }
        viewModelScope.launch {
            animeRepository.deleteAnimeListStatus(animeId).fold(
                ifLeft = { error ->
                    updateInternalState {
                        it.copy(
                            isUpdatingList = false,
                            listUpdateError = error,
                        )
                    }
                },
                ifRight = {
                    updateInternalState { currentState ->
                        currentState.copy(
                            isUpdatingList = false,
                            listUpdateSuccess = true,
                            anime = currentState.anime?.copy(myListStatus = null),
                        )
                    }
                },
            )
        }
    }

    override fun initialInternalState(): AnimeDetailsUi = AnimeDetailsUi()

    override val state: StateFlow<AnimeDetailsUi> = getInternalState()

    override fun processIntent(intent: AnimeDetailsIntent) {
        when (intent) {
            AnimeDetailsIntent.Refresh -> {
                getAnimeDetails()
            }

            is AnimeDetailsIntent.UpdateListStatus -> {
                updateListStatus(intent.status, intent.score, intent.episodesWatched)
            }

            AnimeDetailsIntent.DeleteFromList -> {
                deleteFromList()
            }

            AnimeDetailsIntent.ClearListUpdateState -> {
                updateInternalState {
                    it.copy(
                        listUpdateSuccess = false,
                        listUpdateError = null,
                    )
                }
            }
        }
    }
}

sealed interface AnimeDetailsIntent {
    data object Refresh : AnimeDetailsIntent

    data class UpdateListStatus(
        val status: UserListStatusYamal?,
        val score: Int,
        val episodesWatched: Int,
    ) : AnimeDetailsIntent

    data object DeleteFromList : AnimeDetailsIntent

    data object ClearListUpdateState : AnimeDetailsIntent
}
