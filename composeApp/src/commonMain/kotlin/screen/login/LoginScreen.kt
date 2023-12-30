package screen.login

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yamal.presentation.login.presenter.LoginEffect
import com.yamal.presentation.login.presenter.LoginIntent
import com.yamal.presentation.login.presenter.LoginPresenter
import kotlinx.coroutines.flow.filterNotNull

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val loginPresenter: LoginPresenter = getScreenModel()
        val uiState by loginPresenter.state.collectAsState()
        LaunchedEffect(Unit) {
            loginPresenter.getEffects().collect {
                when (it) {
                    is LoginEffect.OpenBrowser -> LoginUtilities.launchBrowser(it.url)
                }
            }
        }
        LaunchedEffect(Unit) {
            LoginUtilities.authCode.filterNotNull().collect {
                loginPresenter.processIntent(LoginIntent.AuthorizationComplete(it))
            }
        }
        if (uiState.isLoggedIn) {
            Text("Logged in good")
        } else {
            Button(
                onClick = {
                    loginPresenter.processIntent(LoginIntent.OpenLoginBrowser(uiState.authorizationUrl))
                },
            ) {
                Text("Login")
            }
        }
    }
}
