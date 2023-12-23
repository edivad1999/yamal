package screen.login

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yamal.presentation.login.presenter.LoginPresenter
import com.yamal.presentation.login.presenter.LoginScreen
import kotlinx.coroutines.flow.filterNotNull

object LoginScreen : Screen {

    @Composable
    override fun Content() {
        val loginPresenter: LoginPresenter = getScreenModel()
        val uiState = loginPresenter.present()
        LaunchedEffect(Unit) {
            loginPresenter.effects.collect {
                when (it) {
                    is LoginScreen.LoginEffect.AuthorizationParameters -> {}
                    is LoginScreen.LoginEffect.OpenBrowser -> LoginUtilities.launchBrowser(it.url)
                }
            }
        }
        LaunchedEffect(Unit) {
            LoginUtilities.authCode.filterNotNull().collect {
                uiState.onIntent(LoginScreen.LoginIntent.AuthorizationComplete(it))
            }
        }
        LoginScreenUI(state = uiState)
    }

    @Composable fun LoginScreenUI(state: LoginScreen.LoginState) {
        Text(state.authMessage)
        if (state.isLoggedIn) {
            Text("Logged in good")
        } else {
            Button(
                onClick = {
                    state.onIntent(LoginScreen.LoginIntent.OpenLoginBrowser(state.authorizationUrl))
                }
            ) {
                Text("Login")
            }
        }
    }
}
