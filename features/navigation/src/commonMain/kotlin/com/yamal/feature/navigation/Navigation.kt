package com.yamal.feature.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.tabBar.TabBarDefaults
import com.yamal.designSystem.components.tabBar.YamalTabBar
import com.yamal.designSystem.components.tabBar.YamalTabBarItem
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.feature.anime.ui.animeDetails.screen.AnimeDetailsScreen
import com.yamal.feature.anime.ui.animeRanking.screen.AnimeRankingScreen
import com.yamal.feature.anime.ui.animeSeasonal.screen.AnimeSeasonalScreen
import com.yamal.feature.anime.ui.home.screen.HomeScreen
import com.yamal.feature.anime.ui.userAnimeList.screen.UserAnimeListScreen
import com.yamal.feature.login.ui.screen.LoginScreen
import com.yamal.feature.manga.ui.mangaDetails.screen.MangaDetailsScreen
import com.yamal.feature.search.ui.search.screen.SearchScreen
import com.yamal.feature.user.ui.profile.screen.ProfileScreen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

// ============================================================================
// Route Definitions
// ============================================================================

// Auth flow routes (no tab bar)
@Serializable
object Login

// Tab destination routes (show tab bar)
@Serializable
object HomeTab

@Serializable
object SearchTab

@Serializable
object SeasonalTab

@Serializable
object MyListTab

@Serializable
object ProfileTab

// Nested routes (hide tab bar)
@Serializable
data class AnimeDetails(
    val animeId: Int,
)

@Serializable
object AnimeRanking

@Serializable
data class MangaDetails(
    val mangaId: Int,
)

@Serializable
object Settings

// Legacy routes kept for compatibility
@Serializable
object Home

@Serializable
object UserAnimeList

// ============================================================================
// Tab Bar Configuration
// ============================================================================

private val tabDestinations =
    listOf(
        HomeTab::class,
        SearchTab::class,
        SeasonalTab::class,
        MyListTab::class,
        ProfileTab::class,
    )

