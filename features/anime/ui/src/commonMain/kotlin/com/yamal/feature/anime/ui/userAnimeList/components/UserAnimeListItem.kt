package com.yamal.feature.anime.ui.userAnimeList.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.badge.Badge
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.tag.Tag
import com.yamal.designSystem.components.tag.TagColor
import com.yamal.designSystem.components.tag.TagFill
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.MediaTypeYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.platform.utils.formatOneDecimal

/**
 * List item for displaying anime in user's list.
 *
 * This component displays anime information including:
 * - Poster thumbnail
 * - Title and metadata (type, episodes, date)
 * - Rating badge
 * - Status indicator (colored left border)
 * - Status tag
 *
 * Note: Episode progress stepper (+/- buttons) requires additional API data
 * not currently available in GenericAnime model.
 *
 * @param anime The anime to display
 * @param status Current user list status for this anime
 * @param modifier Modifier for the component
 */
@Composable
fun UserAnimeListItem(
    anime: AnimeForListYamal,
    status: UserListStatusYamal,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.background,
        border = BorderStroke(1.dp, YamalTheme.colors.border),
    ) {
        Row(
            modifier =
                Modifier
                    .height(IntrinsicSize.Min)
                    .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Left status indicator
            Spacer(
                modifier =
                    Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(getStatusColor(status)),
            )

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

                Spacer(Modifier.weight(1f))

                // Status tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Tag(
                        text = formatStatusName(status),
                        color = getStatusTagColor(status),
                        fill = TagFill.Solid,
                        round = true,
                    )

                    // TODO: Episode stepper would go here when API provides list_status data
                    // Requires: num_episodes_watched, total episodes
                    // Example: EpisodeStepper(current = 8, total = 12, onIncrement = {}, onDecrement = {})
                }
            }
        }
    }
}

@Composable
private fun getStatusColor(status: UserListStatusYamal): Color =
    when (status) {
        UserListStatusYamal.Watching -> YamalTheme.colors.primary
        UserListStatusYamal.Completed -> YamalTheme.colors.success
        UserListStatusYamal.OnHold -> YamalTheme.colors.warning
        UserListStatusYamal.Dropped -> YamalTheme.colors.danger
        UserListStatusYamal.PlanToWatch -> YamalTheme.colors.textSecondary
    }

@Composable
private fun getStatusTagColor(status: UserListStatusYamal): TagColor =
    when (status) {
        UserListStatusYamal.Watching -> TagColor.Primary
        UserListStatusYamal.Completed -> TagColor.Success
        UserListStatusYamal.OnHold -> TagColor.Warning
        UserListStatusYamal.Dropped -> TagColor.Danger
        UserListStatusYamal.PlanToWatch -> TagColor.Default
    }

private fun formatStatusName(status: UserListStatusYamal): String =
    when (status) {
        UserListStatusYamal.Watching -> "Watching"
        UserListStatusYamal.Completed -> "Completed"
        UserListStatusYamal.OnHold -> "On Hold"
        UserListStatusYamal.Dropped -> "Dropped"
        UserListStatusYamal.PlanToWatch -> "Plan to Watch"
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

    // Start date (extract year/season)
    anime.startDate?.let { date ->
        // Format: "YYYY-MM-DD" -> extract year and approximate season
        val year = date.take(4)
        parts.add(year)
    }

    return parts.joinToString(" • ")
}
