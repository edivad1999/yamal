package com.yamal.feature.anime.ui.animeDetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.addToList.AddToListBottomSheet
import com.yamal.designSystem.components.addToList.AddToListState
import com.yamal.designSystem.components.addToList.AnimeListStatus
import com.yamal.designSystem.components.animeCard.AnimeCard
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.sectionHeader.SectionHeader
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.tag.Tag
import com.yamal.designSystem.components.tag.TagColor
import com.yamal.designSystem.components.tag.TagFill
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.AnimeCharacterYamal
import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeListStatusYamal
import com.yamal.feature.anime.api.model.AnimeRecommendationYamal
import com.yamal.feature.anime.api.model.AnimeReviewYamal
import com.yamal.feature.anime.api.model.ExternalLinkYamal
import com.yamal.feature.anime.api.model.MangaForListYamal
import com.yamal.feature.anime.api.model.MediaTypeYamal
import com.yamal.feature.anime.api.model.PictureYamal
import com.yamal.feature.anime.api.model.RelatedItemYamal
import com.yamal.feature.anime.api.model.ThemeSongsYamal
import com.yamal.feature.anime.api.model.TrailerYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.feature.anime.ui.animeDetails.presenter.AnimeDetailsIntent
import com.yamal.feature.anime.ui.animeDetails.presenter.AnimeDetailsPresenter
import com.yamal.feature.login.api.LoginRepository
import com.yamal.platform.utils.formatOneDecimal
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun AnimeDetailsScreen(
    animeId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetails: (Int) -> Unit = {},
    onNavigateToMangaDetails: (Int) -> Unit = {},
    onOpenUrl: (String) -> Unit = {},
    presenter: AnimeDetailsPresenter = koinInject { parametersOf(animeId) },
    loginRepository: LoginRepository = koinInject(),
) {
    val state by presenter.state.collectAsState()
    val isLoggedIn by loginRepository.isUserAuthenticated().collectAsState(initial = false)
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(animeId) {
        presenter.processIntent(AnimeDetailsIntent.Refresh)
    }

    // Close bottom sheet on successful update
    LaunchedEffect(state.listUpdateSuccess) {
        if (state.listUpdateSuccess) {
            showBottomSheet = false
            presenter.processIntent(AnimeDetailsIntent.ClearListUpdateState)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.loading -> {
                AnimeDetailsSkeletonContent(onNavigateBack = onNavigateBack)
            }

            state.error != null -> {
                Column(
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        icon = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = YamalTheme.colors.danger,
                        modifier = Modifier.size(48.dp),
                    )
                    Text(
                        text = "Failed to load anime details",
                        style = YamalTheme.typography.titleLarge,
                        color = YamalTheme.colors.text,
                    )
                    Text(
                        text = state.error ?: "Unknown error",
                        color = YamalTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                    YamalButton(
                        text = "Try Again",
                        onClick = { presenter.processIntent(AnimeDetailsIntent.Refresh) },
                        color = ButtonColor.Primary,
                    )
                }
            }

            state.anime != null -> {
                AnimeDetailsContent(
                    anime = state.anime!!,
                    onNavigateBack = onNavigateBack,
                    onAddToListClick = { showBottomSheet = true },
                    onAnimeClick = onNavigateToAnimeDetails,
                    onMangaClick = onNavigateToMangaDetails,
                    onOpenUrl = onOpenUrl,
                    isLoggedIn = isLoggedIn,
                )
            }
        }

        // Bottom sheet for adding to list
        AddToListBottomSheet(
            visible = showBottomSheet,
            onDismiss = { showBottomSheet = false },
            initialState =
                state.anime?.let { anime ->
                    AddToListState(
                        status =
                            anime.myListStatus?.status?.let { statusString ->
                                AnimeListStatus.entries.find { it.serialName == statusString }
                            },
                        score = anime.myListStatus?.score ?: 0,
                        episodesWatched = anime.myListStatus?.numEpisodesWatched ?: 0,
                        totalEpisodes = anime.numEpisodes,
                        isInList = anime.myListStatus != null,
                    )
                } ?: AddToListState(),
            isLoggedIn = isLoggedIn,
            onSave = { status, score, episodesWatched ->
                presenter.processIntent(
                    AnimeDetailsIntent.UpdateListStatus(
                        status = status?.let { UserListStatusYamal.fromSerializedValue(it.serialName) },
                        score = score,
                        episodesWatched = episodesWatched,
                    ),
                )
            },
            onDelete = {
                presenter.processIntent(AnimeDetailsIntent.DeleteFromList)
            },
            onLoginClick = {
                onNavigateBack()
            },
        )
    }
}

