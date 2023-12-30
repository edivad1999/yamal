package com.yamal.presentation.login.presenter

import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.login.api.LoginRepository
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LoginState(
    val isLoggedIn: Boolean,
    val authorizationUrl: String,
)

sealed interface LoginIntent {
    data class OpenLoginBrowser(val url: String) : LoginIntent

    data class AuthorizationComplete(val code: String) : LoginIntent
}

sealed interface LoginEffect {
    data class OpenBrowser(val url: String) : LoginEffect
}

class LoginPresenter(private val loginRepository: LoginRepository) : Presenter<LoginState, LoginState, LoginIntent, LoginEffect>() {
    private val isLoggedIn = loginRepository.isUserAuthenticated()

    override val state: StateFlow<LoginState> =
        isLoggedIn.map {
            LoginState(it, loginRepository.getAuthorizationUrl())
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialInternalState(),
        )

    override fun initialInternalState(): LoginState = LoginState(false, loginRepository.getAuthorizationUrl())

    override fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OpenLoginBrowser -> {
                launchEffect {
                    LoginEffect.OpenBrowser(intent.url)
                }
            }

            is LoginIntent.AuthorizationComplete -> {
                screenModelScope.launch {
                    loginRepository.authenticate(intent.code)
                    println(intent.code)
                }
            }
        }
    }
}
