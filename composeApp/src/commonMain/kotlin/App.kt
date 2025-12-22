import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.fetch.NetworkFetcher
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.login.ui.presenter.LoginPresenter
import com.yamal.feature.navigation.Routes
import com.yamal.feature.navigation.YamalNavGraph
import core.LoginUtilities
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.koinInject

@OptIn(ExperimentalResourceApi::class, ExperimentalCoilApi::class)
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(NetworkFetcher.Factory())
            }
            .build()
    }

    YamalTheme {
        val navController = rememberNavController()

        // Check login state to determine initial screen
        val loginPresenter: LoginPresenter = koinInject()
        val loginState by loginPresenter.state.collectAsState()

        // If user is logged in, navigate to home
        LaunchedEffect(loginState.isLoggedIn) {
            if (loginState.isLoggedIn) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
        }

        YamalNavGraph(
            navController = navController,
            startDestination = if (loginState.isLoggedIn) Routes.HOME else Routes.LOGIN,
            authCodeFlow = LoginUtilities.authCode,
            launchBrowser = { url -> LoginUtilities.launchBrowser(url) }
        )
    }
}
