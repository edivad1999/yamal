package com.yamal.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yamal.feature.anime.ui.animeDetails.screen.AnimeDetailsScreen
import com.yamal.feature.anime.ui.animeRanking.screen.AnimeRankingScreen
import com.yamal.feature.anime.ui.animeSeasonal.screen.AnimeSeasonalScreen
import com.yamal.feature.anime.ui.home.screen.HomeScreen
import com.yamal.feature.anime.ui.userAnimeList.screen.UserAnimeListScreen
import com.yamal.feature.login.ui.screen.LoginScreen
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation routes for the app
 */
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val ANIME_SEASONAL = "anime_seasonal"
    const val ANIME_RANKING = "anime_ranking"
    const val USER_ANIME_LIST = "user_anime_list"
    const val ANIME_DETAILS = "anime_details/{animeId}"

    fun animeDetails(animeId: Int) = "anime_details/$animeId"
}

@Composable
fun YamalNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LOGIN,
    authCodeFlow: StateFlow<String?>,
    launchBrowser: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                authCodeFlow = authCodeFlow,
                launchBrowser = launchBrowser
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToSeasonal = {
                    navController.navigate(Routes.ANIME_SEASONAL)
                },
                onNavigateToRanking = {
                    navController.navigate(Routes.ANIME_RANKING)
                },
                onNavigateToUserList = {
                    navController.navigate(Routes.USER_ANIME_LIST)
                }
            )
        }

        composable(Routes.ANIME_SEASONAL) {
            AnimeSeasonalScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAnimeClick = { animeId ->
                    navController.navigate(Routes.animeDetails(animeId))
                }
            )
        }

        composable(Routes.ANIME_RANKING) {
            AnimeRankingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAnimeClick = { animeId ->
                    navController.navigate(Routes.animeDetails(animeId))
                }
            )
        }

        composable(Routes.USER_ANIME_LIST) {
            UserAnimeListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAnimeClick = { animeId ->
                    navController.navigate(Routes.animeDetails(animeId))
                }
            )
        }

        composable(
            route = Routes.ANIME_DETAILS,
            arguments = listOf(
                navArgument("animeId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
            AnimeDetailsScreen(
                animeId = animeId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