@Composable
private fun AnimeDetailsSkeletonContent(onNavigateBack: () -> Unit) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
        ) {
            // Header skeleton
            Skeleton(
                modifier = Modifier.fillMaxWidth().height(280.dp),
                borderRadius = 0.dp,
            )

            // Content skeleton
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .offset(y = (-80).dp)
                        .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // Poster skeleton
                Skeleton(
                    modifier =
                        Modifier
                            .width(192.dp)
                            .aspectRatio(2f / 3f),
                    borderRadius = 12.dp,
                )

                // Title skeleton
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Skeleton(width = 250.dp, height = 28.dp, borderRadius = 4.dp)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Skeleton(width = 60.dp, height = 24.dp, borderRadius = 4.dp)
                        Skeleton(width = 50.dp, height = 24.dp, borderRadius = 4.dp)
                        Skeleton(width = 60.dp, height = 24.dp, borderRadius = 4.dp)
                    }
                }

                // Synopsis skeleton
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Skeleton(modifier = Modifier.fillMaxWidth(), height = 16.dp, borderRadius = 4.dp)
                    Skeleton(modifier = Modifier.fillMaxWidth(), height = 16.dp, borderRadius = 4.dp)
                    Skeleton(modifier = Modifier.fillMaxWidth(0.7f), height = 16.dp, borderRadius = 4.dp)
                }

                // Stats skeleton
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(3) {
                        Skeleton(
                            modifier = Modifier.weight(1f).height(72.dp),
                            borderRadius = 12.dp,
                        )
                    }
                }

                // Info skeleton
                Skeleton(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    borderRadius = 12.dp,
                )
            }
        }

        // Back button
        BackButton(onNavigateBack = onNavigateBack)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimeDetailsContent(
    anime: AnimeForDetailsYamal,
    onNavigateBack: () -> Unit,
    onAddToListClick: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    onMangaClick: (Int) -> Unit,
    onOpenUrl: (String) -> Unit,
    isLoggedIn: Boolean,
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
        ) {
            // Header with backdrop image
            Box(
                modifier = Modifier.fillMaxWidth().height(280.dp),
            ) {
                // Backdrop image
                AsyncImage(
                    model = anime.mainPicture?.large ?: anime.mainPicture?.medium,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                // Gradient overlay
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            YamalTheme.colors.text.copy(alpha = 0.2f),
                                            Color.Transparent,
                                            YamalTheme.colors.background,
                                        ),
                                ),
                            ),
                )
            }

            // Main content with negative margin to overlap header
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .offset(y = (-80).dp)
                        .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // Poster and title section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    // Poster
                    Surface(
                        modifier =
                            Modifier
                                .width(192.dp)
                                .aspectRatio(2f / 3f)
                                .shadow(
                                    elevation = 12.dp,
                                    shape = RoundedCornerShape(12.dp),
                                ),
                        shape = RoundedCornerShape(12.dp),
                        color = YamalTheme.colors.background,
                    ) {
                        AsyncImage(
                            model = anime.mainPicture?.large ?: anime.mainPicture?.medium,
                            contentDescription = anime.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    // Title and metadata
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = anime.title,
                            style = YamalTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = YamalTheme.colors.text,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )

                        // Type, Year, Rating row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // Type badge
                            Tag(
                                text = anime.mediaType.name,
                                color = TagColor.Default,
                                fill = TagFill.Solid,
                                modifier = Modifier,
                            )

                            // Year
                            anime.startSeason?.let { season ->
                                Text(
                                    text = season.year,
                                    style = YamalTheme.typography.body,
                                    color = YamalTheme.colors.text,
                                )
                            }

                            // Rating
                            anime.mean?.let { score ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        icon = Icons.Outlined.Star,
                                        contentDescription = null,
                                        tint = YamalTheme.colors.primary,
                                        modifier = Modifier.size(18.dp),
                                    )
                                    Text(
                                        text = score.formatOneDecimal(),
                                        style = YamalTheme.typography.body,
                                        fontWeight = FontWeight.Bold,
                                        color = YamalTheme.colors.text,
                                    )
                                }
                            }
                        }
                    }
                }

                // Synopsis section
                anime.synopsis?.let { synopsis ->
                    ExpandableSynopsis(synopsis = synopsis)
                }

                // Add to List and Progress section
                AddToListSection(
                    episodesWatched = anime.myListStatus?.numEpisodesWatched ?: 0,
                    totalEpisodes = anime.numEpisodes,
                    isLoggedIn = isLoggedIn,
                    isInList = anime.myListStatus != null,
                    currentStatus = anime.myListStatus?.status,
                    onAddToListClick = onAddToListClick,
                )

                // Stats row (Rank, Popularity, Members)
                StatsRow(
                    rank = anime.rank,
                    popularity = anime.popularity,
                    members = anime.numListUsers,
                )

                // Info section
                InfoSection(anime = anime)

                // Alternative Titles Section
                anime.alternativeTitles?.let { altTitles ->
                    if (altTitles.en != null || altTitles.ja != null || altTitles.synonyms.isNotEmpty()) {
                        AlternativeTitlesSection(alternativeTitles = altTitles)
                    }
                }

                // Statistics breakdown
                anime.statistics?.let { stats ->
                    StatisticsSection(statistics = stats)
                }

                // Trailer Section (from Jikan)
                anime.trailer?.let { trailer ->
                    if (trailer.youtubeId != null || trailer.url != null) {
                        TrailerSection(trailer = trailer, onOpenUrl = onOpenUrl)
                    }
                }

                // Characters Section (from Jikan)
                if (anime.characters.isNotEmpty()) {
                    CharactersSection(characters = anime.characters)
                }

                // Related Anime Section
                if (anime.relatedAnime.isNotEmpty()) {
                    RelatedAnimeSection(
                        relatedAnime = anime.relatedAnime,
                        onAnimeClick = onAnimeClick,
                    )
                }

                // Recommendations Section
                if (anime.recommendations.isNotEmpty()) {
                    RecommendationsSection(
                        recommendations = anime.recommendations,
                        onAnimeClick = onAnimeClick,
                    )
                }

                // Reviews Section (from Jikan)
                if (anime.reviews.isNotEmpty()) {
                    ReviewsSection(reviews = anime.reviews)
                }

                // Theme Songs Section (from Jikan)
                anime.themeSongs?.let { themeSongs ->
                    if (themeSongs.openings.isNotEmpty() || themeSongs.endings.isNotEmpty()) {
                        ThemeSongsSection(themeSongs = themeSongs)
                    }
                }

                // Streaming Links Section (from Jikan)
                if (anime.streamingLinks.isNotEmpty()) {
                    StreamingLinksSection(
                        streamingLinks = anime.streamingLinks,
                        onOpenUrl = onOpenUrl,
                    )
                }

                // External Links Section (from Jikan)
                if (anime.externalLinks.isNotEmpty()) {
                    ExternalLinksSection(
                        externalLinks = anime.externalLinks,
                        onOpenUrl = onOpenUrl,
                    )
                }

                // Image Gallery Section
                if (anime.pictures.isNotEmpty()) {
                    ImageGallerySection(pictures = anime.pictures)
                }

                // Related Manga Section
                if (anime.relatedManga.isNotEmpty()) {
                    RelatedMangaSection(
                        relatedManga = anime.relatedManga,
                        onMangaClick = onMangaClick,
                    )
                }

                // User Personal Info Section
                anime.myListStatus?.let { listStatus ->
                    if (isLoggedIn && hasPersonalInfo(listStatus)) {
                        UserPersonalInfoSection(listStatus = listStatus)
                    }
                }

                // Background info
                anime.background?.let { background ->
                    if (background.isNotBlank()) {
                        BackgroundSection(background = background)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Floating back button
        BackButton(onNavigateBack = onNavigateBack)
    }
}

