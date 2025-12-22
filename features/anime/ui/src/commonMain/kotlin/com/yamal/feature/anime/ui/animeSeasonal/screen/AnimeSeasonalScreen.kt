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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seasonal Anime") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            if (state.animeSeason.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    backgroundColor = MaterialTheme.colors.surface,
                    edgePadding = 16.dp,
                ) {
                    state.animeSeason.forEachIndexed { index, (season, _) ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text("${season.season.name} ${season.year}") },
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
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                Text(
                                    text = "Error loading more items",
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colors.error,
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
