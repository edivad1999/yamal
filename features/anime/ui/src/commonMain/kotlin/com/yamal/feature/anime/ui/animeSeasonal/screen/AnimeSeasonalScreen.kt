package com.yamal.feature.anime.ui.animeSeasonal.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.ui.animeSeasonal.presenter.AnimeSeasonalPresenter
import com.yamal.feature.anime.ui.components.GenericAnimeCard
import org.koin.compose.koinInject

@Composable
fun AnimeSeasonalScreen(
    onNavigateBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    presenter: AnimeSeasonalPresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(state.animeSeason.size - 1) }

    YamalScaffold(
        topBar = {
            YamalNavBar(
                title = { Text("Seasonal Anime", color = YamalTheme.colors.text) },
                onBack = onNavigateBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            if (state.animeSeason.isNotEmpty()) {
                // TODO: Replace with proper Tabs component when designed
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(state.animeSeason) { index, (season, _) ->
                        YamalButton(
                            text = "${season.season.name} ${season.year}",
                            onClick = { selectedTabIndex = index },
                            color = if (selectedTabIndex == index) ButtonColor.Primary else ButtonColor.Default,
                            fill = if (selectedTabIndex == index) ButtonFill.Solid else ButtonFill.Outline,
                            size = ButtonSize.Small,
                        )
                    }
                }

                val (_, pagingFlow) = state.animeSeason[selectedTabIndex]
                val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
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
    }
}