@Composable
private fun BackButton(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
    ) {
        Surface(
            modifier =
                Modifier
                    .size(40.dp)
                    .clickable(onClick = onNavigateBack),
            shape = RoundedCornerShape(20.dp),
            color = Color.Black.copy(alpha = 0.4f),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon = Icons.Outlined.ArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
private fun ExpandableSynopsis(synopsis: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val maxLines = if (isExpanded) Int.MAX_VALUE else 4

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Synopsis",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        Box {
            Text(
                text = synopsis,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            if (!isExpanded) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            Color.Transparent,
                                            YamalTheme.colors.background,
                                        ),
                                ),
                            ),
                )
            }
        }

        YamalButton(
            text = if (isExpanded) "READ LESS" else "READ MORE",
            onClick = { isExpanded = !isExpanded },
            color = ButtonColor.Primary,
            fill = ButtonFill.None,
            size = ButtonSize.Small,
        )
    }
}

@Composable
private fun AddToListSection(
    episodesWatched: Int,
    totalEpisodes: Int,
    isLoggedIn: Boolean,
    isInList: Boolean,
    currentStatus: String?,
    onAddToListClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (!isLoggedIn) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    icon = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = YamalTheme.colors.textSecondary,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = "Sign in to track your progress",
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.textSecondary,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Add to List button
            Surface(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clickable(onClick = onAddToListClick)
                        .border(
                            width = 2.dp,
                            color = if (isInList) YamalTheme.colors.primary else YamalTheme.colors.border,
                            shape = RoundedCornerShape(12.dp),
                        ),
                shape = RoundedCornerShape(12.dp),
                color = if (isInList) YamalTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            icon = if (isInList) Icons.Outlined.Check else Icons.Outlined.Plus,
                            contentDescription = null,
                            tint = YamalTheme.colors.primary,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text =
                                if (isInList) {
                                    currentStatus?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "In List"
                                } else {
                                    "Add to List"
                                },
                            style = YamalTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            color = if (isInList) YamalTheme.colors.primary else YamalTheme.colors.text,
                        )
                    }

                    // Lock icon only shown when not logged in
                    if (!isLoggedIn) {
                        Icon(
                            icon = Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = YamalTheme.colors.weak,
                            modifier =
                                Modifier
                                    .size(14.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp),
                        )
                    }
                }
            }

            // Progress display
            Surface(
                modifier =
                    Modifier
                        .weight(0.5f)
                        .height(56.dp)
                        .border(
                            width = 2.dp,
                            color = YamalTheme.colors.border,
                            shape = RoundedCornerShape(12.dp),
                        ),
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Progress",
                        style = YamalTheme.typography.small,
                        color = YamalTheme.colors.textSecondary,
                    )
                    Text(
                        text = if (isInList) "$episodesWatched / $totalEpisodes" else "-- / $totalEpisodes",
                        style = YamalTheme.typography.body,
                        fontWeight = FontWeight.Bold,
                        color = YamalTheme.colors.text,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsRow(
    rank: Int?,
    popularity: Int?,
    members: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatCard(
            label = "Rank",
            value = rank?.let { "#$it" } ?: "--",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = "Popularity",
            value = popularity?.let { "#$it" } ?: "--",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = "Members",
            value = formatMembers(members),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label.uppercase(),
                style = YamalTheme.typography.small,
                color = YamalTheme.colors.textSecondary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InfoSection(anime: AnimeForDetailsYamal) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Information",
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )

            // Type and Episodes
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                InfoItem(
                    label = "Type",
                    value = formatMediaType(anime.mediaType),
                    modifier = Modifier.weight(1f),
                )
                InfoItem(
                    label = "Episodes",
                    value = if (anime.numEpisodes > 0) anime.numEpisodes.toString() else "Unknown",
                    modifier = Modifier.weight(1f),
                )
            }

            // Status and Aired
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                InfoItem(
                    label = "Status",
                    value = formatStatus(anime.status),
                    modifier = Modifier.weight(1f),
                )
                InfoItem(
                    label = "Aired",
                    value = formatAiredDate(anime.startDate, anime.endDate),
                    modifier = Modifier.weight(1f),
                )
            }

            // Source and Duration
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                InfoItem(
                    label = "Source",
                    value = anime.source?.replaceFirstChar { it.uppercase() }?.replace("_", " ") ?: "Unknown",
                    modifier = Modifier.weight(1f),
                )
                InfoItem(
                    label = "Duration",
                    value = anime.averageEpisodeDuration?.let { formatDuration(it) } ?: "Unknown",
                    modifier = Modifier.weight(1f),
                )
            }

            // Rating (age rating) and Broadcast
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                InfoItem(
                    label = "Rating",
                    value = anime.rating?.let { formatRating(it) } ?: "Unknown",
                    modifier = Modifier.weight(1f),
                )
                anime.broadcast?.let { broadcast ->
                    InfoItem(
                        label = "Broadcast",
                        value = "${broadcast.dayOfTheWeek.replaceFirstChar { it.uppercase() }}${broadcast.startTime?.let { " at $it" } ?: ""}",
                        modifier = Modifier.weight(1f),
                    )
                } ?: InfoItem(
                    label = "Season",
                    value = anime.startSeason?.let { "${it.season.name} ${it.year}" } ?: "Unknown",
                    modifier = Modifier.weight(1f),
                )
            }

            // Studios
            if (anime.studios.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Studios".uppercase(),
                        style = YamalTheme.typography.small,
                        color = YamalTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = anime.studios.joinToString(", ") { it.name },
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.primary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            // Genres
            if (anime.genres.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Genres".uppercase(),
                        style = YamalTheme.typography.small,
                        color = YamalTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        anime.genres.forEach { genre ->
                            Tag(
                                text = genre.name,
                                color = TagColor.Default,
                                fill = TagFill.Outline,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatMediaType(mediaType: MediaTypeYamal): String =
    when (mediaType) {
        MediaTypeYamal.Tv -> "TV"
        MediaTypeYamal.Movie -> "Movie"
        MediaTypeYamal.Ova -> "OVA"
        MediaTypeYamal.Ona -> "ONA"
        MediaTypeYamal.Special -> "Special"
        MediaTypeYamal.Music -> "Music"
        MediaTypeYamal.Unknown -> "Unknown"
    }

private fun formatStatus(status: String): String =
    status
        .replace("_", " ")
        .split(" ")
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

private fun formatAiredDate(
    startDate: String?,
    endDate: String?,
): String {
    if (startDate == null) return "Unknown"
    return if (endDate != null && endDate != startDate) {
        "$startDate to $endDate"
    } else {
        startDate
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    return if (minutes >= 60) {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        "${hours}h ${remainingMinutes}m"
    } else {
        "${minutes}m per ep"
    }
}

private fun formatRating(rating: String): String =
    when (rating) {
        "g" -> "G - All Ages"
        "pg" -> "PG - Children"
        "pg_13" -> "PG-13 - Teens 13+"
        "r" -> "R - 17+"
        "r+" -> "R+ - Mild Nudity"
        "rx" -> "Rx - Hentai"
        else -> rating.uppercase()
    }

@Composable
private fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label.uppercase(),
            style = YamalTheme.typography.small,
            color = YamalTheme.colors.textSecondary,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            style = YamalTheme.typography.body,
            color = YamalTheme.colors.text,
            fontWeight = FontWeight.Medium,
        )
    }
}

private fun formatMembers(members: Int): String =
    when {
        members >= 1_000_000 -> "${(members / 1_000_000.0).formatOneDecimal()}M"
        members >= 1_000 -> "${members / 1_000}k"
        else -> members.toString()
    }

@Composable
private fun AlternativeTitlesSection(alternativeTitles: com.yamal.feature.anime.api.model.AlternativeTitlesYamal) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Alternative Titles",
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )

            alternativeTitles.en?.let { englishTitle ->
                InfoItem(label = "English", value = englishTitle)
            }

            alternativeTitles.ja?.let { japaneseTitle ->
                InfoItem(label = "Japanese", value = japaneseTitle)
            }

            if (alternativeTitles.synonyms.isNotEmpty()) {
                InfoItem(
                    label = "Synonyms",
                    value = alternativeTitles.synonyms.joinToString(", "),
                )
            }
        }
    }
}

