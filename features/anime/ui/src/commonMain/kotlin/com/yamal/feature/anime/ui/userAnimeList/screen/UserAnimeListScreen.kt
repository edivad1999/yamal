package com.yamal.feature.anime.ui.userAnimeList.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonShape
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.empty.Empty
import com.yamal.designSystem.components.input.SearchField
import com.yamal.designSystem.components.listItem.UserAnimeListItemSkeleton
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.feature.anime.ui.userAnimeList.components.UserAnimeListItem
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
    var searchQuery by remember { mutableStateOf("") }

    YamalScaffold(
        topBar = {
            YamalNavBar(
                title = { Text("My List", color = YamalTheme.colors.text) },
                back = "",
                onBack = onNavigateBack,
                right = {
                    Icon(
                        icon = Icons.Outlined.Filter,
                        contentDescription = "Filter",
                        modifier = Modifier.size(24.dp),
                    )
                },
            )
        },
    ) { paddingValues ->
        if (!state.isLoggedIn) {
            UnauthenticatedContent(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                onLoginClick = { /* TODO: Navigate to login */ },
            )
        } else {
            AuthenticatedContent(
                state = state,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onStatusSelect = { status ->
                    presenter.processIntent(UserAnimeListIntent.OnSelectStatus(status))
                },
                onAnimeClick = onAnimeClick,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            )
        }
    }
}

@Composable
private fun AuthenticatedContent(
    state: com.yamal.feature.anime.ui.userAnimeList.presenter.UserAnimeListUi,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onStatusSelect: (UserListStatusYamal) -> Unit,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Search bar
        SearchField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = "Search titles...",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
        )

        Spacer(Modifier.height(12.dp))

        // Status filter chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(UserListStatusYamal.entries) { index, status ->
                val isSelected = state.userAnimeStatus == status
                StatusFilterChip(
                    status = status,
                    isSelected = isSelected,
                    onClick = { onStatusSelect(status) },
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Anime list
        val currentStatusList = state.userAnimeList.find { it.status == state.userAnimeStatus }
        val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

        if (currentStatusList != null) {
            val lazyPagingItems = currentStatusList.list.collectAsLazyPagingItems()

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
                        items(8) {
                            UserAnimeListItemSkeleton()
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
                                        text = "Error loading your list",
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
                        // Empty state for this status
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Empty(
                                imageContent = {
                                    Icon(
                                        icon = getStatusIcon(state.userAnimeStatus),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        tint = YamalTheme.colors.textSecondary,
                                    )
                                },
                                description = "No anime in ${formatStatusName(state.userAnimeStatus)}",
                            )
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
                                    UserAnimeListItem(
                                        anime = anime,
                                        status = state.userAnimeStatus,
                                    )
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
        } else {
            // No status list available (shouldn't happen normally)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Empty(description = "Select a status to view your list")
            }
        }
    }
}

private fun getStatusIcon(status: UserListStatusYamal) =
    when (status) {
        UserListStatusYamal.Watching -> Icons.Filled.PlayCircle
        UserListStatusYamal.Completed -> Icons.Filled.CheckCircle
        UserListStatusYamal.OnHold -> Icons.Outlined.PauseCircle
        UserListStatusYamal.Dropped -> Icons.Outlined.CloseCircle
        UserListStatusYamal.PlanToWatch -> Icons.Outlined.Calendar
    }

@Composable
private fun UnauthenticatedContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // Ghost list background
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            repeat(4) {
                GhostListItem()
            }
        }

        // Centered content
        Column(
            modifier =
                Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Illustration placeholder
            Surface(
                modifier = Modifier.size(192.dp),
                shape = RoundedCornerShape(16.dp),
                color = YamalTheme.colors.box,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        icon = Icons.Filled.Book,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = YamalTheme.colors.textSecondary.copy(alpha = 0.5f),
                    )
                }
            }

            // Text content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Your List is Waiting",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Join our community to track the anime you've watched, save manga for later, and rate your favorites. It's free and easy.",
                    fontSize = 14.sp,
                    color = YamalTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            // Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                YamalButton(
                    text = "Log In or Sign Up",
                    onClick = onLoginClick,
                    color = ButtonColor.Primary,
                    fill = ButtonFill.Solid,
                    block = true,
                )
                YamalButton(
                    text = "Continue browsing",
                    onClick = { /* TODO: Navigate back or to home */ },
                    color = ButtonColor.Default,
                    fill = ButtonFill.None,
                    block = true,
                )
            }
        }
    }
}

@Composable
private fun GhostListItem() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.box.copy(alpha = 0.3f),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Poster placeholder
            Surface(
                modifier =
                    Modifier
                        .size(width = 70.dp, height = 105.dp),
                shape = RoundedCornerShape(8.dp),
                color = YamalTheme.colors.textSecondary.copy(alpha = 0.2f),
            ) {}

            // Content placeholders
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.7f)
                            .height(16.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = YamalTheme.colors.textSecondary.copy(alpha = 0.2f),
                ) {}
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.4f)
                            .height(12.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = YamalTheme.colors.textSecondary.copy(alpha = 0.2f),
                ) {}
            }
        }
    }
}

@Composable
private fun StatusFilterChip(
    status: UserListStatusYamal,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val iconPainter =
        when (status) {
            UserListStatusYamal.Watching -> Icons.Filled.PlayCircle
            UserListStatusYamal.Completed -> Icons.Filled.CheckCircle
            UserListStatusYamal.OnHold -> Icons.Outlined.PauseCircle
            UserListStatusYamal.Dropped -> Icons.Outlined.CloseCircle
            UserListStatusYamal.PlanToWatch -> Icons.Outlined.Calendar
        }

    YamalButton(
        onClick = onClick,
        color = if (isSelected) ButtonColor.Primary else ButtonColor.Default,
        fill = if (isSelected) ButtonFill.Solid else ButtonFill.Outline,
        size = ButtonSize.Small,
        shape = ButtonShape.Rounded,
    ) {
        if (status != UserListStatusYamal.PlanToWatch || isSelected) {
            Icon(
                icon = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.size(4.dp))
        }
        Text(formatStatusName(status))
    }
}

private fun formatStatusName(status: UserListStatusYamal): String =
    when (status) {
        UserListStatusYamal.Watching -> "Watching"
        UserListStatusYamal.Completed -> "Completed"
        UserListStatusYamal.OnHold -> "On Hold"
        UserListStatusYamal.Dropped -> "Dropped"
        UserListStatusYamal.PlanToWatch -> "Plan to Watch"
    }
