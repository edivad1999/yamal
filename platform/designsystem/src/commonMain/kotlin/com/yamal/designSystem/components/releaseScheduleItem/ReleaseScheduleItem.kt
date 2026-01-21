package com.yamal.designSystem.components.releaseScheduleItem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Release schedule item component for displaying upcoming episode releases.
 *
 * Shows anime thumbnail, title, episode info, air time, and rating.
 * Typically used in release schedule lists or calendars.
 *
 * Example usage:
 * ```
 * ReleaseScheduleItem(
 *     imageUrl = "https://...",
 *     title = "Chainsaw Man",
 *     episodeInfo = "Ep 8 • Action, Horror",
 *     airTime = "18:30",
 *     rating = 8.9f,
 *     onClick = { /* navigate */ }
 * )
 * ```
 *
 * @param imageUrl URL of the thumbnail image
 * @param title Anime title
 * @param episodeInfo Episode number and genres
 * @param airTime Time the episode airs (e.g., "18:30")
 * @param rating Optional rating score
 * @param modifier Modifier for the item
 * @param onClick Optional click callback
 */
@Composable
fun ReleaseScheduleItem(
    imageUrl: String?,
    title: String,
    episodeInfo: String,
    airTime: String,
    modifier: Modifier = Modifier,
    rating: Float? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Thumbnail
        Box(
            modifier =
                Modifier
                    .width(64.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(YamalTheme.colors.border),
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        // Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Title and air time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = title,
                    style = YamalTheme.typography.titleSmall,
                    color = YamalTheme.colors.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                )

                Text(
                    text = airTime,
                    style = YamalTheme.typography.titleSmall,
                    color = YamalTheme.colors.primary,
                )
            }

            // Episode info
            Text(
                text = episodeInfo,
                style = YamalTheme.typography.caption,
                color = YamalTheme.colors.textSecondary,
            )

            // Rating
            if (rating != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        icon = Icons.Filled.Star,
                        contentDescription = null,
                        tint = YamalTheme.colors.warning,
                        modifier = Modifier.size(14.dp),
                    )

                    Text(
                        text = rating.toString(),
                        style = YamalTheme.typography.caption,
                        color = YamalTheme.colors.text,
                    )
                }
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun ReleaseScheduleItemPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ReleaseScheduleItem(
                    imageUrl = null,
                    title = "Chainsaw Man",
                    episodeInfo = "Ep 8 • Action, Horror",
                    airTime = "18:30",
                    rating = 8.9f,
                    onClick = {},
                )

                ReleaseScheduleItem(
                    imageUrl = null,
                    title = "Solo Leveling",
                    episodeInfo = "Ep 5 • Fantasy, Action",
                    airTime = "20:00",
                    rating = 8.5f,
                    onClick = {},
                )

                ReleaseScheduleItem(
                    imageUrl = null,
                    title = "Frieren: Beyond Journey's End",
                    episodeInfo = "Ep 12 • Adventure, Drama",
                    airTime = "22:00",
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun ReleaseScheduleItemNoRatingPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            ReleaseScheduleItem(
                imageUrl = null,
                title = "New Anime Series",
                episodeInfo = "Ep 1 • Comedy",
                airTime = "19:00",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

/**
 * Skeleton loading variant of [ReleaseScheduleItem].
 *
 * Shows a shimmer placeholder while data is loading.
 *
 * @param modifier Modifier for the skeleton item
 */
@Composable
fun ReleaseScheduleItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Thumbnail skeleton
        Skeleton(
            modifier =
                Modifier
                    .width(64.dp)
                    .height(80.dp),
            borderRadius = 8.dp,
        )

        // Content skeleton
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Title and time row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Skeleton(
                    modifier = Modifier.weight(1f).padding(end = 16.dp),
                    height = 16.dp,
                    borderRadius = 4.dp,
                )
                Skeleton(
                    width = 48.dp,
                    height = 16.dp,
                    borderRadius = 4.dp,
                )
            }

            // Episode info skeleton
            Skeleton(
                modifier = Modifier.fillMaxWidth(0.6f),
                height = 12.dp,
                borderRadius = 4.dp,
            )

            // Rating skeleton
            Skeleton(
                width = 60.dp,
                height = 14.dp,
                borderRadius = 4.dp,
            )
        }
    }
}

@Preview
@Composable
private fun ReleaseScheduleItemSkeletonPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ReleaseScheduleItemSkeleton()
                ReleaseScheduleItemSkeleton()
                ReleaseScheduleItemSkeleton()
            }
        }
    }
}