@Composable
private fun StatisticsSection(statistics: com.yamal.feature.anime.api.model.AnimeStatisticsYamal) {
    val total =
        statistics.watching + statistics.completed + statistics.onHold +
            statistics.dropped + statistics.planToWatch

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "User Statistics",
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )

            // Visual progress bars for each status
            StatisticsBar(
                label = "Watching",
                count = statistics.watching,
                total = total,
                color = YamalTheme.colors.primary,
            )
            StatisticsBar(
                label = "Completed",
                count = statistics.completed,
                total = total,
                color = YamalTheme.colors.success,
            )
            StatisticsBar(
                label = "On Hold",
                count = statistics.onHold,
                total = total,
                color = YamalTheme.colors.warning,
            )
            StatisticsBar(
                label = "Dropped",
                count = statistics.dropped,
                total = total,
                color = YamalTheme.colors.danger,
            )
            StatisticsBar(
                label = "Plan to Watch",
                count = statistics.planToWatch,
                total = total,
                color = YamalTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun StatisticsBar(
    label: String,
    count: Int,
    total: Int,
    color: Color,
) {
    val percentage = if (total > 0) count.toFloat() / total else 0f

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.text,
            )
            Text(
                text = formatMembers(count),
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.textSecondary,
            )
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(YamalTheme.colors.box),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(percentage)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color),
            )
        }
    }
}

