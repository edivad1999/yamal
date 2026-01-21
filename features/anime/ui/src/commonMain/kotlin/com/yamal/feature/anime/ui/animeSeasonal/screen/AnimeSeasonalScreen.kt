package com.yamal.feature.anime.ui.animeSeasonal.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.animeCard.AnimeCard
import com.yamal.designSystem.components.animeCard.AnimeCardSkeleton
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.daySelector.Day
import com.yamal.designSystem.components.empty.Empty
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.NavBarDefaults
import com.yamal.designSystem.components.popup.YamalBottomSheet
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.selector.Selector
import com.yamal.designSystem.components.selector.SelectorOption
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.IconPainter
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeSeasonYamal
import com.yamal.feature.anime.ui.animeSeasonal.presenter.AnimeSeasonalIntent
import com.yamal.feature.anime.ui.animeSeasonal.presenter.AnimeSeasonalPresenter
import com.yamal.feature.anime.ui.animeSeasonal.presenter.DayOfWeek
import com.yamal.feature.anime.ui.animeSeasonal.presenter.MediaTypeFilter
import com.yamal.feature.anime.ui.animeSeasonal.presenter.SeriesFilter
import com.yamal.feature.anime.ui.animeSeasonal.presenter.ViewMode
import com.yamal.feature.anime.ui.animeSeasonal.presenter.getCurrentDayIndex
import com.yamal.platform.utils.formatOneDecimal
import org.koin.compose.koinInject

