package com.yamal.feature.manga.ui.mangaDetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.yamal.designSystem.components.addToList.MangaAddToListBottomSheet
import com.yamal.designSystem.components.addToList.MangaAddToListState
import com.yamal.designSystem.components.addToList.MangaListStatus
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.tag.Tag
import com.yamal.designSystem.components.tag.TagColor
import com.yamal.designSystem.components.tag.TagFill
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.login.api.LoginRepository
import com.yamal.feature.manga.api.model.MangaForDetailsYamal
import com.yamal.feature.manga.api.model.UserMangaListStatusYamal
import com.yamal.feature.manga.ui.mangaDetails.presenter.MangaDetailsIntent
import com.yamal.feature.manga.ui.mangaDetails.presenter.MangaDetailsPresenter
import com.yamal.platform.utils.formatOneDecimal
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun MangaDetailsScreen(
    mangaId: Int,
    onNavigateBack: () -> Unit,
    presenter: MangaDetailsPresenter = koinInject { parametersOf(mangaId) },
    loginRepository: LoginRepository = koinInject(),
) {
    val state by presenter.state.collectAsState()
    val isLoggedIn by loginRepository.isUserAuthenticated().collectAsState(initial = false)
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(mangaId) {
        presenter.processIntent(MangaDetailsIntent.Refresh)
    }

    // Close bottom sheet on successful update
    LaunchedEffect(state.listUpdateSuccess) {
        if (state.listUpdateSuccess) {
            showBottomSheet = false
            presenter.processIntent(MangaDetailsIntent.ClearListUpdateState)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.loading -> {
                SpinLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            state.error != null -> {
                Text(
                    text = "Error: ${state.error}",
                    color = YamalTheme.colors.danger,
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                )
            }

            state.manga != null -> {
                MangaDetailsContent(
                    manga = state.manga!!,
                    onNavigateBack = onNavigateBack,
                    onAddToListClick = { showBottomSheet = true },
                    isLoggedIn = isLoggedIn,
                )
            }
        }

        // Bottom sheet for adding to list
        MangaAddToListBottomSheet(
            visible = showBottomSheet,
            onDismiss = { showBottomSheet = false },
            initialState =
                state.manga?.let { manga ->
                    MangaAddToListState(
                        status =
                            manga.myListStatus?.status?.let { statusString ->
                                MangaListStatus.entries.find { it.serialName == statusString }
                            },
                        score = manga.myListStatus?.score ?: 0,
                        chaptersRead = manga.myListStatus?.numChaptersRead ?: 0,
                        totalChapters = manga.numChapters,
                        volumesRead = manga.myListStatus?.numVolumesRead ?: 0,
                        totalVolumes = manga.numVolumes,
                        isInList = manga.myListStatus != null,
                    )
                } ?: MangaAddToListState(),
            isLoggedIn = isLoggedIn,
            onSave = { status, score, chaptersRead, volumesRead ->
                presenter.processIntent(
                    MangaDetailsIntent.UpdateListStatus(
                        status = status?.let { UserMangaListStatusYamal.fromSerializedValue(it.serialName) },
                        score = score,
                        chaptersRead = chaptersRead,
                        volumesRead = volumesRead,
                    ),
                )
            },
            onDelete = {
                presenter.processIntent(MangaDetailsIntent.DeleteFromList)
            },
            onLoginClick = {
                onNavigateBack()
            },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MangaDetailsContent(
    manga: MangaForDetailsYamal,
    onNavigateBack: () -> Unit,
    onAddToListClick: () -> Unit,
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
                    model = manga.mainPicture?.large ?: manga.mainPicture?.medium,
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
                                            Color.Black.copy(alpha = 0.6f),
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
                            model = manga.mainPicture?.large ?: manga.mainPicture?.medium,
                            contentDescription = manga.title,
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
                            text = manga.title,
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
                                text = manga.mediaType.displayName,
                                color = TagColor.Default,
                                fill = TagFill.Solid,
                                modifier = Modifier,
                            )

                            // Year
                            manga.startDate?.take(4)?.let { year ->
                                Text(
                                    text = year,
                                    style = YamalTheme.typography.body,
                                    color = YamalTheme.colors.text,
                                )
                            }

                            // Rating
                            manga.mean?.let { score ->
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
                manga.synopsis?.let { synopsis ->
                    ExpandableSynopsis(synopsis = synopsis)
                }

                // Add to List and Progress section
                AddToListSection(
                    chaptersRead = manga.myListStatus?.numChaptersRead ?: 0,
                    totalChapters = manga.numChapters,
                    volumesRead = manga.myListStatus?.numVolumesRead ?: 0,
                    totalVolumes = manga.numVolumes,
                    isLoggedIn = isLoggedIn,
                    isInList = manga.myListStatus != null,
                    currentStatus = manga.myListStatus?.status,
                    onAddToListClick = onAddToListClick,
                )

                // Stats row (Rank, Popularity, Members)
                StatsRow(
                    rank = manga.rank,
                    popularity = manga.popularity,
                    members = manga.numListUsers,
                )

                // Info section
                InfoSection(manga = manga)

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Floating back button
        Box(
            modifier =
                Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(16.dp)
                    .align(Alignment.TopStart),
        ) {
            Surface(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clickable(onClick = onNavigateBack),
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.2f),
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
        )
    }
}

@Composable
private fun AddToListSection(
    chaptersRead: Int,
    totalChapters: Int,
    volumesRead: Int,
    totalVolumes: Int,
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

            // Progress display - show chapters
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
                        text = "Chapters",
                        style = YamalTheme.typography.small,
                        color = YamalTheme.colors.textSecondary,
                    )
                    Text(
                        text = if (isInList) "$chaptersRead / $totalChapters" else "-- / $totalChapters",
                        style = YamalTheme.typography.body,
                        fontWeight = FontWeight.Bold,
                        color = YamalTheme.colors.text,
                    )
                }
            }
        }

        // Volumes row if available
        if (totalVolumes > 0) {
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(
                            width = 2.dp,
                            color = YamalTheme.colors.border,
                            shape = RoundedCornerShape(12.dp),
                        ),
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Volumes",
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.textSecondary,
                    )
                    Text(
                        text = if (isInList) "$volumesRead / $totalVolumes" else "-- / $totalVolumes",
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
private fun InfoSection(manga: MangaForDetailsYamal) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Type and Chapters
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                InfoItem(
                    label = "Type",
                    value = manga.mediaType.displayName,
                    modifier = Modifier.weight(1f),
                )
                InfoItem(
                    label = "Chapters",
                    value = if (manga.numChapters > 0) manga.numChapters.toString() else "?",
                    modifier = Modifier.weight(1f),
                )
            }

            // Status and Volumes
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                InfoItem(
                    label = "Status",
                    value = manga.status,
                    modifier = Modifier.weight(1f),
                )
                InfoItem(
                    label = "Volumes",
                    value = if (manga.numVolumes > 0) manga.numVolumes.toString() else "?",
                    modifier = Modifier.weight(1f),
                )
            }

            // Authors
            if (manga.authors.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Authors".uppercase(),
                        style = YamalTheme.typography.small,
                        color = YamalTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                    manga.authors.forEach { author ->
                        Text(
                            text = "${author.firstName} ${author.lastName} (${author.role})",
                            style = YamalTheme.typography.body,
                            color = YamalTheme.colors.primary,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            // Serialization
            if (manga.serialization.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Serialization".uppercase(),
                        style = YamalTheme.typography.small,
                        color = YamalTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = manga.serialization.joinToString(", ") { it.name },
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.primary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            // Genres
            if (manga.genres.isNotEmpty()) {
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
                        manga.genres.forEach { genre ->
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
