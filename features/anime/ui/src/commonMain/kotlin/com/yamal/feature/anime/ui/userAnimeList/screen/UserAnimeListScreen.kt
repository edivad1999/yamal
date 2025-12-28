package com.yamal.feature.anime.ui.userAnimeList.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.IconButton
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
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

    YamalScaffold(
        topBar = {
            YamalNavBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = { Text("My Anime List", color = YamalTheme.colors.text) },
                left = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Outlined.ArrowLeft, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            if (!state.isLoggedIn) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Please login to view your anime list",
                        color = YamalTheme.colors.text,
                    )
                }
            } else {
                val selectedIndex = UserListStatus.entries.indexOf(state.userAnimeStatus)

                // TODO: Replace with proper Tabs component when designed
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(UserListStatus.entries) { index, status ->
                        YamalButton(
                            text = formatStatusName(status),
                            onClick = {
                                presenter.processIntent(UserAnimeListIntent.OnSelectStatus(status))
                            },
                            color = if (selectedIndex == index) ButtonColor.Primary else ButtonColor.Default,
                            fill = if (selectedIndex == index) ButtonFill.Solid else ButtonFill.Outline,
                            size = ButtonSize.Small,
                        )
                    }
                }

                val currentStatusList = state.userAnimeList.find { it.status == state.userAnimeStatus }

                if (currentStatusList != null) {
                    val lazyPagingItems = currentStatusList.list.collectAsLazyPagingItems()

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
                                        text = "Error loading anime list",
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
        }
    }
}

private fun formatStatusName(status: UserListStatus): String =
    when (status) {
        UserListStatus.Watching -> "Watching"
        UserListStatus.Completed -> "Completed"
        UserListStatus.OnHold -> "On Hold"
        UserListStatus.Dropped -> "Dropped"
        UserListStatus.PlanToWatch -> "Plan to Watch"
    }