@Composable
private fun RelatedAnimeSection(
    relatedAnime: List<RelatedItemYamal<AnimeForListYamal>>,
    onAnimeClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Related Anime",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            relatedAnime.forEach { related ->
                RelatedAnimeCard(
                    anime = related.node,
                    relationLabel = related.relation.formatted,
                    onClick = { onAnimeClick(related.node.id) },
                )
            }
        }
    }
}

@Composable
private fun RelatedAnimeCard(
    anime: AnimeForListYamal,
    relationLabel: String,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .width(128.dp)
                .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
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

            // Relation label at bottom
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = relationLabel,
                    style = YamalTheme.typography.tiny,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }

        Text(
            text = anime.title,
            style = YamalTheme.typography.caption,
            color = YamalTheme.colors.text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RecommendationsSection(
    recommendations: List<AnimeRecommendationYamal>,
    onAnimeClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Recommendations",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            recommendations.take(10).forEach { recommendation ->
                AnimeCard(
                    imageUrl = recommendation.node.mainPicture?.medium ?: recommendation.node.mainPicture?.large,
                    title = recommendation.node.title,
                    metadata = buildRecommendationMetadata(recommendation.node),
                    rating = recommendation.node.mean,
                    width = 128.dp,
                    onClick = { onAnimeClick(recommendation.node.id) },
                )
            }
        }
    }
}

private fun buildRecommendationMetadata(anime: AnimeForListYamal): String {
    val parts = mutableListOf<String>()

    val typeString =
        when (anime.mediaType) {
            MediaTypeYamal.Tv -> "TV"
            MediaTypeYamal.Movie -> "Movie"
            MediaTypeYamal.Ova -> "OVA"
            MediaTypeYamal.Ona -> "ONA"
            MediaTypeYamal.Special -> "Special"
            MediaTypeYamal.Music -> "Music"
            MediaTypeYamal.Unknown -> "Unknown"
        }
    parts.add(typeString)

    anime.numberOfEpisodes?.let { eps ->
        parts.add("$eps eps")
    }

    return parts.joinToString(" • ")
}

