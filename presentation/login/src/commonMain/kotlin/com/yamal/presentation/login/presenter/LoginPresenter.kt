package com.yamal.presentation.login.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.yamal.feature.login.api.LoginRepository
import com.yamal.mvi.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

object LoginScreen {

    data class LoginState(
        val isLoggedIn: Boolean,
        val authorizationUrl: String,
        val onIntent: (LoginIntent) -> Unit,
    )

    sealed interface LoginIntent {

        data class UrlChanged(val authCode: String) : LoginIntent
    }
}

class LoginPresenter(private val loginRepository: LoginRepository) : Presenter<LoginScreen.LoginState, Nothing> {

    override val effects: Flow<Nothing> = flowOf()

    @Composable
    override fun present(): LoginScreen.LoginState {
        val isLoggedIn by loginRepository.isUserAuthenticated().collectAsState(false)

        return LoginScreen.LoginState(
            isLoggedIn = isLoggedIn,
            authorizationUrl = loginRepository.getAuthorizationUrl(),
            onIntent = {
                when (it) {
                    is LoginScreen.LoginIntent.UrlChanged -> {
                        screenModelScope.launch {
                            println("AuthCode: " + it.authCode)
                            if (it.authCode.contains("yamal://yamal")) {
                                loginRepository.authenticate(it.authCode)
                            }
                        }
                    }
                }
            }
        )
    }
}