// ============================================================================
// Root Navigation Graph
// ============================================================================

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
                    navController.navigate(HomeTab) {
                        popUpTo<Login> { inclusive = true }
                    }
                },
                authCodeFlow = authCodeFlow,
                launchBrowser = launchBrowser,
            )
        }

        composable<HomeTab> {
            MainScreen(
                initialTab = TabDestination.Home,
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onNavigateToAnimeRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
            )
        }

        composable<SearchTab> {
            MainScreen(
                initialTab = TabDestination.Search,
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onNavigateToAnimeRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
            )
        }

        composable<MyListTab> {
            MainScreen(
                initialTab = TabDestination.MyList,
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onNavigateToAnimeRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
            )
        }

        composable<SeasonalTab> {
            MainScreen(
                initialTab = TabDestination.Seasonal,
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onNavigateToAnimeRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
            )
        }

        composable<ProfileTab> {
            MainScreen(
                initialTab = TabDestination.Profile,
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onNavigateToAnimeRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
            )
        }

        // Nested screens (no tab bar)
        composable<AnimeDetails> { backStackEntry ->
            val animeDetails: AnimeDetails = backStackEntry.toRoute()
            AnimeDetailsScreen(
                animeId = animeDetails.animeId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onOpenUrl = launchBrowser,
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

        composable<Settings> {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        // Manga details screen
        composable<MangaDetails> { backStackEntry ->
            val mangaDetails: MangaDetails = backStackEntry.toRoute()
            MangaDetailsScreen(
                mangaId = mangaDetails.mangaId,
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        // Legacy routes for backward compatibility
        composable<Home> {
            MainScreen(
                initialTab = TabDestination.Home,
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onNavigateToAnimeRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
            )
        }

        composable<UserAnimeList> {
            MainScreen(
                initialTab = TabDestination.MyList,
                onNavigateToAnimeDetails = { animeId ->
                    navController.navigate(AnimeDetails(animeId))
                },
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(MangaDetails(mangaId))
                },
                onNavigateToAnimeRanking = {
                    navController.navigate(AnimeRanking)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
            )
        }
    }
}

// ============================================================================
// Main Screen with Tab Bar
// ============================================================================

enum class TabDestination {
    Home,
    Search,
    Seasonal,
    MyList,
    Profile,
}

@Composable
private fun MainScreen(
    initialTab: TabDestination,
    onNavigateToAnimeDetails: (Int) -> Unit,
    onNavigateToMangaDetails: (Int) -> Unit,
    onNavigateToAnimeRanking: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determine if tab bar should be shown
    val showTabBar =
        currentDestination?.let { dest ->
            tabDestinations.any { dest.hasRoute(it) }
        } ?: true

    YamalScaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showTabBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                YamalTabBar {
                    YamalTabBarItem(
                        selected = currentDestination?.hasRoute<HomeTab>() == true,
                        onClick = {
                            tabNavController.navigate(HomeTab) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                icon =
                                    if (currentDestination?.hasRoute<HomeTab>() == true) {
                                        Icons.Filled.Home
                                    } else {
                                        Icons.Outlined.Home
                                    },
                                contentDescription = "Home",
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                fontSize = TabBarDefaults.TitleFontSize,
                                lineHeight = TabBarDefaults.TitleLineHeight,
                            )
                        },
                    )
                    YamalTabBarItem(
                        selected = currentDestination?.hasRoute<SearchTab>() == true,
                        onClick = {
                            tabNavController.navigate(SearchTab) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                icon = Icons.Outlined.Search,
                                contentDescription = "Search",
                            )
                        },
                        label = {
                            Text(
                                text = "Search",
                                fontSize = TabBarDefaults.TitleFontSize,
                                lineHeight = TabBarDefaults.TitleLineHeight,
                            )
                        },
                    )
                    YamalTabBarItem(
                        selected = currentDestination?.hasRoute<SeasonalTab>() == true,
                        onClick = {
                            tabNavController.navigate(SeasonalTab) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                icon =
                                    if (currentDestination?.hasRoute<SeasonalTab>() == true) {
                                        Icons.Filled.Calendar
                                    } else {
                                        Icons.Outlined.Calendar
                                    },
                                contentDescription = "Seasonal",
                            )
                        },
                        label = {
                            Text(
                                text = "Seasonal",
                                fontSize = TabBarDefaults.TitleFontSize,
                                lineHeight = TabBarDefaults.TitleLineHeight,
                            )
                        },
                    )
                    YamalTabBarItem(
                        selected = currentDestination?.hasRoute<MyListTab>() == true,
                        onClick = {
                            tabNavController.navigate(MyListTab) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                icon =
                                    if (currentDestination?.hasRoute<MyListTab>() == true) {
                                        Icons.Filled.Book
                                    } else {
                                        Icons.Outlined.Book
                                    },
                                contentDescription = "My List",
                            )
                        },
                        label = {
                            Text(
                                text = "My List",
                                fontSize = TabBarDefaults.TitleFontSize,
                                lineHeight = TabBarDefaults.TitleLineHeight,
                            )
                        },
                    )
                    YamalTabBarItem(
                        selected = currentDestination?.hasRoute<ProfileTab>() == true,
                        onClick = {
                            tabNavController.navigate(ProfileTab) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                icon =
                                    if (currentDestination?.hasRoute<ProfileTab>() == true) {
                                        Icons.Filled.Profile
                                    } else {
                                        Icons.Outlined.User
                                    },
                                contentDescription = "Profile",
                            )
                        },
                        label = {
                            Text(
                                text = "Profile",
                                fontSize = TabBarDefaults.TitleFontSize,
                                lineHeight = TabBarDefaults.TitleLineHeight,
                            )
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = tabNavController,
            startDestination =
                when (initialTab) {
                    TabDestination.Home -> HomeTab
                    TabDestination.Search -> SearchTab
                    TabDestination.Seasonal -> SeasonalTab
                    TabDestination.MyList -> MyListTab
                    TabDestination.Profile -> ProfileTab
                },
            modifier = Modifier.padding(paddingValues),
        ) {
            composable<HomeTab> {
                HomeScreen(
                    onNavigateToRanking = onNavigateToAnimeRanking,
                    onNavigateToSeasonal = {
                        tabNavController.navigate(SeasonalTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToAnimeDetails = onNavigateToAnimeDetails,
                )
            }

            composable<SearchTab> {
                SearchScreen(
                    onNavigateToAnimeDetails = onNavigateToAnimeDetails,
                    onNavigateToMangaDetails = onNavigateToMangaDetails,
                )
            }

            composable<SeasonalTab> {
                AnimeSeasonalScreen(
                    onNavigateBack = {
                        tabNavController.navigate(HomeTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onAnimeClick = onNavigateToAnimeDetails,
                )
            }

            composable<MyListTab> {
                UserAnimeListScreen(
                    onNavigateBack = {
                        tabNavController.navigate(HomeTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onAnimeClick = onNavigateToAnimeDetails,
                )
            }

            composable<ProfileTab> {
                ProfileScreen(
                    onNavigateToSettings = onNavigateToSettings,
                )
            }
        }
    }
}

// ============================================================================
// Placeholder Screens
// ============================================================================

@Composable
private fun SettingsScreen(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("TODO: SettingsScreen")
    }
}
