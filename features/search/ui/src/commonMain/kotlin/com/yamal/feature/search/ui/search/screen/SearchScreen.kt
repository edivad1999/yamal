package com.yamal.feature.search.ui.search.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonShape
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.empty.Empty
import com.yamal.designSystem.components.input.SearchField
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.tag.Tag
import com.yamal.designSystem.components.tag.TagColor
import com.yamal.designSystem.components.tag.TagFill
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.MangaForListYamal
import com.yamal.feature.search.api.model.SearchType
import com.yamal.feature.search.ui.search.presenter.SearchIntent
import com.yamal.feature.search.ui.search.presenter.SearchPresenter
import com.yamal.platform.utils.formatOneDecimal
import org.koin.compose.koinInject

@Composable
fun SearchScreen(
    onNavigateToAnimeDetails: (Int) -> Unit,
    onNavigateToMangaDetails: (Int) -> Unit,
    presenter: SearchPresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()

    YamalScaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            // Search bar with clear button
            SearchField(
                value = state.query,
                onValueChange = { presenter.processIntent(SearchIntent.UpdateQuery(it)) },
                placeholder = "Search anime or manga...",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
            )

            Spacer(Modifier.height(12.dp))

            // Search type toggle chips
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YamalButton(
                    text = "Anime",
                    onClick = { presenter.processIntent(SearchIntent.ChangeSearchType(SearchType.ANIME)) },
                    color = if (state.searchType == SearchType.ANIME) ButtonColor.Primary else ButtonColor.Default,
                    fill = if (state.searchType == SearchType.ANIME) ButtonFill.Solid else ButtonFill.Outline,
                    size = ButtonSize.Small,
                    shape = ButtonShape.Rounded,
                )

                YamalButton(
                    text = "Manga",
                    onClick = { presenter.processIntent(SearchIntent.ChangeSearchType(SearchType.MANGA)) },
                    color = if (state.searchType == SearchType.MANGA) ButtonColor.Primary else ButtonColor.Default,
                    fill = if (state.searchType == SearchType.MANGA) ButtonFill.Solid else ButtonFill.Outline,
                    size = ButtonSize.Small,
                    shape = ButtonShape.Rounded,
                )
            }

            Spacer(Modifier.height(8.dp))

            // Show initial empty state if no query
            if (state.query.isBlank()) {
                InitialEmptyState(
                    searchType = state.searchType,
                    modifier = Modifier.weight(1f),
                )
            } else {
                // Results
                when (state.searchType) {
                    SearchType.ANIME -> {
                        AnimeSearchResults(
                            results = state.animeResults,
                            query = state.query,
                            onAnimeClick = onNavigateToAnimeDetails,
                            onRetry = { presenter.processIntent(SearchIntent.UpdateQuery(state.query)) },
                        )
                    }

                    SearchType.MANGA -> {
                        MangaSearchResults(
                            results = state.mangaResults,
                            query = state.query,
                            onMangaClick = onNavigateToMangaDetails,
                            onRetry = { presenter.processIntent(SearchIntent.UpdateQuery(state.query)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InitialEmptyState(
    searchType: SearchType,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Empty(
            imageContent = {
                Icon(
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = YamalTheme.colors.weak,
                )
            },
            description = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Search for ${if (searchType == SearchType.ANIME) "anime" else "manga"}",
                        style = YamalTheme.typography.title,
                        color = YamalTheme.colors.text,
                    )
                    Text(
                        text = "Find your favorite titles by name",
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.textSecondary,
                    )
                }
            },
        )
    }
}

@Composable
private fun NoResultsState(
    query: String,
    searchType: SearchType,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Empty(
            imageContent = {
                Icon(
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = YamalTheme.colors.weak,
                )
            },
            description = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "No ${if (searchType == SearchType.ANIME) "anime" else "manga"} found",
                        style = YamalTheme.typography.title,
                        color = YamalTheme.colors.text,
                    )
                    Text(
                        text = "We couldn't find results for \"$query\"",
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.textSecondary,
                    )
                    Text(
                        text = "Try different keywords or check spelling",
                        style = YamalTheme.typography.small,
                        color = YamalTheme.colors.weak,
                    )
                }
            },
        )
    }
}

