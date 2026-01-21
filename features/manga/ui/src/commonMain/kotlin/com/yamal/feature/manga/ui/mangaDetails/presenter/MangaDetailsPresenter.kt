package com.yamal.feature.manga.ui.mangaDetails.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.yamal.feature.manga.api.MangaRepository
import com.yamal.feature.manga.api.model.MangaForDetailsYamal
import com.yamal.feature.manga.api.model.UserMangaListStatusYamal
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Stable
data class MangaDetailsUi(
    val manga: MangaForDetailsYamal? = null,
    val error: String? = null,
    val loading: Boolean = false,
    val isUpdatingList: Boolean = false,
    val listUpdateSuccess: Boolean = false,
    val listUpdateError: String? = null,
)

class MangaDetailsPresenter(
    private val mangaRepository: MangaRepository,
    private val mangaId: Int,
) : Presenter<MangaDetailsUi, MangaDetailsUi, MangaDetailsIntent, Nothing>() {
    private fun getMangaDetails() {
        updateInternalState {
            it.copy(
                loading = true,
            )
        }
        viewModelScope.launch {
            mangaRepository.getMangaDetails(mangaId).fold(
                ifLeft = { apiError ->
                    updateInternalState {
                        it.copy(
                            error = apiError,
                            loading = false,
                        )
                    }
                },
                ifRight = { mangaDetails ->
                    updateInternalState {
                        it.copy(
                            manga = mangaDetails,
                            loading = false,
                        )
                    }
                },
            )
        }
    }

    private fun updateListStatus(
        status: UserMangaListStatusYamal?,
        score: Int,
        chaptersRead: Int,
        volumesRead: Int,
    ) {
        updateInternalState {
            it.copy(
                isUpdatingList = true,
                listUpdateError = null,
            )
        }
        viewModelScope.launch {
            mangaRepository
                .updateMangaListStatus(
                    mangaId = mangaId,
                    status = status,
                    score = if (score > 0) score else null,
                    numChaptersRead = chaptersRead,
                    numVolumesRead = volumesRead,
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
                                manga = currentState.manga?.copy(myListStatus = updatedStatus),
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
            mangaRepository.deleteMangaListStatus(mangaId).fold(
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
                            manga = currentState.manga?.copy(myListStatus = null),
                        )
                    }
                },
            )
        }
    }

    override fun initialInternalState(): MangaDetailsUi = MangaDetailsUi()

    override val state: StateFlow<MangaDetailsUi> = getInternalState()

    override fun processIntent(intent: MangaDetailsIntent) {
        when (intent) {
            MangaDetailsIntent.Refresh -> {
                getMangaDetails()
            }

            is MangaDetailsIntent.UpdateListStatus -> {
                updateListStatus(intent.status, intent.score, intent.chaptersRead, intent.volumesRead)
            }

            MangaDetailsIntent.DeleteFromList -> {
                deleteFromList()
            }

            MangaDetailsIntent.ClearListUpdateState -> {
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

sealed interface MangaDetailsIntent {
    data object Refresh : MangaDetailsIntent

    data class UpdateListStatus(
        val status: UserMangaListStatusYamal?,
        val score: Int,
        val chaptersRead: Int,
        val volumesRead: Int,
    ) : MangaDetailsIntent

    data object DeleteFromList : MangaDetailsIntent

    data object ClearListUpdateState : MangaDetailsIntent
}
