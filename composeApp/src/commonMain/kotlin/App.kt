import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.fetch.NetworkFetcher
import navigator.BottomRoute
import navigator.BottomRoutes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import screen.home.HomeScreen

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
    MaterialTheme {
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

@Composable
fun BottomBar(
    routes: List<BottomRoute> =
        remember {
            BottomRoutes()
        },
    onRouteSelected: (Screen) -> Unit,
) {
    BottomAppBar {
        LazyRow {
            items(routes) {
                if (it.icon != null) Icon(it.icon.asPainter(), it.text)
                Text(it.text, modifier = Modifier.clickable { onRouteSelected(it.screen) })
            }
        }
    }
}
