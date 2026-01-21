package com.yamal.designSystem.components.listItem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.skeleton.Skeleton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Skeleton loading variant for anime list items (ranking/user list).
 *
 * Provides a shimmer placeholder while data is loading.
 *
 * @param modifier Modifier for the skeleton
 * @param showRankBadge Whether to show the rank badge skeleton (for ranking lists)
 * @param showStatusIndicator Whether to show the left status indicator (for user lists)
 */
@Composable
fun AnimeListItemSkeleton(
    modifier: Modifier = Modifier,
    showRankBadge: Boolean = false,
    showStatusIndicator: Boolean = false,
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
            // Left status indicator (for user list)
            if (showStatusIndicator) {
                Skeleton(
                    width = 4.dp,
                    height = 100.dp,
                    borderRadius = 2.dp,
                )
            }

            // Rank badge (for ranking list)
            if (showRankBadge) {
                Skeleton(
                    width = 48.dp,
                    height = 48.dp,
                    borderRadius = 8.dp,
                )
            }

            // Poster thumbnail
            Skeleton(
                modifier =
                    Modifier
                        .width(70.dp)
                        .aspectRatio(2f / 3f),
                borderRadius = 8.dp,
            )

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Title row with rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    // Title
                    Column(
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Skeleton(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            height = 16.dp,
                            borderRadius = 4.dp,
                        )
                        Skeleton(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            height = 16.dp,
                            borderRadius = 4.dp,
                        )
                    }

                    // Rating badge
                    Skeleton(
                        width = 48.dp,
                        height = 20.dp,
                        borderRadius = 10.dp,
                    )
                }

                // Metadata
                Skeleton(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    height = 12.dp,
                    borderRadius = 4.dp,
                )

                // Status tag (for user list)
                if (showStatusIndicator) {
                    Skeleton(
                        width = 80.dp,
                        height = 24.dp,
                        borderRadius = 12.dp,
                    )
                }
            }
        }
    }
}

/**
 * Convenience composable for ranking list skeleton.
 */
@Composable
fun AnimeRankingListItemSkeleton(modifier: Modifier = Modifier) {
    AnimeListItemSkeleton(
        modifier = modifier,
        showRankBadge = true,
        showStatusIndicator = false,
    )
}

/**
 * Convenience composable for user list item skeleton.
 */
@Composable
fun UserAnimeListItemSkeleton(modifier: Modifier = Modifier) {
    AnimeListItemSkeleton(
        modifier = modifier,
        showRankBadge = false,
        showStatusIndicator = true,
    )
}

// Previews

@Preview
@Composable
private fun AnimeRankingListItemSkeletonPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                AnimeRankingListItemSkeleton()
                AnimeRankingListItemSkeleton()
                AnimeRankingListItemSkeleton()
            }
        }
    }
}

@Preview
@Composable
private fun UserAnimeListItemSkeletonPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                UserAnimeListItemSkeleton()
                UserAnimeListItemSkeleton()
                UserAnimeListItemSkeleton()
            }
        }
    }
}