@Composable
private fun SearchResultSkeleton() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Poster skeleton
        Skeleton(
            modifier =
                Modifier
                    .width(70.dp)
                    .aspectRatio(2f / 3f),
            borderRadius = 8.dp,
        )

        // Content skeleton
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Title
            Skeleton(
                modifier = Modifier.fillMaxWidth(0.8f),
                height = 18.dp,
                borderRadius = 4.dp,
            )
            // Subtitle
            Skeleton(
                modifier = Modifier.fillMaxWidth(0.6f),
                height = 14.dp,
                borderRadius = 4.dp,
            )
            // Tags row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Skeleton(width = 50.dp, height = 20.dp, borderRadius = 4.dp)
                Skeleton(width = 40.dp, height = 20.dp, borderRadius = 4.dp)
            }
        }
    }
}

@Composable
private fun AnimeSearchResults(
    results: kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<AnimeForListYamal>>,
    query: String,
    onAnimeClick: (Int) -> Unit,
    onRetry: () -> Unit,
) {
    val lazyPagingItems = results.collectAsLazyPagingItems()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 16.dp + navigationBarPadding.calculateBottomPadding(),
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Handle different load states
        when (val refreshState = lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                // Skeleton loading
                items(6) {
                    SearchResultSkeleton()
                }
            }

            is LoadState.Error -> {
                item {
                    ErrorState(
                        message = refreshState.error.message ?: "Failed to load results",
                        onRetry = onRetry,
                    )
                }
            }

            is LoadState.NotLoading -> {
                if (lazyPagingItems.itemCount == 0) {
                    item {
                        NoResultsState(query = query, searchType = SearchType.ANIME)
                    }
                } else {
                    items(lazyPagingItems.itemCount) { index ->
                        lazyPagingItems[index]?.let { anime ->
                            AnimeSearchResultCard(
                                anime = anime,
                                onClick = { onAnimeClick(anime.id) },
                            )
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
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Error loading more",
                            color = YamalTheme.colors.danger,
                            style = YamalTheme.typography.small,
                        )
                        Spacer(Modifier.width(8.dp))
                        YamalButton(
                            text = "Retry",
                            onClick = { lazyPagingItems.retry() },
                            size = ButtonSize.Small,
                            color = ButtonColor.Default,
                        )
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun MangaSearchResults(
    results: kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<MangaForListYamal>>,
    query: String,
    onMangaClick: (Int) -> Unit,
    onRetry: () -> Unit,
) {
    val lazyPagingItems = results.collectAsLazyPagingItems()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 16.dp + navigationBarPadding.calculateBottomPadding(),
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Handle different load states
        when (val refreshState = lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                // Skeleton loading
                items(6) {
                    SearchResultSkeleton()
                }
            }

            is LoadState.Error -> {
                item {
                    ErrorState(
                        message = refreshState.error.message ?: "Failed to load results",
                        onRetry = onRetry,
                    )
                }
            }

            is LoadState.NotLoading -> {
                if (lazyPagingItems.itemCount == 0) {
                    item {
                        NoResultsState(query = query, searchType = SearchType.MANGA)
                    }
                } else {
                    items(lazyPagingItems.itemCount) { index ->
                        lazyPagingItems[index]?.let { manga ->
                            MangaSearchResultCard(
                                manga = manga,
                                onClick = { onMangaClick(manga.id) },
                            )
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
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Error loading more",
                            color = YamalTheme.colors.danger,
                            style = YamalTheme.typography.small,
                        )
                        Spacer(Modifier.width(8.dp))
                        YamalButton(
                            text = "Retry",
                            onClick = { lazyPagingItems.retry() },
                            size = ButtonSize.Small,
                            color = ButtonColor.Default,
                        )
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Empty(
            imageContent = {
                Icon(
                    icon = Icons.Outlined.CloseCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = YamalTheme.colors.danger,
                )
            },
            description = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Something went wrong",
                        style = YamalTheme.typography.title,
                        color = YamalTheme.colors.text,
                    )
                    Text(
                        text = message,
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.textSecondary,
                    )
                    YamalButton(
                        text = "Try Again",
                        onClick = onRetry,
                        color = ButtonColor.Primary,
                    )
                }
            },
        )
    }
}

@Composable
private fun AnimeSearchResultCard(
    anime: AnimeForListYamal,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // Poster
            Box(
                modifier =
                    Modifier
                        .width(70.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YamalTheme.colors.box),
            ) {
                AsyncImage(
                    model = anime.mainPicture?.medium ?: anime.mainPicture?.large,
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                // Rating badge
                anime.mean?.let { rating ->
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(
                                    color = YamalTheme.colors.warning.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(4.dp),
                                ).padding(horizontal = 4.dp, vertical = 2.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                icon = Icons.Filled.Star,
                                contentDescription = null,
                                tint = YamalTheme.colors.text,
                                modifier = Modifier.size(10.dp),
                            )
                            Text(
                                text = rating.formatOneDecimal(),
                                style = YamalTheme.typography.tiny,
                                fontWeight = FontWeight.Bold,
                                color = YamalTheme.colors.text,
                            )
                        }
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = anime.title,
                    style = YamalTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = YamalTheme.colors.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                // Metadata row
                Text(
                    text =
                        buildString {
                            append(anime.mediaType.displayName)
                            anime.numberOfEpisodes?.let { append(" • $it eps") }
                            anime.startDate?.let { append(" • $it") }
                        },
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Tags row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Tag(
                        text = anime.mediaType.displayName,
                        color = TagColor.Primary,
                        fill = TagFill.Outline,
                    )

                    anime.rank?.let { rank ->
                        if (rank <= 100) {
                            Tag(
                                text = "#$rank",
                                color = TagColor.Warning,
                                fill = TagFill.Solid,
                            )
                        }
                    }
                }
            }

            // Chevron indicator
            Icon(
                icon = Icons.Outlined.ArrowRight,
                contentDescription = null,
                tint = YamalTheme.colors.weak,
                modifier =
                    Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
private fun MangaSearchResultCard(
    manga: MangaForListYamal,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // Poster
            Box(
                modifier =
                    Modifier
                        .width(70.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YamalTheme.colors.box),
            ) {
                AsyncImage(
                    model = manga.mainPicture?.medium ?: manga.mainPicture?.large,
                    contentDescription = manga.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                // Rating badge
                manga.mean?.let { rating ->
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(
                                    color = YamalTheme.colors.warning.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(4.dp),
                                ).padding(horizontal = 4.dp, vertical = 2.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                icon = Icons.Filled.Star,
                                contentDescription = null,
                                tint = YamalTheme.colors.text,
                                modifier = Modifier.size(10.dp),
                            )
                            Text(
                                text = rating.formatOneDecimal(),
                                style = YamalTheme.typography.tiny,
                                fontWeight = FontWeight.Bold,
                                color = YamalTheme.colors.text,
                            )
                        }
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = manga.title,
                    style = YamalTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = YamalTheme.colors.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                // Metadata row
                Text(
                    text =
                        buildString {
                            append(manga.mediaType.displayName)
                            manga.numberOfChapters?.let { append(" • $it ch") }
                            manga.numberOfVolumes?.let { append(" • $it vols") }
                        },
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Tags row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Tag(
                        text = "Manga",
                        color = TagColor.Primary,
                        fill = TagFill.Outline,
                    )

                    manga.rank?.let { rank ->
                        if (rank <= 100) {
                            Tag(
                                text = "#$rank",
                                color = TagColor.Warning,
                                fill = TagFill.Solid,
                            )
                        }
                    }
                }
            }

            // Chevron indicator
            Icon(
                icon = Icons.Outlined.ArrowRight,
                contentDescription = null,
                tint = YamalTheme.colors.weak,
                modifier =
                    Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
            )
        }
    }
}
