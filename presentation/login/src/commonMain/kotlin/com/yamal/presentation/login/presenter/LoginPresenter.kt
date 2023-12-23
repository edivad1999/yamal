package com.yamal.presentation.login.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.login.api.LoginRepository
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object LoginScreen {

    data class LoginState(
        val authMessage: String,
        val isLoggedIn: Boolean,
        val authorizationUrl: String,
        val onIntent: (LoginIntent) -> Unit,
    )

    sealed interface LoginIntent {

        data class OpenLoginBrowser(val url: String) : LoginIntent
        data class AuthorizationComplete(val code: String) : LoginIntent
    }

    sealed interface LoginEffect {
        data class OpenBrowser(val url: String) : LoginEffect
        data class AuthorizationParameters(val url: String) : LoginEffect
    }
}

class LoginPresenter(private val loginRepository: LoginRepository) : Presenter<LoginScreen.LoginState, LoginScreen.LoginEffect> {

    override val effects: MutableSharedFlow<LoginScreen.LoginEffect> = MutableSharedFlow()

    @Composable
    override fun present(): LoginScreen.LoginState {
        val isLoggedIn by loginRepository.isUserAuthenticated().collectAsState(false)

        var authMessage by remember { mutableStateOf("") }
        return LoginScreen.LoginState(
            authMessage = authMessage,
            isLoggedIn = isLoggedIn,
            authorizationUrl = loginRepository.getAuthorizationUrl(),
            onIntent = {
                when (it) {
                    is LoginScreen.LoginIntent.OpenLoginBrowser -> {
                        screenModelScope.launch {
                            effects.emit(LoginScreen.LoginEffect.OpenBrowser(it.url))
                        }
                    }

                    is LoginScreen.LoginIntent.AuthorizationComplete -> {
                        screenModelScope.launch {
                            loginRepository.authenticate(it.code)
                            println(it.code)
                        }
                    }
                }
            }
        )
    }
}
