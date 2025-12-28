package com.yamal.designSystem.components.skeleton

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for Skeleton components following Ant Design Mobile specifications.
 *
 * ADM CSS values:
 * - Base color: rgba(190, 190, 190, 0.2)
 * - Highlight color: rgba(129, 129, 129, 0.24)
 * - Animation: 1.4s ease infinite
 * - Background-size: 400% 100%
 *
 * @see <a href="https://mobile.ant.design/components/skeleton">Ant Design Mobile Skeleton</a>
 */
object SkeletonDefaults {
    /** Animation duration: 1400ms (1.4s) */
    const val ANIMATION_DURATION_MS = 1400

    /** Border radius for title and paragraph lines: 2dp */
    val BorderRadius: Dp = 2.dp

    /** Title width: 45% */
    const val TITLE_WIDTH_FRACTION = 0.45f

    /** Title height: 32dp */
    val TitleHeight: Dp = 32.dp

    /** Paragraph line height: 18dp */
    val ParagraphLineHeight: Dp = 18.dp

    /** Paragraph line spacing: 12dp */
    val ParagraphLineSpacing: Dp = 12.dp

    /** Last line width: 65% */
    const val LAST_LINE_WIDTH_FRACTION = 0.65f

    /** Default paragraph line count */
    const val DEFAULT_LINE_COUNT = 3

    /** Base color: rgba(190, 190, 190, 0.2) */
    val baseColor: Color
        @Composable get() = Color(0x33BEBEBE)

    /** Highlight color: rgba(129, 129, 129, 0.24) */
    val highlightColor: Color
        @Composable get() = Color(0x3D818181)
}

/**
 * Base skeleton placeholder element following Ant Design Mobile specifications.
 *
 * @param modifier Modifier for the skeleton
 * @param animated Whether to show shimmer animation (default: true)
 * @param width Fixed width, or null to use modifier width
 * @param height Height of the skeleton
 * @param borderRadius Corner radius
 * @param baseColor Base background color
 * @param highlightColor Shimmer highlight color
 */
@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    animated: Boolean = true,
    width: Dp? = null,
    height: Dp = 0.dp,
    borderRadius: Dp = 0.dp,
    baseColor: Color = SkeletonDefaults.baseColor,
    highlightColor: Color = SkeletonDefaults.highlightColor,
) {
    val shape = RoundedCornerShape(borderRadius)

    val sizeModifier =
        if (width != null) {
            modifier.width(width).height(height)
        } else {
            modifier.height(height)
        }

    if (!animated) {
        // Static skeleton - just solid color
        Box(
            modifier =
                sizeModifier
                    .clip(shape)
                    .background(baseColor),
        )
    } else {
        // Animated skeleton with shimmer
        val transition = rememberInfiniteTransition()

        // Animate from 0 to 1 (background-position: 100% -> 0%)
        val progress by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            durationMillis = SkeletonDefaults.ANIMATION_DURATION_MS,
                            easing = FastOutSlowInEasing,
                        ),
                    repeatMode = RepeatMode.Restart,
                ),
        )

        Box(
            modifier =
                sizeModifier
                    .clip(shape)
                    .drawWithCache {
                        // ADM uses 400% background-size with gradient at 25%, 37%, 63%
                        // We create a gradient 4x wider than the element
                        val gradientWidth = size.width * 4f

                        // At progress=0: show rightmost portion (highlight off-screen right)
                        // At progress=1: show leftmost portion (highlight off-screen left)
                        // Offset moves from right to left
                        val offsetX = (1f - progress) * (gradientWidth - size.width)

                        val brush =
                            Brush.linearGradient(
                                colorStops =
                                    arrayOf(
                                        0.00f to baseColor,
                                        0.25f to baseColor,
                                        0.37f to highlightColor,
                                        0.63f to baseColor,
                                        1.00f to baseColor,
                                    ),
                                start = Offset(-offsetX, 0f),
                                end = Offset(gradientWidth - offsetX, 0f),
                            )

                        onDrawBehind {
                            drawRect(brush)
                        }
                    },
        )
    }
}

/**
 * Skeleton title placeholder.
 *
 * Dimensions: 45% width, 32dp height, 2dp border-radius
 */
@Composable
fun SkeletonTitle(
    modifier: Modifier = Modifier,
    animated: Boolean = true,
    baseColor: Color = SkeletonDefaults.baseColor,
    highlightColor: Color = SkeletonDefaults.highlightColor,
) {
    Skeleton(
        modifier = modifier.fillMaxWidth(SkeletonDefaults.TITLE_WIDTH_FRACTION),
        animated = animated,
        height = SkeletonDefaults.TitleHeight,
        borderRadius = SkeletonDefaults.BorderRadius,
        baseColor = baseColor,
        highlightColor = highlightColor,
    )
}

/**
 * Skeleton paragraph placeholder with multiple lines.
 *
 * Each line: 18dp height, 2dp border-radius
 * Last line: 65% width
 */
@Composable
fun SkeletonParagraph(
    modifier: Modifier = Modifier,
    animated: Boolean = true,
    lineCount: Int = SkeletonDefaults.DEFAULT_LINE_COUNT,
    baseColor: Color = SkeletonDefaults.baseColor,
    highlightColor: Color = SkeletonDefaults.highlightColor,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SkeletonDefaults.ParagraphLineSpacing),
    ) {
        repeat(lineCount) { index ->
            val isLastLine = index == lineCount - 1
            Skeleton(
                modifier =
                    if (isLastLine) {
                        Modifier.fillMaxWidth(SkeletonDefaults.LAST_LINE_WIDTH_FRACTION)
                    } else {
                        Modifier.fillMaxWidth()
                    },
                animated = animated,
                height = SkeletonDefaults.ParagraphLineHeight,
                borderRadius = SkeletonDefaults.BorderRadius,
                baseColor = baseColor,
                highlightColor = highlightColor,
            )
        }
    }
}

// Previews

@Preview
@Composable
private fun SkeletonBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Basic Skeleton", style = YamalTheme.typography.bodyMedium)
                Skeleton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 20.dp,
                    borderRadius = 4.dp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SkeletonTitlePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Title", style = YamalTheme.typography.bodyMedium)
                SkeletonTitle()

                Text("Title (static)", style = YamalTheme.typography.bodyMedium)
                SkeletonTitle(animated = false)
            }
        }
    }
}

@Preview
@Composable
private fun SkeletonParagraphPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Paragraph (3 lines)", style = YamalTheme.typography.bodyMedium)
                SkeletonParagraph()

                Text("Paragraph (5 lines)", style = YamalTheme.typography.bodyMedium)
                SkeletonParagraph(lineCount = 5)
            }
        }
    }
}

@Preview
@Composable
private fun SkeletonCombinedPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Title + Paragraph", style = YamalTheme.typography.bodyMedium)
                SkeletonTitle()
                SkeletonParagraph()
            }
        }
    }
}

@Preview
@Composable
private fun SkeletonCustomPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Custom Shapes", style = YamalTheme.typography.bodyMedium)

                // Avatar + text
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Skeleton(
                        width = 48.dp,
                        height = 48.dp,
                        borderRadius = 24.dp,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Skeleton(width = 120.dp, height = 16.dp, borderRadius = 2.dp)
                        Skeleton(width = 80.dp, height = 14.dp, borderRadius = 2.dp)
                    }
                }

                // Card
                Skeleton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 120.dp,
                    borderRadius = 8.dp,
                )
            }
        }
    }
}
