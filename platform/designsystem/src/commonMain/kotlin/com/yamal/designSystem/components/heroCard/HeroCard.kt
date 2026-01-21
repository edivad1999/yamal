package com.yamal.designSystem.components.heroCard

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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.tag.Tag
import com.yamal.designSystem.components.tag.TagColor
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Hero card component for featured content with large image and gradient overlay.
 *
 * Typically used in carousels for highlighting trending or featured anime.
 *
 * Example usage:
 * ```
 * HeroCard(
 *     imageUrl = "https://...",
 *     title = "Cyberpunk: Edgerunners",
 *     description = "In a dystopia riddled with corruption...",
 *     badge = "TRENDING",
 *     onClick = { /* navigate */ }
 * )
 * ```
 *
 * @param imageUrl URL of the hero background image
 * @param title Main title text
 * @param description Optional description text
 * @param modifier Modifier for the card
 * @param badge Optional badge text (e.g., "TRENDING", "NEW SEASON")
 * @param onClick Optional click callback
 */
@Composable
fun HeroCard(
    imageUrl: String?,
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    badge: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .width(320.dp) // ~85vw on typical mobile
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(YamalTheme.colors.box)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        // Background image
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )
        }

        // Gradient overlay (from transparent to dark at bottom)
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Black.copy(alpha = 0.9f),
                                ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY,
                        ),
                    ),
        )

        // Content at bottom
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .fillMaxWidth(),
        ) {
            // Badge
            if (badge != null) {
                Tag(
                    text = badge,
                    color = TagColor.Primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Title
            Text(
                text = title,
                style = YamalTheme.typography.displayMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            // Description
            if (description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = YamalTheme.typography.body,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun HeroCardPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            HeroCard(
                imageUrl = null,
                title = "Cyberpunk: Edgerunners",
                description = "In a dystopia riddled with corruption and cybernetic implants...",
                badge = "TRENDING",
                onClick = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun HeroCardNoBadgePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            HeroCard(
                imageUrl = null,
                title = "Frieren: Beyond Journey's End",
                description = "The adventure is over but the life continues...",
                onClick = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun HeroCardMinimalPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            HeroCard(
                imageUrl = null,
                title = "Solo Leveling",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

/**
 * Skeleton loading variant of [HeroCard].
 *
 * Shows a shimmer placeholder while data is loading.
 *
 * @param modifier Modifier for the skeleton card
 */
@Composable
fun HeroCardSkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .width(320.dp)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(YamalTheme.colors.box),
    ) {
        // Skeleton background
        Skeleton(
            modifier = Modifier.matchParentSize(),
            borderRadius = 0.dp,
        )

        // Content at bottom (skeleton text)
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Badge skeleton
            Skeleton(
                width = 80.dp,
                height = 24.dp,
                borderRadius = 4.dp,
            )

            // Title skeleton
            Skeleton(
                modifier = Modifier.fillMaxWidth(0.85f),
                height = 24.dp,
                borderRadius = 4.dp,
            )

            // Description skeleton
            Skeleton(
                modifier = Modifier.fillMaxWidth(0.6f),
                height = 16.dp,
                borderRadius = 4.dp,
            )
        }
    }
}

@Preview
@Composable
private fun HeroCardSkeletonPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                HeroCardSkeleton()
            }
        }
    }
}
