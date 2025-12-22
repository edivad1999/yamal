package com.yamal.designSystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Avatar shape for skeleton.
 */
@Immutable
enum class YamalSkeletonAvatarShape {
    Circle,
    Square,
}

/**
 * Avatar size for skeleton.
 */
@Immutable
enum class YamalSkeletonAvatarSize {
    Small,
    Default,
    Large,
}

/**
 * Button shape for skeleton.
 */
@Immutable
enum class YamalSkeletonButtonShape {
    Default,
    Circle,
    Round,
    Square,
}

/**
 * Creates an animated shimmer effect for skeleton loading.
 */
@Composable
private fun shimmerBrush(
    baseColor: Color,
    highlightColor: Color,
    active: Boolean,
): Brush {
    if (!active) {
        return Brush.linearGradient(listOf(baseColor, baseColor))
    }

    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
    )

    return Brush.linearGradient(
        colors =
            listOf(
                baseColor,
                highlightColor,
                baseColor,
            ),
        start = Offset(translateAnimation - 500f, 0f),
        end = Offset(translateAnimation, 0f),
    )
}

/**
 * Base skeleton element with shimmer animation.
 */
@Composable
private fun SkeletonElement(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    active: Boolean = true,
) {
    val colors = YamalTheme.colors
    val baseColor = colors.neutralColors.fillTertiary
    val highlightColor = colors.neutralColors.fillQuaternary

    val brush = shimmerBrush(baseColor, highlightColor, active)

    Box(
        modifier =
            modifier
                .clip(shape)
                .background(brush),
    )
}

/**
 * A skeleton loading component following Ant Design guidelines.
 * Shows placeholder content while actual content is loading.
 *
 * @param modifier Modifier for the skeleton
 * @param active Whether to show animation
 * @param avatar Whether to show avatar placeholder
 * @param avatarShape Shape of the avatar
 * @param avatarSize Size of the avatar
 * @param title Whether to show title placeholder
 * @param titleWidth Width of the title
 * @param paragraph Whether to show paragraph placeholder
 * @param paragraphRows Number of paragraph rows
 * @param round Whether to use rounded corners for title and paragraph
 * @param loading Whether to show skeleton (true) or children (false)
 * @param content Content to show when not loading
 */
@Composable
fun YamalSkeleton(
    modifier: Modifier = Modifier,
    active: Boolean = true,
    avatar: Boolean = false,
    avatarShape: YamalSkeletonAvatarShape = YamalSkeletonAvatarShape.Circle,
    avatarSize: YamalSkeletonAvatarSize = YamalSkeletonAvatarSize.Default,
    title: Boolean = true,
    titleWidth: Dp? = null,
    paragraph: Boolean = true,
    paragraphRows: Int = 3,
    round: Boolean = false,
    loading: Boolean = true,
    content: @Composable (() -> Unit)? = null,
) {
    if (!loading && content != null) {
        content()
        return
    }

    val avatarDimension =
        when (avatarSize) {
            YamalSkeletonAvatarSize.Small -> 24.dp
            YamalSkeletonAvatarSize.Default -> 32.dp
            YamalSkeletonAvatarSize.Large -> 40.dp
        }

    val avatarShapeValue =
        when (avatarShape) {
            YamalSkeletonAvatarShape.Circle -> CircleShape
            YamalSkeletonAvatarShape.Square -> RoundedCornerShape(4.dp)
        }

    val textShape = if (round) RoundedCornerShape(50) else RoundedCornerShape(4.dp)

    Row(modifier = modifier) {
        if (avatar) {
            SkeletonElement(
                modifier = Modifier.size(avatarDimension),
                shape = avatarShapeValue,
                active = active,
            )
            Spacer(Modifier.width(Dimension.Spacing.md))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
        ) {
            if (title) {
                SkeletonElement(
                    modifier =
                        Modifier
                            .width(titleWidth ?: 120.dp)
                            .height(16.dp),
                    shape = textShape,
                    active = active,
                )
            }

            if (paragraph) {
                repeat(paragraphRows) { index ->
                    val width =
                        when {
                            index == paragraphRows - 1 -> 0.6f
                            else -> 1f
                        }
                    SkeletonElement(
                        modifier =
                            Modifier
                                .fillMaxWidth(width)
                                .height(14.dp),
                        shape = textShape,
                        active = active,
                    )
                }
            }
        }
    }
}

/**
 * Skeleton avatar placeholder.
 */
