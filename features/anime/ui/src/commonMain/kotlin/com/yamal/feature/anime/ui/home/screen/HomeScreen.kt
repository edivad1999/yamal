package com.yamal.feature.anime.ui.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.animeCard.AnimeCard
import com.yamal.designSystem.components.animeCard.AnimeCardSkeleton
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.empty.Empty
import com.yamal.designSystem.components.heroCard.HeroCard
import com.yamal.designSystem.components.heroCard.HeroCardSkeleton
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.sectionHeader.SectionHeader
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.components.upcomingAnimeItem.UpcomingAnimeItem
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.ui.home.presenter.HomeIntent
import com.yamal.feature.anime.ui.home.presenter.HomePresenter
import com.yamal.platform.utils.formatOneDecimal
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onNavigateToRanking: () -> Unit,
    onNavigateToSeasonal: () -> Unit,
    onNavigateToAnimeDetails: (Int) -> Unit = {},
    presenter: HomePresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()

    YamalScaffold(
        topBar = {
            YamalNavBar(
                title = { Text("YAMAL") },
                right = {
                    Icon(Icons.Outlined.Search, contentDescription = "Search")
                },
            )
        },
    ) { paddingValues ->
        when {
            state.isLoading && state.heroAnimes.isEmpty() -> {
                // Skeleton loading state
                HomeSkeletonContent(paddingValues = paddingValues)
            }

            state.error != null && state.heroAnimes.isEmpty() -> {
                // Error state with retry
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Empty(
                        imageContent = {
                            Icon(
                                icon = Icons.Outlined.CloseCircle,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                tint = YamalTheme.colors.danger,
                            )
                        },
                        description = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Text(
                                    text = state.error ?: "An error occurred",
                                    color = YamalTheme.colors.textSecondary,
                                )
                                YamalButton(
                                    text = "Retry",
                                    onClick = { presenter.processIntent(HomeIntent.Refresh) },
                                    color = ButtonColor.Primary,
                                )
                            }
                        },
                    )
                }
            }

            else -> {
                HomeContent(
                    state = state,
                    paddingValues = paddingValues,
                    onNavigateToRanking = onNavigateToRanking,
                    onNavigateToSeasonal = onNavigateToSeasonal,
                    onNavigateToAnimeDetails = onNavigateToAnimeDetails,
                )
            }
        }
    }
}

@Composable
private fun HomeSkeletonContent(paddingValues: PaddingValues) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Spacer(modifier = Modifier.height(0.dp))

        // Hero Carousel Skeleton
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(2) {
                HeroCardSkeleton()
            }
        }

        // Trending Section Skeleton
        AnimeSectionSkeleton(title = "Currently Airing")

        // Top Anime Section Skeleton
        AnimeSectionSkeleton(title = "Top Anime of All Times")

        // Upcoming Section Skeleton
        UpcomingSectionSkeleton()

        Spacer(modifier = Modifier.height(0.dp))
    }
}

@Composable
private fun AnimeSectionSkeleton(title: String) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
            title = title,
            actionText = "See All",
            onActionClick = {},
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(4) {
                AnimeCardSkeleton(width = 144.dp)
            }
        }
    }
}

@Composable
private fun UpcomingSectionSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
            title = "Top Upcoming",
            actionText = "See All",
            onActionClick = {},
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(3) {
                UpcomingAnimeItemSkeleton()
            }
        }
    }
}

@Composable
private fun UpcomingAnimeItemSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Image skeleton
        Skeleton(
            width = 60.dp,
            height = 80.dp,
            borderRadius = 8.dp,
        )

        // Content skeleton
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Skeleton(
                modifier = Modifier.fillMaxWidth(),
                height = 16.dp,
                borderRadius = 4.dp,
            )
            Skeleton(
                modifier = Modifier.fillMaxWidth(0.7f),
                height = 12.dp,
                borderRadius = 4.dp,
            )
        }

        // Notification icon skeleton
        Skeleton(
            width = 32.dp,
            height = 32.dp,
            borderRadius = 16.dp,
        )
    }
}

@Composable
private fun HomeContent(
    state: com.yamal.feature.anime.ui.home.presenter.HomeState,
    paddingValues: PaddingValues,
    onNavigateToRanking: () -> Unit,
    onNavigateToSeasonal: () -> Unit,
    onNavigateToAnimeDetails: (Int) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Spacer(modifier = Modifier.height(0.dp))

        // Hero Carousel Section
        if (state.heroAnimes.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(state.heroAnimes, key = { it.id }) { anime ->
                    HeroCard(
                        imageUrl = anime.mainPicture?.large ?: anime.mainPicture?.medium,
                        title = anime.title,
                        description = "Score: ${anime.mean?.formatOneDecimal() ?: "--"} • ${anime.mediaType.name}",
                        badge = anime.rank?.takeIf { it <= 10 }?.let { "TOP $it" },
                        onClick = { onNavigateToAnimeDetails(anime.id) },
                    )
                }
            }
        }

        // Trending Anime Section
        if (state.trendingAnimes.isNotEmpty()) {
            AnimeSection(
                title = "Currently Airing",
                actionText = "See All",
                animes = state.trendingAnimes,
                onActionClick = onNavigateToSeasonal,
                onAnimeClick = onNavigateToAnimeDetails,
            )
        }

        // Top Anime of All Times Section
        if (state.topAnimes.isNotEmpty()) {
            AnimeSection(
                title = "Top Anime of All Times",
                actionText = "See All",
                animes = state.topAnimes,
                onActionClick = onNavigateToRanking,
                onAnimeClick = onNavigateToAnimeDetails,
            )
        }

        // Top Upcoming Section
        if (state.upcomingAnimes.isNotEmpty()) {
            UpcomingSection(
                upcomingAnimes = state.upcomingAnimes,
                onAnimeClick = onNavigateToAnimeDetails,
            )
        }

        Spacer(modifier = Modifier.height(0.dp))
    }
}

@Composable
private fun AnimeSection(
    title: String,
    actionText: String,
    animes: List<AnimeForListYamal>,
    onActionClick: () -> Unit,
    onAnimeClick: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
            title = title,
            actionText = actionText,
            onActionClick = onActionClick,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(animes, key = { it.id }) { anime ->
                AnimeCard(
                    imageUrl = anime.mainPicture?.large ?: anime.mainPicture?.medium,
                    title = anime.title,
                    metadata = "${anime.mediaType.name} • ${anime.numberOfEpisodes ?: "?"} eps",
                    rating = anime.mean?.toFloat(),
                    width = 144.dp,
                    onClick = { onAnimeClick(anime.id) },
                )
            }
        }
    }
}

@Composable
private fun UpcomingSection(
    upcomingAnimes: List<AnimeForListYamal>,
    onAnimeClick: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(
            title = "Top Upcoming",
            actionText = "See All",
            onActionClick = { /* Seasonal is now a tab - no navigation needed */ },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            upcomingAnimes.forEach { anime ->
                UpcomingAnimeItem(
                    imageUrl = anime.mainPicture?.large ?: anime.mainPicture?.medium,
                    title = anime.title,
                    metadata = "${anime.mediaType.name} • ${anime.startDate ?: "TBA"}",
                    onNotificationClick = { /* TODO: Toggle notification */ },
                    onClick = { onAnimeClick(anime.id) },
                )
            }
        }
    }
}
