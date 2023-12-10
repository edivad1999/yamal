package screen.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import com.yamal.presentation.login.presenter.LoginPresenter
import com.yamal.presentation.login.presenter.LoginScreen

object LoginScreen : Screen {

    @Composable
    override fun Content() {
        val loginPresenter: LoginPresenter = getScreenModel()

        val uiState = loginPresenter.present()
        LoginScreenUI(state = uiState, onUrlChange = {
            uiState.onIntent(LoginScreen.LoginIntent.UrlChanged(it))
        })
    }

    @Composable fun LoginScreenUI(state: LoginScreen.LoginState, onUrlChange: (String) -> Unit) {
        if (state.isLoggedIn) {
            Text("Logged in good")
        } else {
            val webViewState = rememberWebViewState(state.authorizationUrl)
            LaunchedEffect(Unit) {
                webViewState.webSettings.apply {
                    isJavaScriptEnabled = true
                    androidWebSettings.apply {
                        isAlgorithmicDarkeningAllowed = true
                        safeBrowsingEnabled = true
                    }
                }
            }
            val currentUrl = remember(webViewState.lastLoadedUrl) {
                webViewState.lastLoadedUrl
            }

            LaunchedEffect(currentUrl) {
                if (currentUrl != null) onUrlChange(currentUrl)
            }
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