@Composable
fun AnimeSeasonalScreen(
    onNavigateBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    presenter: AnimeSeasonalPresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Track if we've scrolled past the header for sticky behavior
    val showCompactHeader by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    YamalScaffold(
        topBar = {
            SeasonalHeader(
                seasons = state.animeSeason.map { it.first },
                selectedSeasonIndex = state.selectedSeasonIndex,
                onSeasonSelected = { index ->
                    presenter.processIntent(AnimeSeasonalIntent.SelectSeasonIndex(index))
                },
                viewMode = state.viewMode,
                hasActiveFilters = state.mediaTypeFilter != MediaTypeFilter.All || state.seriesFilter != SeriesFilter.All,
                onViewModeToggle = {
                    val newMode = if (state.viewMode == ViewMode.List) ViewMode.Grid else ViewMode.List
                    presenter.processIntent(AnimeSeasonalIntent.SetViewMode(newMode))
                },
                onFilterClick = { showFilterSheet = true },
                isCompact = showCompactHeader,
            )
        },
    ) { paddingValues ->
        val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

        if (state.animeSeason.isEmpty()) {
            EmptySeasonalState(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            )
        } else {
            val selectedSeasonIndex = state.selectedSeasonIndex.coerceIn(0, state.animeSeason.size - 1)
            val (currentSeason, pagingFlow) = state.animeSeason[selectedSeasonIndex]
            val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()

            // Filter items by selected day and media type
            val selectedDayOfWeek = DayOfWeek.fromIndex(state.selectedDayIndex)
            val filteredByDay =
                remember(lazyPagingItems.itemCount, state.selectedDayIndex, state.mediaTypeFilter, state.seriesFilter) {
                    (0 until lazyPagingItems.itemCount)
                        .mapNotNull { lazyPagingItems[it] }
                        .filter { anime ->
                            val matchesDay =
                                anime.broadcast?.dayOfTheWeek?.equals(
                                    selectedDayOfWeek.apiValue,
                                    ignoreCase = true,
                                ) == true

                            val matchesMediaType =
                                when (state.mediaTypeFilter) {
                                    MediaTypeFilter.All -> true
                                    MediaTypeFilter.TV -> anime.mediaType.displayName.equals("TV", ignoreCase = true)
                                    MediaTypeFilter.ONA -> anime.mediaType.displayName.equals("ONA", ignoreCase = true)
                                    MediaTypeFilter.Movie -> anime.mediaType.displayName.equals("Movie", ignoreCase = true)
                                    MediaTypeFilter.Special -> anime.mediaType.displayName.equals("Special", ignoreCase = true)
                                    MediaTypeFilter.OVA -> anime.mediaType.displayName.equals("OVA", ignoreCase = true)
                                }

                            matchesDay && matchesMediaType
                        }
                }

            // Get top anime for featured section
            val topAnime =
                remember(lazyPagingItems.itemCount) {
                    (0 until minOf(10, lazyPagingItems.itemCount))
                        .mapNotNull { lazyPagingItems[it] }
                        .sortedByDescending { anime: AnimeForListYamal -> anime.mean ?: 0f }
                        .take(5)
                }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding =
                    PaddingValues(
                        bottom = 16.dp + navigationBarPadding.calculateBottomPadding(),
                    ),
            ) {
                // Featured/Top Anime Section
                item {
                    FeaturedSection(
                        anime = topAnime,
                        isLoading = lazyPagingItems.loadState.refresh is LoadState.Loading,
                        onAnimeClick = onAnimeClick,
                    )
                }

                // Day Selector Section
                item {
                    DaySelectorSection(
                        days = state.weekDays,
                        selectedDayIndex = state.selectedDayIndex,
                        onDaySelected = { index ->
                            presenter.processIntent(AnimeSeasonalIntent.SelectDayIndex(index))
                        },
                    )
                }

                // Content Section
                when (lazyPagingItems.loadState.refresh) {
                    is LoadState.Loading -> {
                        items(6) {
                            LoadingScheduleItem(
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            ErrorState(
                                onRetry = { lazyPagingItems.retry() },
                            )
                        }
                    }

                    else -> {
                        if (filteredByDay.isEmpty()) {
                            item {
                                EmptyDayState(dayName = selectedDayOfWeek.displayName)
                            }
                        } else {
                            when (state.viewMode) {
                                ViewMode.List -> {
                                    items(filteredByDay.size) { index ->
                                        val anime = filteredByDay[index]
                                        EnhancedScheduleItem(
                                            anime = anime,
                                            onClick = { onAnimeClick(anime.id) },
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                        )
                                    }
                                }

                                ViewMode.Grid -> {
                                    item {
                                        EnhancedAnimeGrid(
                                            items = filteredByDay,
                                            onAnimeClick = onAnimeClick,
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Append loading indicator
                when (lazyPagingItems.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                SpinLoadingIndicator()
                            }
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
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

        // Filter Bottom Sheet
        FilterBottomSheet(
            visible = showFilterSheet,
            onDismiss = { showFilterSheet = false },
            mediaTypeFilter = state.mediaTypeFilter,
            seriesFilter = state.seriesFilter,
            onMediaTypeFilterChange = { filter ->
                presenter.processIntent(AnimeSeasonalIntent.SetMediaTypeFilter(filter))
            },
            onSeriesFilterChange = { filter ->
                presenter.processIntent(AnimeSeasonalIntent.SetSeriesFilter(filter))
            },
            onReset = {
                presenter.processIntent(AnimeSeasonalIntent.SetMediaTypeFilter(MediaTypeFilter.All))
                presenter.processIntent(AnimeSeasonalIntent.SetSeriesFilter(SeriesFilter.All))
            },
        )
    }
}

@Composable
private fun SeasonalHeader(
    seasons: List<AnimeSeasonYamal>,
    selectedSeasonIndex: Int,
    onSeasonSelected: (Int) -> Unit,
    viewMode: ViewMode,
    hasActiveFilters: Boolean,
    onViewModeToggle: () -> Unit,
    onFilterClick: () -> Unit,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    val elevation by animateDpAsState(if (isCompact) 4.dp else 0.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = YamalTheme.colors.background,
        elevation = elevation,
    ) {
        Column(
            modifier =
                Modifier
                    .windowInsetsPadding(
                        WindowInsets.statusBars.only(
                            WindowInsetsSides.Horizontal + WindowInsetsSides.Top,
                        ),
                    ),
        ) {
            // Title row - following NavBar patterns
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(NavBarDefaults.Height)
                        .padding(horizontal = NavBarDefaults.HorizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Seasonal",
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // View mode toggle
                    HeaderIconButton(
                        icon = if (viewMode == ViewMode.List) Icons.Outlined.Appstore else Icons.Outlined.UnorderedList,
                        contentDescription = if (viewMode == ViewMode.List) "Grid view" else "List view",
                        onClick = onViewModeToggle,
                    )

                    // Filter button with indicator
                    Box {
                        HeaderIconButton(
                            icon = Icons.Outlined.Filter,
                            contentDescription = "Filters",
                            onClick = onFilterClick,
                            isActive = hasActiveFilters,
                        )
                        if (hasActiveFilters) {
                            Box(
                                modifier =
                                    Modifier
                                        .align(Alignment.TopEnd)
                                        .size(8.dp)
                                        .background(YamalTheme.colors.primary, CircleShape),
                            )
                        }
                    }
                }
            }

            // Season chips
            if (seasons.isNotEmpty()) {
                SeasonChipRow(
                    seasons = seasons,
                    selectedIndex = selectedSeasonIndex,
                    onSeasonSelected = onSeasonSelected,
                )
            }
        }
    }
}

@Composable
private fun HeaderIconButton(
    icon: IconPainter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
) {
    val backgroundColor by animateColorAsState(
        if (isActive) YamalTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent,
    )
    val iconTint by animateColorAsState(
        if (isActive) YamalTheme.colors.primary else YamalTheme.colors.text,
    )

    Box(
        modifier =
            modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp),
            tint = iconTint,
        )
    }
}

@Composable
private fun SeasonChipRow(
    seasons: List<AnimeSeasonYamal>,
    selectedIndex: Int,
    onSeasonSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        seasons.forEachIndexed { index, season ->
            SeasonChip(
                label = "${season.season.name.lowercase().replaceFirstChar { it.uppercase() }} ${season.year}",
                isSelected = index == selectedIndex,
                onClick = { onSeasonSelected(index) },
            )
        }
    }
}

@Composable
private fun SeasonChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) YamalTheme.colors.primary else YamalTheme.colors.box,
    )
    val textColor by animateColorAsState(
        if (isSelected) YamalTheme.colors.textLightSolid else YamalTheme.colors.text,
    )

    Surface(
        modifier =
            modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
    ) {
        Text(
            text = label,
            style = YamalTheme.typography.body,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun FeaturedSection(
    anime: List<AnimeForListYamal>,
    isLoading: Boolean,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Section header
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    icon = Icons.Filled.Fire,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = YamalTheme.colors.danger,
                )
                Text(
                    text = "Top This Season",
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )
            }
        }

        // Horizontal scroll of featured cards
        if (isLoading) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(3) {
                    FeaturedCardSkeleton()
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                itemsIndexed(anime) { index, item ->
                    FeaturedCard(
                        anime = item,
                        rank = index + 1,
                        onClick = { onAnimeClick(item.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedCard(
    anime: AnimeForListYamal,
    rank: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .width(280.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick),
    ) {
        // Background image
        AsyncImage(
            model = anime.mainPicture?.large ?: anime.mainPicture?.medium,
            contentDescription = anime.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // Gradient overlay
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    Color.Black.copy(alpha = 0.8f),
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Transparent,
                                ),
                        ),
                    ),
        )

        // Content
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Rank badge
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            when (rank) {
                                1 -> Color(0xFFFFD700)
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> YamalTheme.colors.primary
                            },
                            RoundedCornerShape(12.dp),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "#$rank",
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title and info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = anime.title,
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    anime.mean?.let { score ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                icon = Icons.Filled.Star,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFFFFC107),
                            )
                            Text(
                                text = score.formatOneDecimal(),
                                style = YamalTheme.typography.body,
                                color = Color.White,
                            )
                        }
                    }

                    Text(
                        text = "•",
                        color = Color.White.copy(alpha = 0.6f),
                    )

                    Text(
                        text = "${anime.mediaType.displayName} • ${anime.numberOfEpisodes ?: "?"} eps",
                        style = YamalTheme.typography.body,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedCardSkeleton(modifier: Modifier = Modifier) {
    Skeleton(
        modifier = modifier.width(280.dp).height(160.dp),
        borderRadius = 16.dp,
    )
}

@Composable
private fun DaySelectorSection(
    days: List<Day>,
    selectedDayIndex: Int,
    onDaySelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val todayIndex = remember { getCurrentDayIndex() }

    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Section header
        Text(
            text = "Airing Schedule",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        // Day chips
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            days.forEachIndexed { index, day ->
                DayChip(
                    dayShort = day.dayOfWeek,
                    dayFull = DayOfWeek.fromIndex(index).displayName,
                    isSelected = index == selectedDayIndex,
                    isToday = index == todayIndex,
                    onClick = { onDaySelected(index) },
                )
            }
        }
    }
}

@Composable
private fun DayChip(
    dayShort: String,
    dayFull: String,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        when {
            isSelected -> YamalTheme.colors.primary
            isToday -> YamalTheme.colors.primary.copy(alpha = 0.1f)
            else -> YamalTheme.colors.box
        },
    )
    val textColor by animateColorAsState(
        when {
            isSelected -> YamalTheme.colors.textLightSolid
            isToday -> YamalTheme.colors.primary
            else -> YamalTheme.colors.text
        },
    )
    val borderColor by animateColorAsState(
        if (isToday && !isSelected) YamalTheme.colors.primary else Color.Transparent,
    )

    Column(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dayShort.uppercase(),
            style = YamalTheme.typography.body,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
        if (isToday) {
            Text(
                text = "TODAY",
                style = YamalTheme.typography.tiny,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else YamalTheme.colors.primary,
            )
        }
    }
}

@Composable
private fun EnhancedScheduleItem(
    anime: AnimeForListYamal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.box,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Poster
            Box(
                modifier =
                    Modifier
                        .size(width = 64.dp, height = 90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YamalTheme.colors.border),
            ) {
                AsyncImage(
                    model = anime.mainPicture?.medium ?: anime.mainPicture?.large,
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            // Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = anime.title,
                    style = YamalTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "${anime.mediaType.displayName} • ${anime.numberOfEpisodes ?: "?"} episodes",
                    style = YamalTheme.typography.caption,
                    color = YamalTheme.colors.textSecondary,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Rating
                    anime.mean?.let { score ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                icon = Icons.Filled.Star,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFFFFC107),
                            )
                            Text(
                                text = score.formatOneDecimal(),
                                style = YamalTheme.typography.caption,
                                fontWeight = FontWeight.Bold,
                                color = YamalTheme.colors.text,
                            )
                        }
                    }
                }
            }

            // Air time
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    icon = Icons.Outlined.ClockCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = YamalTheme.colors.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = anime.broadcast?.startTime ?: "--:--",
                    style = YamalTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.primary,
                )
            }
        }
    }
}

