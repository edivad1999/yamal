package com.yamal.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yamal.feature.anime.ui.animeDetails.screen.AnimeDetailsScreen
import com.yamal.feature.anime.ui.animeRanking.screen.AnimeRankingScreen
import com.yamal.feature.anime.ui.animeSeasonal.screen.AnimeSeasonalScreen
import com.yamal.feature.anime.ui.home.screen.HomeScreen
import com.yamal.feature.anime.ui.userAnimeList.screen.UserAnimeListScreen
import com.yamal.feature.login.ui.screen.LoginScreen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using Kotlinx Serialization
 */
@Serializable
object Home

@Serializable
object Login

@Serializable
object UserAnimeList

@Serializable
object AnimeSeasonal

@Serializable
object AnimeRanking

@Serializable
data class AnimeDetails(
    val animeId: Int,
)

@Composable
fun YamalNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: Any = Login,
    authCodeFlow: StateFlow<String?>,
    launchBrowser: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo<Login> { inclusive = true }
                    }
                },
                authCodeFlow = authCodeFlow,
                launchBrowser = launchBrowser,
            )
        }

        composable<Home> {
            HomeScreen(
                onNavigateToSeasonal = {
                    navController.navigate(AnimeSeasonal)
                },
                onNavigateToRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToUserList = {
                    navController.navigate(UserAnimeList)
                },
            )
        }

        composable<AnimeSeasonal> {
            AnimeSeasonalScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAnimeClick = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
            )
        }

        composable<AnimeRanking> {
            AnimeRankingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAnimeClick = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
            )
        }

        composable<UserAnimeList> {
            UserAnimeListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAnimeClick = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
            )
        }

        composable<AnimeDetails> { backStackEntry ->
            val animeDetails: AnimeDetails = backStackEntry.toRoute()
            AnimeDetailsScreen(
                animeId = animeDetails.animeId,
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