@Composable
private fun BackgroundSection(background: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Background",
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )

            Text(
                text = background,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun ImageGallerySection(pictures: List<PictureYamal>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeader(
            title = "Gallery",
            actionText = "${pictures.size} images",
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            pictures.take(10).forEach { picture ->
                Surface(
                    modifier =
                        Modifier
                            .width(160.dp)
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    color = YamalTheme.colors.box,
                ) {
                    AsyncImage(
                        model = picture.large ?: picture.medium,
                        contentDescription = "Gallery image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

@Composable
private fun RelatedMangaSection(
    relatedManga: List<RelatedItemYamal<MangaForListYamal>>,
    onMangaClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Related Manga",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            relatedManga.forEach { related ->
                RelatedMangaCard(
                    manga = related.node,
                    relationLabel = related.relation.formatted,
                    onClick = { onMangaClick(related.node.id) },
                )
            }
        }
    }
}

@Composable
private fun RelatedMangaCard(
    manga: MangaForListYamal,
    relationLabel: String,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .width(128.dp)
                .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
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

            // Relation label at bottom
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = relationLabel,
                    style = YamalTheme.typography.tiny,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }

            // Manga badge at top
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .background(
                            color = YamalTheme.colors.primary,
                            shape = RoundedCornerShape(4.dp),
                        ).padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = "MANGA",
                    style = YamalTheme.typography.tiny,
                    color = YamalTheme.colors.textLightSolid,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Text(
            text = manga.title,
            style = YamalTheme.typography.caption,
            color = YamalTheme.colors.text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        // Chapters/Volumes info
        val metaInfo =
            buildList {
                manga.numberOfVolumes?.let { add("$it vols") }
                manga.numberOfChapters?.let { add("$it ch") }
            }.joinToString(" • ")

        if (metaInfo.isNotBlank()) {
            Text(
                text = metaInfo,
                style = YamalTheme.typography.tiny,
                color = YamalTheme.colors.textSecondary,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UserPersonalInfoSection(listStatus: AnimeListStatusYamal) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Your Notes",
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )

                Icon(
                    icon = Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = YamalTheme.colors.primary,
                    modifier = Modifier.size(20.dp),
                )
            }

            // Rewatching status
            if (listStatus.isRewatching == true || (listStatus.numTimesRewatched ?: 0) > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (listStatus.isRewatching == true) {
                        Tag(
                            text = "Rewatching",
                            color = TagColor.Primary,
                            fill = TagFill.Solid,
                        )
                    }
                    listStatus.numTimesRewatched?.let { times ->
                        if (times > 0) {
                            Tag(
                                text = "Rewatched $times ${if (times == 1) "time" else "times"}",
                                color = TagColor.Default,
                                fill = TagFill.Outline,
                            )
                        }
                    }
                }
            }

            // Dates
            if (listStatus.startDate != null || listStatus.finishDate != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    listStatus.startDate?.let { date ->
                        InfoItem(
                            label = "Started",
                            value = date,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    listStatus.finishDate?.let { date ->
                        InfoItem(
                            label = "Finished",
                            value = date,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // Priority and Rewatch Value
            if (listStatus.priority != null || listStatus.rewatchValue != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    listStatus.priority?.let { priority ->
                        InfoItem(
                            label = "Priority",
                            value = formatPriority(priority),
                            modifier = Modifier.weight(1f),
                        )
                    }
                    listStatus.rewatchValue?.let { value ->
                        InfoItem(
                            label = "Rewatch Value",
                            value = formatRewatchValue(value),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // Tags
            listStatus.tags?.let { tags ->
                if (tags.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Tags".uppercase(),
                            style = YamalTheme.typography.small,
                            color = YamalTheme.colors.textSecondary,
                            fontWeight = FontWeight.Bold,
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            tags.forEach { tag ->
                                Tag(
                                    text = tag,
                                    color = TagColor.Default,
                                    fill = TagFill.Outline,
                                )
                            }
                        }
                    }
                }
            }

            // Comments
            listStatus.comments?.let { comments ->
                if (comments.isNotBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Comments".uppercase(),
                            style = YamalTheme.typography.small,
                            color = YamalTheme.colors.textSecondary,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = comments,
                            style = YamalTheme.typography.body,
                            color = YamalTheme.colors.text,
                        )
                    }
                }
            }

            // Last updated
            listStatus.updatedAt?.let { updatedAt ->
                Text(
                    text = "Last updated: $updatedAt",
                    style = YamalTheme.typography.tiny,
                    color = YamalTheme.colors.weak,
                )
            }
        }
    }
}

private fun hasPersonalInfo(listStatus: AnimeListStatusYamal): Boolean =
    listStatus.isRewatching == true ||
        (listStatus.numTimesRewatched ?: 0) > 0 ||
        listStatus.startDate != null ||
        listStatus.finishDate != null ||
        listStatus.priority != null ||
        listStatus.rewatchValue != null ||
        !listStatus.tags.isNullOrEmpty() ||
        !listStatus.comments.isNullOrBlank()

private fun formatPriority(priority: Int): String =
    when (priority) {
        0 -> "Low"
        1 -> "Medium"
        2 -> "High"
        else -> "Unknown"
    }

private fun formatRewatchValue(value: Int): String =
    when (value) {
        1 -> "Very Low"
        2 -> "Low"
        3 -> "Medium"
        4 -> "High"
        5 -> "Very High"
        else -> "Unknown"
    }

// ===========================================
// Jikan API Sections
// ===========================================

@Composable
private fun TrailerSection(
    trailer: TrailerYamal,
    onOpenUrl: (String) -> Unit,
) {
    val trailerUrl = trailer.url ?: trailer.youtubeId?.let { "https://www.youtube.com/watch?v=$it" }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Trailer",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(enabled = trailerUrl != null) { trailerUrl?.let { onOpenUrl(it) } },
            shape = RoundedCornerShape(12.dp),
            color = YamalTheme.colors.box,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                // Thumbnail
                trailer.thumbnailUrl?.let { thumbnailUrl ->
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "Trailer thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }

                // Dark overlay
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                )

                // Play button
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(32.dp),
                    color = YamalTheme.colors.primary,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            icon = Icons.Outlined.Right,
                            contentDescription = "Play trailer",
                            tint = YamalTheme.colors.textLightSolid,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }

                // YouTube badge
                if (trailer.youtubeId != null) {
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .background(
                                    color = Color(0xFFFF0000),
                                    shape = RoundedCornerShape(4.dp),
                                ).padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = "YouTube",
                            style = YamalTheme.typography.tiny,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharactersSection(characters: List<AnimeCharacterYamal>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeader(
            title = "Characters",
            actionText = "${characters.size} characters",
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            characters.forEach { character ->
                CharacterCard(character = character)
            }
        }
    }
}

@Composable
private fun CharacterCard(character: AnimeCharacterYamal) {
    Column(
        modifier = Modifier.width(100.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Character image
        Surface(
            modifier =
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp)),
            shape = RoundedCornerShape(40.dp),
            color = YamalTheme.colors.box,
        ) {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        // Character name
        Text(
            text = character.name,
            style = YamalTheme.typography.caption,
            color = YamalTheme.colors.text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )

        // Role badge
        Tag(
            text = character.role,
            color = if (character.role == "Main") TagColor.Primary else TagColor.Default,
            fill = TagFill.Solid,
        )

        // Voice actor (first Japanese VA)
        character.voiceActors
            .firstOrNull { it.language == "Japanese" }
            ?.let { va ->
                Text(
                    text = "CV: ${va.name}",
                    style = YamalTheme.typography.tiny,
                    color = YamalTheme.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
    }
}

@Composable
private fun ThemeSongsSection(themeSongs: ThemeSongsYamal) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Theme Songs",
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )

            // Opening themes
            if (themeSongs.openings.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            icon = Icons.Outlined.Sound,
                            contentDescription = null,
                            tint = YamalTheme.colors.primary,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = "Opening${if (themeSongs.openings.size > 1) "s" else ""}",
                            style = YamalTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            color = YamalTheme.colors.text,
                        )
                    }

                    themeSongs.openings.forEachIndexed { index, opening ->
                        ThemeSongItem(
                            number = index + 1,
                            title = opening,
                        )
                    }
                }
            }

            // Ending themes
            if (themeSongs.endings.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            icon = Icons.Outlined.Sound,
                            contentDescription = null,
                            tint = YamalTheme.colors.textSecondary,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = "Ending${if (themeSongs.endings.size > 1) "s" else ""}",
                            style = YamalTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            color = YamalTheme.colors.text,
                        )
                    }

                    themeSongs.endings.forEachIndexed { index, ending ->
                        ThemeSongItem(
                            number = index + 1,
                            title = ending,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSongItem(
    number: Int,
    title: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Number badge
        Box(
            modifier =
                Modifier
                    .size(24.dp)
                    .background(
                        color = YamalTheme.colors.box,
                        shape = RoundedCornerShape(4.dp),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                style = YamalTheme.typography.tiny,
                color = YamalTheme.colors.textSecondary,
                fontWeight = FontWeight.Bold,
            )
        }

        // Song title (parse artist and title from format: "\"Title\" by Artist (eps X-Y)")
        Text(
            text = title,
            style = YamalTheme.typography.body,
            color = YamalTheme.colors.text,
            modifier = Modifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StreamingLinksSection(
    streamingLinks: List<ExternalLinkYamal>,
    onOpenUrl: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    icon = Icons.Outlined.VideoCamera,
                    contentDescription = null,
                    tint = YamalTheme.colors.primary,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = "Where to Watch",
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                streamingLinks.forEach { link ->
                    StreamingChip(link = link, onOpenUrl = onOpenUrl)
                }
            }
        }
    }
}

@Composable
private fun StreamingChip(
    link: ExternalLinkYamal,
    onOpenUrl: (String) -> Unit,
) {
    val chipColor = getStreamingServiceColor(link.name)

    Surface(
        modifier = Modifier.clickable { onOpenUrl(link.url) },
        shape = RoundedCornerShape(20.dp),
        color = chipColor.copy(alpha = 0.15f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon = Icons.Outlined.VideoCamera,
                contentDescription = null,
                tint = chipColor,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = link.name,
                style = YamalTheme.typography.body,
                color = chipColor,
                fontWeight = FontWeight.Medium,
            )
            Icon(
                icon = Icons.Outlined.Right,
                contentDescription = null,
                tint = chipColor,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

private fun getStreamingServiceColor(serviceName: String): Color =
    when (serviceName.lowercase()) {
        "crunchyroll" -> Color(0xFFF47521)
        "netflix" -> Color(0xFFE50914)
        "funimation" -> Color(0xFF5B0BB5)
        "amazon prime video" -> Color(0xFF00A8E1)
        "hulu" -> Color(0xFF1CE783)
        "disney+" -> Color(0xFF113CCF)
        "hidive" -> Color(0xFF00BAFF)
        else -> Color(0xFF6366F1) // Default purple
    }

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExternalLinksSection(
    externalLinks: List<ExternalLinkYamal>,
    onOpenUrl: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    icon = Icons.Outlined.Link,
                    contentDescription = null,
                    tint = YamalTheme.colors.text,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = "External Links",
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                externalLinks.forEach { link ->
                    ExternalLinkChip(link = link, onOpenUrl = onOpenUrl)
                }
            }
        }
    }
}

@Composable
private fun ExternalLinkChip(
    link: ExternalLinkYamal,
    onOpenUrl: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.clickable { onOpenUrl(link.url) },
        shape = RoundedCornerShape(8.dp),
        color = YamalTheme.colors.box,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = link.name,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.text,
            )
            Icon(
                icon = Icons.Outlined.Right,
                contentDescription = null,
                tint = YamalTheme.colors.textSecondary,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun ReviewsSection(reviews: List<AnimeReviewYamal>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeader(
            title = "Reviews",
            actionText = "${reviews.size} reviews",
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            reviews.forEach { review ->
                ReviewCard(review = review)
            }
        }
    }
}

@Composable
private fun ReviewCard(review: AnimeReviewYamal) {
    var isExpanded by remember { mutableStateOf(false) }
    val maxLines = if (isExpanded) Int.MAX_VALUE else 4

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // User info and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // User avatar
                    Surface(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        color = YamalTheme.colors.box,
                    ) {
                        review.user.imageUrl?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = review.user.username,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }

                    Column {
                        Text(
                            text = review.user.username,
                            style = YamalTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            color = YamalTheme.colors.text,
                        )
                        Text(
                            text = formatReviewDate(review.date),
                            style = YamalTheme.typography.tiny,
                            color = YamalTheme.colors.textSecondary,
                        )
                    }
                }

                // Score badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getScoreColor(review.score).copy(alpha = 0.15f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            icon = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = getScoreColor(review.score),
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            text = "${review.score}/10",
                            style = YamalTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            color = getScoreColor(review.score),
                        )
                    }
                }
            }

            // Tags (Spoiler, Preliminary, etc.)
            if (review.isSpoiler || review.isPreliminary || review.tags.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (review.isSpoiler) {
                        Tag(
                            text = "Spoiler",
                            color = TagColor.Danger,
                            fill = TagFill.Solid,
                        )
                    }
                    if (review.isPreliminary) {
                        Tag(
                            text = "Preliminary",
                            color = TagColor.Warning,
                            fill = TagFill.Solid,
                        )
                    }
                    review.tags.take(2).forEach { tag ->
                        Tag(
                            text = tag,
                            color = TagColor.Default,
                            fill = TagFill.Outline,
                        )
                    }
                }
            }

            // Review text
            Column {
                Text(
                    text = review.review,
                    style = YamalTheme.typography.body,
                    color = YamalTheme.colors.text,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                )

                if (review.review.length > 200) {
                    Text(
                        text = if (isExpanded) "Show less" else "Read more",
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.primary,
                        fontWeight = FontWeight.Medium,
                        modifier =
                            Modifier
                                .clickable { isExpanded = !isExpanded }
                                .padding(top = 4.dp),
                    )
                }
            }

            // Reactions
            if (review.reactions.overall > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (review.reactions.nice > 0) {
                        ReactionChip(label = "Nice", count = review.reactions.nice)
                    }
                    if (review.reactions.loveIt > 0) {
                        ReactionChip(label = "Love it", count = review.reactions.loveIt)
                    }
                    if (review.reactions.funny > 0) {
                        ReactionChip(label = "Funny", count = review.reactions.funny)
                    }
                    if (review.reactions.informative > 0) {
                        ReactionChip(label = "Helpful", count = review.reactions.informative)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReactionChip(
    label: String,
    count: Int,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = YamalTheme.colors.box,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = YamalTheme.typography.tiny,
                color = YamalTheme.colors.textSecondary,
            )
            Text(
                text = count.toString(),
                style = YamalTheme.typography.tiny,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )
        }
    }
}