@Composable
private fun LoadingScheduleItem(modifier: Modifier = Modifier) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.box,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Skeleton(
                modifier = Modifier.size(width = 64.dp, height = 90.dp),
                borderRadius = 8.dp,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Skeleton(modifier = Modifier.fillMaxWidth(0.8f), height = 16.dp, borderRadius = 4.dp)
                Skeleton(modifier = Modifier.fillMaxWidth(0.5f), height = 12.dp, borderRadius = 4.dp)
                Skeleton(modifier = Modifier.fillMaxWidth(0.3f), height = 12.dp, borderRadius = 4.dp)
            }

            Skeleton(modifier = Modifier.size(40.dp), borderRadius = 8.dp)
        }
    }
}

@Composable
private fun EnhancedAnimeGrid(
    items: List<AnimeForListYamal>,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { anime ->
                    AnimeCard(
                        imageUrl = anime.mainPicture?.medium ?: anime.mainPicture?.large,
                        title = anime.title,
                        metadata = "${anime.broadcast?.startTime ?: "--:--"} • ${anime.mediaType.displayName}",
                        rating = anime.mean,
                        width = 110.dp,
                        onClick = { onAnimeClick(anime.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
                // Fill empty spaces
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun FilterBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    mediaTypeFilter: MediaTypeFilter,
    seriesFilter: SeriesFilter,
    onMediaTypeFilterChange: (MediaTypeFilter) -> Unit,
    onSeriesFilterChange: (SeriesFilter) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YamalBottomSheet(
        visible = visible,
        onDismiss = onDismiss,
        showHandleBar = true,
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimension.Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Filters",
                    style = YamalTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )

                YamalButton(
                    text = "Reset",
                    onClick = onReset,
                    color = ButtonColor.Default,
                    fill = ButtonFill.None,
                )
            }

            // Media Type Filter
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Media Type",
                    style = YamalTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )

                Selector(
                    options =
                        MediaTypeFilter.entries.map { filter ->
                            SelectorOption(
                                label = filter.name,
                                value = filter,
                            )
                        },
                    value = listOf(mediaTypeFilter),
                    onValueChange = { values, _ ->
                        values.firstOrNull()?.let { onMediaTypeFilterChange(it) }
                    },
                    columns = 3,
                    showCheckMark = false,
                )
            }

            // Series Filter
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Series Type",
                    style = YamalTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )

                Selector(
                    options =
                        listOf(
                            SelectorOption(label = "All", value = SeriesFilter.All),
                            SelectorOption(label = "New Episodes", value = SeriesFilter.NewEpisodesOnly),
                            SelectorOption(label = "Continuing", value = SeriesFilter.ContinuingSeries),
                        ),
                    value = listOf(seriesFilter),
                    onValueChange = { values, _ ->
                        values.firstOrNull()?.let { onSeriesFilterChange(it) }
                    },
                    columns = 3,
                    showCheckMark = false,
                )
            }

            // Apply button
            YamalButton(
                text = "Apply Filters",
                onClick = onDismiss,
                color = ButtonColor.Primary,
                block = true,
            )

            Spacer(modifier = Modifier.height(Dimension.Spacing.md))
        }
    }
}

@Composable
private fun EmptySeasonalState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Empty(
            imageContent = {
                Icon(
                    icon = Icons.Outlined.Calendar,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = YamalTheme.colors.textSecondary,
                )
            },
            description = "No seasonal anime available",
        )
    }
}

@Composable
private fun EmptyDayState(
    dayName: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                icon = Icons.Outlined.Calendar,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = YamalTheme.colors.textSecondary,
            )
            Text(
                text = "No releases on $dayName",
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                icon = Icons.Outlined.CloseCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = YamalTheme.colors.danger,
            )
            Text(
                text = "Failed to load seasonal anime",
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
            YamalButton(
                text = "Try Again",
                onClick = onRetry,
                color = ButtonColor.Primary,
            )
        }
    }
}
