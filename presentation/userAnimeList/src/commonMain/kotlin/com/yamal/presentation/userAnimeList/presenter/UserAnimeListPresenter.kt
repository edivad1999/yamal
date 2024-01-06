package com.yamal.presentation.userAnimeList.presenter

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.feature.anime.api.model.UserListStatus
import com.yamal.feature.login.api.LoginRepository
import com.yamal.mvi.Presenter
import com.yamal.presentation.core.presenterPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Stable
data class UserAnimeListUi(
    val userAnimeStatus: UserListStatus,
    val isLoggedIn: Boolean = false,
    val userAnimeList: List<StatusAnimeList> = emptyList(),
)

data class StatusAnimeList(
    val status: UserListStatus,
    val list: Flow<PagingData<GenericAnime>>,
)

@Immutable
sealed interface UserAnimeListIntent {
    data class OnSelectStatus(val status: UserListStatus) : UserAnimeListIntent
}

class UserAnimeListPresenter(
    private val animeRepository: AnimeRepository,
    private val loginRepository: LoginRepository,
) : Presenter<UserListStatus, UserAnimeListUi, UserAnimeListIntent, Nothing>() {
    private val userAnimeList =
        loginRepository.isUserAuthenticated().distinctUntilChanged().map {
            UserListStatus.entries.map {
                StatusAnimeList(
                    status = it,
                    list = animeRepository.getUserAnimeList(status = it).presenterPager(screenModelScope),
                )
            }
        }

    override fun initialInternalState(): UserListStatus = UserListStatus.Completed

    override val state: StateFlow<UserAnimeListUi> =
        combine(userAnimeList, getInternalState(), loginRepository.isUserAuthenticated().distinctUntilChanged()) { a, b, c ->
            UserAnimeListUi(b, c, a)
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserAnimeListUi(initialInternalState()),
        )

    override fun processIntent(intent: UserAnimeListIntent) {
        when (intent) {
            is UserAnimeListIntent.OnSelectStatus -> {
                updateInternalState {
                    intent.status
                }
            }
        }
    }
}
