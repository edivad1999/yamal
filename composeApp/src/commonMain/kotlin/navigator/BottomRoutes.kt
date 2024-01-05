package navigator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import cafe.adriel.voyager.core.screen.Screen
import core.Icon
import core.StringResources
import screen.animeRanking.AnimeRankingScreen
import screen.animeSeasonal.AnimeSeasonalScreen
import screen.home.HomeScreen
import screen.login.LoginScreen
import screen.userAnimeList.UserAnimeList

object BottomRoutes {
    operator fun invoke(): List<BottomRoute> =
        listOf(
            BottomRoute(screen = HomeScreen, icon = Icon.Vector(Icons.Default.Home), text = StringResources.home),
            BottomRoute(screen = AnimeRankingScreen, icon = Icon.Vector(Icons.Default.CheckCircle), text = StringResources.animeRanking),
            BottomRoute(screen = AnimeSeasonalScreen, icon = Icon.Vector(Icons.Default.DateRange), text = StringResources.animeSeasonal),
            BottomRoute(screen = UserAnimeList, icon = Icon.Vector(Icons.Default.List), text = StringResources.animeList),
            BottomRoute(screen = LoginScreen, icon = Icon.Vector(Icons.Default.AccountBox), text = StringResources.user),
        )
}

data class BottomRoute(
    val screen: Screen,
    val icon: Icon? = null,
    val text: String = "",
)
