package com.yamal.feature.anime.ui.userAnimeList.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamal.feature.anime.api.model.UserListStatus
import com.yamal.feature.anime.ui.components.GenericAnimeCard
import com.yamal.feature.anime.ui.userAnimeList.presenter.UserAnimeListIntent
import com.yamal.feature.anime.ui.userAnimeList.presenter.UserAnimeListPresenter
import org.koin.compose.koinInject

@Composable
fun UserAnimeListScreen(
    onNavigateBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    presenter: UserAnimeListPresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Anime List") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!state.isLoggedIn) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Please login to view your anime list")
                }
            } else {
                val selectedIndex = UserListStatus.entries.indexOf(state.userAnimeStatus)

                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    backgroundColor = MaterialTheme.colors.surface,
                    edgePadding = 16.dp
                ) {
                    UserListStatus.entries.forEachIndexed { index, status ->
                        Tab(
                            selected = selectedIndex == index,
                            onClick = {
                                presenter.processIntent(UserAnimeListIntent.OnSelectStatus(status))
                            },
                            text = { Text(formatStatusName(status)) }
                        )
                    }
                }

                val currentStatusList = state.userAnimeList.find { it.status == state.userAnimeStatus }

                if (currentStatusList != null) {
                    val lazyPagingItems = currentStatusList.list.collectAsLazyPagingItems()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lazyPagingItems.itemCount) { index ->
                            lazyPagingItems[index]?.let { anime ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onAnimeClick(anime.id) }
                                ) {
                                    GenericAnimeCard(anime)
                                }
                            }
                        }

                        when (lazyPagingItems.loadState.refresh) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    Text(
                                        text = "Error loading anime list",
                                        modifier = Modifier.padding(16.dp),
                                        color = MaterialTheme.colors.error
                                    )
                                }
                            }
                            else -> {}
                        }

                        when (lazyPagingItems.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
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
                                        color = MaterialTheme.colors.error
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
}

private fun formatStatusName(status: UserListStatus): String {
    return when (status) {
        UserListStatus.Watching -> "Watching"
        UserListStatus.Completed -> "Completed"
        UserListStatus.OnHold -> "On Hold"
        UserListStatus.Dropped -> "Dropped"
        UserListStatus.PlanToWatch -> "Plan to Watch"
    }
}
