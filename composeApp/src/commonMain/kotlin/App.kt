import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.yamal.featureManager.AppModules
import navigator.BottomRoute
import navigator.BottomRoutes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.KoinApplication
import screen.HomeScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        KoinApplication(application = {
            modules(AppModules.exportModules())
        }) {
            Navigator(HomeScreen) { navigator ->
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomBar {
                        navigator.push(it)
                    }
                }) {
                    Column(modifier = Modifier.padding(it)) {
                        CurrentScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    routes: List<BottomRoute> = remember {
        BottomRoutes()
    },
    onRouteSelected: (Screen) -> Unit,
) {
    BottomAppBar {
        routes.forEach {
            if (it.icon != null) Icon(it.icon.asPainter(), it.text)
            Text(it.text, modifier = Modifier.clickable { onRouteSelected(it.screen) })
        }
    }
}