private fun formatReviewDate(dateString: String): String {
    // Parse ISO date and format to readable string
    // Format: "2024-01-15T12:00:00+00:00" -> "Jan 15, 2024"
    return try {
        val datePart = dateString.substringBefore("T")
        val parts = datePart.split("-")
        if (parts.size == 3) {
            val year = parts[0]
            val month =
                when (parts[1]) {
                    "01" -> "Jan"
                    "02" -> "Feb"
                    "03" -> "Mar"
                    "04" -> "Apr"
                    "05" -> "May"
                    "06" -> "Jun"
                    "07" -> "Jul"
                    "08" -> "Aug"
                    "09" -> "Sep"
                    "10" -> "Oct"
                    "11" -> "Nov"
                    "12" -> "Dec"
                    else -> parts[1]
                }
            val day = parts[2].toIntOrNull() ?: parts[2]
            "$month $day, $year"
        } else {
            dateString
        }
    } catch (e: Exception) {
        dateString
    }
}

private fun getScoreColor(score: Int): Color =
    when {
        score >= 8 -> Color(0xFF22C55E)

        // Green
        score >= 6 -> Color(0xFFFBBF24)

        // Yellow
        score >= 4 -> Color(0xFFF97316)

        // Orange
        else -> Color(0xFFEF4444) // Red
    }
