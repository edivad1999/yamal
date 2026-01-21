package com.yamal.designSystem.components.animeCard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.platform.utils.formatOneDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Vertical anime card component with poster image, rating, and metadata.
 *
 * Follows mobile app card patterns for media content.
 *
 * Example usage:
 * ```
 * AnimeCard(
 *     imageUrl = "https://...",
 *     title = "One Piece",
 *     metadata = "TV • 1000+ eps",
 *     rating = 9.1f,
 *     onClick = { /* navigate */ }
 * )
 * ```
 *
 * @param imageUrl URL of the anime poster image
 * @param title Anime title
 * @param metadata Subtitle text (e.g., "TV • 24 eps")
 * @param modifier Modifier for the card
 * @param rating Optional rating to display (0-10 scale)
 * @param width Width of the card
 * @param onClick Optional click callback
 */
@Composable
fun AnimeCard(
    imageUrl: String?,
    title: String,
    metadata: String,
    modifier: Modifier = Modifier,
    rating: Float? = null,
    width: Dp = 144.dp,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier =
            modifier
                .width(width)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Poster image
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(YamalTheme.colors.box),
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            // Rating badge in top-right corner
            if (rating != null) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            icon = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFFFC107), // Yellow star
                        )
                        Text(
                            text = rating.formatOneDecimal(),
                            style = YamalTheme.typography.tiny,
                            color = Color.White,
                        )
                    }
                }
            }
        }

        // Title and metadata
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                style = YamalTheme.typography.bodyMedium,
                color = YamalTheme.colors.text,
                maxLines = 1,
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
    }
}

// Previews

@Preview
@Composable
private fun AnimeCardPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AnimeCard(
                    imageUrl = null,
                    title = "One Piece",
                    metadata = "TV • 1000+ eps",
                    rating = 9.1f,
                    onClick = {},
                )

                AnimeCard(
                    imageUrl = null,
                    title = "Jujutsu Kaisen",
                    metadata = "TV • 24 eps",
                    rating = 8.9f,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnimeCardNoRatingPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            AnimeCard(
                imageUrl = null,
                title = "Demon Slayer: Kimetsu no Yaiba",
                metadata = "TV • 26 eps",
                modifier = Modifier.padding(16.dp),
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun AnimeCardCompactPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            AnimeCard(
                imageUrl = null,
                title = "Berserk",
                metadata = "Manga • Vol 41",
                width = 128.dp,
                modifier = Modifier.padding(16.dp),
                onClick = {},
            )
        }
    }
}

/**
 * Skeleton loading variant of [AnimeCard].
 *
 * Shows a shimmer placeholder while data is loading.
 *
 * @param modifier Modifier for the skeleton card
 * @param width Width of the card (should match AnimeCard width)
 */
@Composable
fun AnimeCardSkeleton(
    modifier: Modifier = Modifier,
    width: Dp = 144.dp,
) {
    Column(
        modifier = modifier.width(width),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Poster image skeleton
        Skeleton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
            borderRadius = 8.dp,
        )

        // Title skeleton
        Skeleton(
            modifier = Modifier.fillMaxWidth(0.85f),
            height = 16.dp,
            borderRadius = 4.dp,
        )

        // Metadata skeleton
        Skeleton(
            modifier = Modifier.fillMaxWidth(0.6f),
            height = 12.dp,
            borderRadius = 4.dp,
        )
    }
}

@Preview
@Composable
private fun AnimeCardSkeletonPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AnimeCardSkeleton()
                AnimeCardSkeleton()
            }
        }
    }
}
