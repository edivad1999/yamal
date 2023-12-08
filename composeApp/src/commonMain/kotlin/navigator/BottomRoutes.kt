package navigator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import cafe.adriel.voyager.core.screen.Screen
import core.Icon
import core.StringResources
import screen.AnimeList
import screen.HomeScreen

object BottomRoutes {

    operator fun invoke(): List<BottomRoute> = listOf(
        BottomRoute(screen = HomeScreen, icon = Icon.Vector(Icons.Default.Home), text = StringResources.home),
        BottomRoute(screen = AnimeList, icon = Icon.Vector(Icons.Default.List), text = StringResources.animeList)
    )
}

data class BottomRoute(
    val screen: Screen,
    val icon: Icon? = null,
    val text: String = "",
)
