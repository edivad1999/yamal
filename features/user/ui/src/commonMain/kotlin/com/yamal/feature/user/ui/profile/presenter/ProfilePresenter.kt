package com.yamal.feature.user.ui.profile.presenter

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.yamal.feature.user.api.UserRepository
import com.yamal.feature.user.api.model.UserProfileYamal
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Stable
data class ProfileUiState(
    val userProfile: UserProfileYamal? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent

    data object Refresh : ProfileIntent
}

class ProfilePresenter(
    private val userRepository: UserRepository,
) : Presenter<ProfileUiState, ProfileUiState, ProfileIntent, Nothing>() {
    init {
        loadProfile()
    }

    private fun loadProfile() {
        updateInternalState { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            userRepository.getUserProfile().fold(
                ifLeft = { error ->
                    updateInternalState {
                        it.copy(isLoading = false, error = error)
                    }
                },
                ifRight = { profile ->
                    updateInternalState {
                        it.copy(isLoading = false, userProfile = profile)
                    }
                },
            )
        }
    }

    override fun initialInternalState(): ProfileUiState = ProfileUiState()

    override val state: StateFlow<ProfileUiState> = getInternalState()

    override fun processIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile, ProfileIntent.Refresh -> loadProfile()
        }
    }
}
