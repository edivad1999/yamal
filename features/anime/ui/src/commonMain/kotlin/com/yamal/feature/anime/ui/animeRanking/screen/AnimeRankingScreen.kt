package com.yamal.feature.anime.ui.animeRanking.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.ui.animeRanking.presenter.AnimeRankingPresenter
import com.yamal.feature.anime.ui.components.GenericAnimeCard
import org.koin.compose.koinInject

@Composable
fun AnimeRankingScreen(
    onNavigateBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    presenter: AnimeRankingPresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()
    val lazyPagingItems = state.ranking.collectAsLazyPagingItems()

    YamalScaffold(
        topBar = {
            YamalNavBar(
                title = { Text("Top Anime", color = YamalTheme.colors.text) },
                onBack = onNavigateBack,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { anime ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { onAnimeClick(anime.id) },
                    ) {
                        GenericAnimeCard(anime)
                    }
                }
            }

            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            SpinLoadingIndicator()
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        Text(
                            text = "Error loading anime ranking",
                            modifier = Modifier.padding(16.dp),
                            color = YamalTheme.colors.danger,
                        )
                    }
                }

                else -> {}
            }

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
                        Text(
                            text = "Error loading more items",
                            modifier = Modifier.padding(16.dp),
                            color = YamalTheme.colors.danger,
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
