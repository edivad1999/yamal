package com.yamal.feature.search.ui.search.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.MangaForListYamal
import com.yamal.feature.search.api.SearchRepository
import com.yamal.feature.search.api.model.SearchType
import com.yamal.mvi.Presenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@Stable
data class SearchInternalState(
    val query: String = "",
    val searchType: SearchType = SearchType.ANIME,
)

@Stable
data class SearchUiState(
    val query: String = "",
    val searchType: SearchType = SearchType.ANIME,
    val animeResults: Flow<PagingData<AnimeForListYamal>>,
    val mangaResults: Flow<PagingData<MangaForListYamal>>,
)

sealed interface SearchIntent {
    data class UpdateQuery(
        val query: String,
    ) : SearchIntent

    data class ChangeSearchType(
        val type: SearchType,
    ) : SearchIntent

    data object ClearSearch : SearchIntent
}

sealed interface SearchEffect {
    data class NavigateToAnimeDetails(
        val animeId: Int,
    ) : SearchEffect

    data class NavigateToMangaDetails(
        val mangaId: Int,
    ) : SearchEffect
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchPresenter(
    private val searchRepository: SearchRepository,
) : Presenter<SearchInternalState, SearchUiState, SearchIntent, SearchEffect>() {
    private val queryFlow = MutableStateFlow("")
    private val searchTypeFlow = MutableStateFlow(SearchType.ANIME)

    private val animeResults: Flow<PagingData<AnimeForListYamal>> =
        combine(
            queryFlow.debounce(300).distinctUntilChanged(),
            searchTypeFlow,
        ) { query, type ->
            Pair(query, type)
        }.filter { (query, type) ->
            query.length >= 3 && type == SearchType.ANIME
        }.flatMapLatest { (query, _) ->
            Pager(PagingConfig(pageSize = 20)) {
                searchRepository.searchAnime(query)
            }.flow.cachedIn(viewModelScope)
        }

    private val mangaResults: Flow<PagingData<MangaForListYamal>> =
        combine(
            queryFlow.debounce(300).distinctUntilChanged(),
            searchTypeFlow,
        ) { query, type ->
            Pair(query, type)
        }.filter { (query, type) ->
            query.length >= 3 && type == SearchType.MANGA
        }.flatMapLatest { (query, _) ->
            Pager(PagingConfig(pageSize = 20)) {
                searchRepository.searchManga(query)
            }.flow.cachedIn(viewModelScope)
        }

    override fun initialInternalState(): SearchInternalState = SearchInternalState()

    override val state: StateFlow<SearchUiState> =
        combine(
            getInternalState(),
            flowOf(animeResults),
            flowOf(mangaResults),
        ) { internalState, anime, manga ->
            SearchUiState(
                query = internalState.query,
                searchType = internalState.searchType,
                animeResults = anime,
                mangaResults = manga,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue =
                SearchUiState(
                    query = "",
                    searchType = SearchType.ANIME,
                    animeResults = flowOf(PagingData.empty()),
                    mangaResults = flowOf(PagingData.empty()),
                ),
        )

    override fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> {
                updateInternalState { it.copy(query = intent.query) }
                queryFlow.value = intent.query
            }

            is SearchIntent.ChangeSearchType -> {
                updateInternalState { it.copy(searchType = intent.type) }
                searchTypeFlow.value = intent.type
            }

            SearchIntent.ClearSearch -> {
                updateInternalState { it.copy(query = "") }
                queryFlow.value = ""
            }
        }
    }
}
