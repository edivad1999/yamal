package com.yamal.feature.anime.ui.animeRanking.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.badge.Badge
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.MediaTypeYamal
import com.yamal.platform.utils.formatOneDecimal

/**
 * List item for displaying anime in ranking list.
 *
 * This component displays anime ranking information including:
 * - Rank number (prominently displayed on the left)
 * - Poster thumbnail
 * - Title and metadata (type, episodes, date)
 * - Rating badge
 *
 * No user-specific elements (status indicators, progress) are shown.
 *
 * @param anime The anime to display
 * @param modifier Modifier for the component
 */
@Composable
fun AnimeRankingListItem(
    anime: AnimeForListYamal,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
        border = BorderStroke(1.dp, YamalTheme.colors.border),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Rank number
            anime.rank?.let { rank ->
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = getRankBackgroundColor(rank),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = rank.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = getRankTextColor(rank),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            // Poster thumbnail
            AsyncImage(
                model = anime.mainPicture?.medium ?: anime.mainPicture?.large,
                contentDescription = "${anime.title} poster",
                modifier =
                    Modifier
                        .width(70.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Title and rating row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = anime.title,
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = YamalTheme.colors.text,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    // Rating badge
                    anime.mean?.let { rating ->
                        Badge(
                            containerColor = YamalTheme.colors.warning,
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    icon = Icons.Filled.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                )
                                Text(
                                    text = rating.formatOneDecimal(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }

                // Metadata
                Text(
                    text = buildMetadataString(anime),
                    fontSize = 12.sp,
                    color = YamalTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun getRankBackgroundColor(rank: Int): Color =
    when {
        rank == 1 -> YamalTheme.colors.warning.copy(alpha = 0.15f)

        // Gold
        rank == 2 -> YamalTheme.colors.textSecondary.copy(alpha = 0.15f)

        // Silver
        rank == 3 -> Color(0xFFCD7F32).copy(alpha = 0.15f)

        // Bronze
        else -> YamalTheme.colors.box
    }

@Composable
private fun getRankTextColor(rank: Int): Color =
    when {
        rank == 1 -> YamalTheme.colors.warning

        // Gold
        rank == 2 -> YamalTheme.colors.textSecondary

        // Silver
        rank == 3 -> Color(0xFFCD7F32)

        // Bronze
        else -> YamalTheme.colors.text
    }

private fun buildMetadataString(anime: AnimeForListYamal): String {
    val parts = mutableListOf<String>()

    // Media type
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

    // Episodes
    anime.numberOfEpisodes?.let { eps ->
        parts.add("$eps eps")
    } ?: parts.add("? eps")

    // Start date (extract year)
    anime.startDate?.let { date ->
        // Format: "YYYY-MM-DD" -> extract year
        val year = date.take(4)
        parts.add(year)
    }

    return parts.joinToString(" • ")
}