@Composable
fun YamalSkeletonAvatar(
    modifier: Modifier = Modifier,
    active: Boolean = true,
    shape: YamalSkeletonAvatarShape = YamalSkeletonAvatarShape.Circle,
    size: YamalSkeletonAvatarSize = YamalSkeletonAvatarSize.Default,
) {
    val dimension =
        when (size) {
            YamalSkeletonAvatarSize.Small -> 24.dp
            YamalSkeletonAvatarSize.Default -> 32.dp
            YamalSkeletonAvatarSize.Large -> 40.dp
        }

    val shapeValue =
        when (shape) {
            YamalSkeletonAvatarShape.Circle -> CircleShape
            YamalSkeletonAvatarShape.Square -> RoundedCornerShape(4.dp)
        }

    SkeletonElement(
        modifier = modifier.size(dimension),
        shape = shapeValue,
        active = active,
    )
}

/**
 * Skeleton button placeholder.
 */
@Composable
fun YamalSkeletonButton(
    modifier: Modifier = Modifier,
    active: Boolean = true,
    shape: YamalSkeletonButtonShape = YamalSkeletonButtonShape.Default,
    size: YamalButtonSize = YamalButtonSize.Middle,
    block: Boolean = false,
) {
    val height =
        when (size) {
            YamalButtonSize.Small -> 24.dp
            YamalButtonSize.Middle -> 32.dp
            YamalButtonSize.Large -> 40.dp
        }

    val width =
        when {
            block -> Dp.Unspecified
            shape == YamalSkeletonButtonShape.Circle -> height
            else -> 80.dp
        }

    val shapeValue =
        when (shape) {
            YamalSkeletonButtonShape.Default -> RoundedCornerShape(6.dp)
            YamalSkeletonButtonShape.Circle -> CircleShape
            YamalSkeletonButtonShape.Round -> RoundedCornerShape(50)
            YamalSkeletonButtonShape.Square -> RoundedCornerShape(0.dp)
        }

    val sizeModifier =
        if (block) {
            modifier.fillMaxWidth().height(height)
        } else {
            modifier.size(width = width, height = height)
        }

    SkeletonElement(
        modifier = sizeModifier,
        shape = shapeValue,
        active = active,
    )
}

/**
 * Skeleton input placeholder.
 */
@Composable
fun YamalSkeletonInput(
    modifier: Modifier = Modifier,
    active: Boolean = true,
    size: YamalInputSize = YamalInputSize.Middle,
) {
    val height =
        when (size) {
            YamalInputSize.Small -> 24.dp
            YamalInputSize.Middle -> 32.dp
            YamalInputSize.Large -> 40.dp
        }

    SkeletonElement(
        modifier =
            modifier
                .fillMaxWidth()
                .height(height),
        shape = RoundedCornerShape(6.dp),
        active = active,
    )
}

/**
 * Skeleton image placeholder.
 */
@Composable
fun YamalSkeletonImage(
    modifier: Modifier = Modifier,
    active: Boolean = true,
    width: Dp = 200.dp,
    height: Dp = 200.dp,
) {
    SkeletonElement(
        modifier = modifier.size(width = width, height = height),
        shape = RoundedCornerShape(8.dp),
        active = active,
    )
}

// Previews

@Preview
@Composable
private fun YamalSkeletonBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            ) {
                Text("Basic Skeleton", style = YamalTheme.typography.bodyMedium)
                YamalSkeleton(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
private fun YamalSkeletonWithAvatarPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            ) {
                Text("With Avatar", style = YamalTheme.typography.bodyMedium)
                YamalSkeleton(
                    avatar = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Text("Square Avatar", style = YamalTheme.typography.bodyMedium)
                YamalSkeleton(
                    avatar = true,
                    avatarShape = YamalSkeletonAvatarShape.Square,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalSkeletonElementsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Avatar Sizes", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    YamalSkeletonAvatar(size = YamalSkeletonAvatarSize.Small)
                    YamalSkeletonAvatar(size = YamalSkeletonAvatarSize.Default)
                    YamalSkeletonAvatar(size = YamalSkeletonAvatarSize.Large)
                }

                Text("Button Shapes", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
                    YamalSkeletonButton(shape = YamalSkeletonButtonShape.Default)
                    YamalSkeletonButton(shape = YamalSkeletonButtonShape.Circle)
                    YamalSkeletonButton(shape = YamalSkeletonButtonShape.Round)
                }

                Text("Input", style = YamalTheme.typography.bodyMedium)
                YamalSkeletonInput()
            }
        }
    }
}

@Preview
@Composable
private fun YamalSkeletonRoundPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            ) {
                Text("Round Skeleton", style = YamalTheme.typography.bodyMedium)
                YamalSkeleton(
                    round = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalSkeletonInactivePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            ) {
                Text("Inactive (No Animation)", style = YamalTheme.typography.bodyMedium)
                YamalSkeleton(
                    active = false,
                    avatar = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
