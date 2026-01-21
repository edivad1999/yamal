package com.yamal.feature.anime.ui.animeRanking.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.empty.Empty
import com.yamal.designSystem.components.input.SearchField
import com.yamal.designSystem.components.listItem.AnimeRankingListItemSkeleton
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.ui.animeRanking.components.AnimeRankingListItem
import com.yamal.feature.anime.ui.animeRanking.presenter.AnimeRankingPresenter
import org.koin.compose.koinInject

@Composable
fun AnimeRankingScreen(
    onNavigateBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    presenter: AnimeRankingPresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()
    val lazyPagingItems = state.ranking.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }

    YamalScaffold(
        topBar = {
            YamalNavBar(
                title = { Text("Top Anime", color = YamalTheme.colors.text) },
                onBack = onNavigateBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            // Search bar
            SearchField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search rankings...",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
            )

            Spacer(Modifier.height(8.dp))

            // Ranking list
            val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

            // Filter items by search query
            val filteredItems =
                remember(lazyPagingItems.itemCount, searchQuery) {
                    if (searchQuery.isEmpty()) {
                        (0 until lazyPagingItems.itemCount).mapNotNull { lazyPagingItems[it] }
                    } else {
                        (0 until lazyPagingItems.itemCount)
                            .mapNotNull { lazyPagingItems[it] }
                            .filter { it.title.contains(searchQuery, ignoreCase = true) }
                    }
                }

            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    // Skeleton loading state
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding =
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp + navigationBarPadding.calculateBottomPadding(),
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(10) {
                            AnimeRankingListItemSkeleton()
                        }
                    }
                }

                is LoadState.Error -> {
                    // Error state with retry
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                                        text = "Error loading anime ranking",
                                        color = YamalTheme.colors.textSecondary,
                                    )
                                    YamalButton(
                                        text = "Retry",
                                        onClick = { lazyPagingItems.retry() },
                                        color = ButtonColor.Primary,
                                    )
                                }
                            },
                        )
                    }
                }

                else -> {
                    // Content loaded
                    if (filteredItems.isEmpty() && searchQuery.isNotEmpty()) {
                        // No search results
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Empty(
                                imageContent = {
                                    Icon(
                                        icon = Icons.Outlined.Search,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        tint = YamalTheme.colors.textSecondary,
                                    )
                                },
                                description = "No anime found for \"$searchQuery\"",
                            )
                        }
                    } else if (lazyPagingItems.itemCount == 0) {
                        // Empty state
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Empty(description = "No anime available")
                        }
                    } else {
                        // Normal list
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding =
                                PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 16.dp,
                                    bottom = 16.dp + navigationBarPadding.calculateBottomPadding(),
                                ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(filteredItems.size) { index ->
                                val anime = filteredItems[index]
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable { onAnimeClick(anime.id) },
                                ) {
                                    AnimeRankingListItem(anime = anime)
                                }
                            }

                            // Append loading
                            when (lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            SpinLoadingIndicator()
                                        }
                                    }
                                }

                                is LoadState.Error -> {
                                    item {
                                        Column(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                        ) {
                                            Text(
                                                text = "Error loading more items",
                                                color = YamalTheme.colors.danger,
                                            )
                                            YamalButton(
                                                text = "Retry",
                                                onClick = { lazyPagingItems.retry() },
                                                color = ButtonColor.Default,
                                            )
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}
