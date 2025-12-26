package com.yamal.feature.anime.ui.animeDetails.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.card.YamalCard
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.AnimeDetails
import com.yamal.feature.anime.ui.animeDetails.presenter.AnimeDetailsIntent
import com.yamal.feature.anime.ui.animeDetails.presenter.AnimeDetailsPresenter
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun AnimeDetailsScreen(
    animeId: Int,
    onNavigateBack: () -> Unit,
    presenter: AnimeDetailsPresenter = koinInject { parametersOf(animeId) },
) {
    val state by presenter.state.collectAsState()

    LaunchedEffect(animeId) {
        presenter.processIntent(AnimeDetailsIntent.Refresh)
    }

    Scaffold(
        topBar = {
            YamalNavBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = {
                    Text(
                        text = state.anime?.title ?: "Anime Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = YamalTheme.colors.neutralColors.primaryText,
                    )
                },
                left = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Outlined.Backward, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when {
                state.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = YamalTheme.colors.functionalColors.error,
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                    )
                }

                state.anime != null -> {
                    AnimeDetailsContent(anime = state.anime!!)
                }
            }
        }
    }
}

@Composable
private fun AnimeDetailsContent(anime: AnimeDetails) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            YamalCard(
                modifier = Modifier.width(150.dp),
            ) {
                AsyncImage(
                    model = anime.mainPicture?.large ?: anime.mainPicture?.medium,
                    contentDescription = anime.title,
                    modifier = Modifier.height(220.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = anime.title,
                    style = YamalTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.neutralColors.title,
                )

                Spacer(modifier = Modifier.height(8.dp))

                anime.mean?.let { score ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            icon = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = YamalTheme.colors.paletteColors.color6,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = ((score * 100).toInt() / 100.0).toString(),
                            style = YamalTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            color = YamalTheme.colors.neutralColors.primaryText,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                InfoRow("Type", anime.mediaType.name)
                InfoRow("Episodes", anime.numEpisodes.toString())
                InfoRow("Status", anime.status)
                anime.startSeason?.let {
                    InfoRow("Season", "${it.season.name} ${it.year}")
                }
                anime.rating?.let {
                    InfoRow("Rating", it)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (anime.genre.isNotEmpty()) {
            Text(
                text = "Genres",
                style = YamalTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.neutralColors.title,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = anime.genre.joinToString(", ") { it.name },
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.neutralColors.primaryText,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (anime.studios.isNotEmpty()) {
            Text(
                text = "Studios",
                style = YamalTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.neutralColors.title,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = anime.studios.joinToString(", ") { it.name },
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.neutralColors.primaryText,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        anime.synopsis?.let { synopsis ->
            Text(
                text = "Synopsis",
                style = YamalTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.neutralColors.title,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = synopsis,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.neutralColors.primaryText,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        anime.background?.let { background ->
            Text(
                text = "Background",
                style = YamalTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.neutralColors.title,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = background,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.neutralColors.primaryText,
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
    ) {
        Text(
            text = "$label: ",
            style = YamalTheme.typography.small,
            color = YamalTheme.colors.neutralColors.secondaryText,
        )
        Text(
            text = value,
            style = YamalTheme.typography.small,
            color = YamalTheme.colors.neutralColors.primaryText,
        )
    }
}
