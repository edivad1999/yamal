package com.yamal.designSystem.components.upcomingAnimeItem

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
import com.yamal.designSystem.components.button.IconButton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Horizontal list item for upcoming anime content.
 *
 * Displays thumbnail, title, metadata, and optional notification button.
 *
 * Example usage:
 * ```
 * UpcomingAnimeItem(
 *     imageUrl = "https://...",
 *     title = "Solo Leveling",
 *     metadata = "Action, Fantasy • Jan 2024",
 *     onNotificationClick = { /* toggle notification */ },
 *     onClick = { /* navigate */ }
 * )
 * ```
 *
 * @param imageUrl URL of the thumbnail image
 * @param title Anime title
 * @param metadata Subtitle text (e.g., "Action, Fantasy • Jan 2024")
 * @param modifier Modifier for the item
 * @param onNotificationClick Optional callback for notification button
 * @param onClick Optional click callback for the entire item
 */
@Composable
fun UpcomingAnimeItem(
    imageUrl: String?,
    title: String,
    metadata: String,
    modifier: Modifier = Modifier,
    onNotificationClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(8.dp),
        color = YamalTheme.colors.box,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Thumbnail
            Box(
                modifier =
                    Modifier
                        .width(64.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(6.dp))
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

            // Title and metadata
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = title,
                    style = YamalTheme.typography.titleSmall,
                    color = YamalTheme.colors.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = metadata,
                    style = YamalTheme.typography.caption,
                    color = YamalTheme.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Notification button
            if (onNotificationClick != null) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        icon = Icons.Outlined.Notification,
                        contentDescription = "Set notification",
                        tint = YamalTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun UpcomingAnimeItemPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                UpcomingAnimeItem(
                    imageUrl = null,
                    title = "Solo Leveling",
                    metadata = "Action, Fantasy • Jan 2024",
                    onNotificationClick = {},
                    onClick = {},
                )

                UpcomingAnimeItem(
                    imageUrl = null,
                    title = "Blue Exorcist: Shimane Illuminati Saga",
                    metadata = "Supernatural • Jan 2024",
                    onNotificationClick = {},
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun UpcomingAnimeItemNoNotificationPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            UpcomingAnimeItem(
                imageUrl = null,
                title = "Demon Slayer: Hashira Training Arc",
                metadata = "Action, Adventure • Spring 2024",
                onClick = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
