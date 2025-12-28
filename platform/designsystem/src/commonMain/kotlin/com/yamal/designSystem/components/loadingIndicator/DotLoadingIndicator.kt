package com.yamal.designSystem.components.loadingIndicator

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

/**
 * Default values for [DotLoadingIndicator] following Ant Design Mobile specifications.
 *
 * The DotLoading component renders 3 dots that animate vertically with staggered timing,
 * creating a wave-like bouncing effect.
 *
 * @see <a href="https://mobile.ant.design/components/dot-loading">Ant Design Mobile DotLoading</a>
 */
object DotLoadingIndicatorDefaults {
    /** Default font size that determines dot size: 14sp (matches text) */
    val FontSize: TextUnit = 14.sp

    /** Animation duration for complete cycle: 2000ms (2s) */
    const val ANIMATION_DURATION_MS = 2000

    /** Delay between each dot's animation: 200ms (0.2s offset) */
    const val DOT_DELAY_MS = 200

    /** Number of dots */
    const val DOT_COUNT = 3

    /** Dot size: 8dp (square dots as per ADM SVG) */
    val DotSize: Dp = 4.dp

    /** Dot corner radius: 2dp (rx="2" in ADM SVG) */
    val DotCornerRadius: Dp = 2.dp

    /** Gap between dots (derived from ADM positioning: 26 - 8 = 18) */
    val DotGap: Dp = 4.dp

    /** Vertical bounce distance: 10dp (ADM moves from y=16 to y=6 and y=26) */
    val BounceDistance: Dp = 10.dp

    // Animation keyframe percentages (matching ADM: '0; 0.1; 0.3; 0.4; 1')

    /** Keyframe: reach top (10% of 2000ms = 200ms) */
    const val KEYFRAME_TOP = 0.10f

    /** Keyframe: reach bottom (30% of 2000ms = 600ms) */
    const val KEYFRAME_BOTTOM = 0.30f

    /** Keyframe: back to center (40% of 2000ms = 800ms) */
    const val KEYFRAME_CENTER = 0.40f

    /** Default color - maps to --adm-color-weak */
    val defaultColor: Color
        @Composable get() = YamalTheme.colors.textSecondary

    /** Primary color - maps to --adm-color-primary */
    val primaryColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** White color - for use on dark backgrounds */
    val whiteColor: Color
        @Composable get() = Color.White
}

/**
 * Dot loading indicator following Ant Design Mobile specifications.
 *
 * Displays 3 dots that animate vertically in a staggered wave pattern.
 * This loading indicator is designed to be used inline with text or in compact spaces.
 *
 * @param modifier Modifier for the loading indicator
 * @param fontSize Font size that determines the overall size of the dots (default: 14sp)
 * @param color Color of the dots (default: weak/secondary text color)
 *
 * Example usage:
 * ```
 * // Default dot loading
 * DotLoadingIndicator()
 *
 * // Larger size
 * DotLoadingIndicator(fontSize = 20.sp)
 *
 * // Primary color
 * DotLoadingIndicator(color = DotLoadingIndicatorDefaults.primaryColor)
 *
 * // White color (for dark backgrounds)
 * DotLoadingIndicator(color = DotLoadingIndicatorDefaults.whiteColor)
 *
 * // Inline with text
 * Row {
 *     Text("Loading")
 *     DotLoadingIndicator(fontSize = 14.sp)
 * }
 * ```
 */
@Composable
fun DotLoadingIndicator(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = DotLoadingIndicatorDefaults.FontSize,
    color: Color = DotLoadingIndicatorDefaults.defaultColor,
) {
    val density = LocalDensity.current

    // Scale dot dimensions based on fontSize ratio (14sp is baseline)
    val scale = fontSize.value / DotLoadingIndicatorDefaults.FontSize.value
    val dotSize = DotLoadingIndicatorDefaults.DotSize * scale
    val dotCornerRadius = DotLoadingIndicatorDefaults.DotCornerRadius * scale
    val dotGap = DotLoadingIndicatorDefaults.DotGap * scale
    val bounceDistance = DotLoadingIndicatorDefaults.BounceDistance * scale
    val bounceDistancePx = with(density) { bounceDistance.toPx() }

    val infiniteTransition = rememberInfiniteTransition()
    val duration = DotLoadingIndicatorDefaults.ANIMATION_DURATION_MS

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dotGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(DotLoadingIndicatorDefaults.DOT_COUNT) { index ->
            // Each dot starts 200ms after the previous one
            val delayMs = index * DotLoadingIndicatorDefaults.DOT_DELAY_MS

            // Calculate keyframe times with delay offset
            val startTime = delayMs
            val topTime = (duration * DotLoadingIndicatorDefaults.KEYFRAME_TOP).toInt() + delayMs
            val bottomTime = (duration * DotLoadingIndicatorDefaults.KEYFRAME_BOTTOM).toInt() + delayMs
            val centerTime = (duration * DotLoadingIndicatorDefaults.KEYFRAME_CENTER).toInt() + delayMs

            val upPosition = -bounceDistancePx
            val downPosition = bounceDistancePx

            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 0f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            keyframes {
                                durationMillis = duration
                                // ADM keyframes: '16; 6; 26; 16; 16' at times '0; 0.1; 0.3; 0.4; 1'
                                // Translated: center(0) -> up(-10) -> down(+10) -> center(0) -> hold
                                0f at startTime // Start at center
                                upPosition at topTime // Move up (negative Y)
                                downPosition at bottomTime // Move down (positive Y)
                                0f at centerTime // Back to center
                                0f at duration // Hold at center
                            },
                        repeatMode = RepeatMode.Restart,
                    ),
            )

            AnimatedDot(
                size = dotSize,
                cornerRadius = dotCornerRadius,
                color = color,
                offsetY = offsetY,
            )
        }
    }
}

@Composable
private fun AnimatedDot(
    size: Dp,
    cornerRadius: Dp,
    color: Color,
    offsetY: Float,
) {
    Box(
        modifier =
            Modifier
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .size(size)
                .clip(RoundedCornerShape(cornerRadius))
                .background(color),
    )
}

// Previews

@Preview
@Composable
private fun DotLoadingIndicatorDefaultPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Default",
                    style = YamalTheme.typography.bodyMedium,
                )
                DotLoadingIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun DotLoadingIndicatorSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Different Sizes",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DotLoadingIndicator(fontSize = 10.sp)
                    DotLoadingIndicator(fontSize = 14.sp)
                    DotLoadingIndicator(fontSize = 20.sp)
                    DotLoadingIndicator(fontSize = 28.sp)
                }
            }
        }
    }
}

@Preview
@Composable
private fun DotLoadingIndicatorColorsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Different Colors",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Default (weak)
                    DotLoadingIndicator()

                    // Primary
                    DotLoadingIndicator(
                        color = DotLoadingIndicatorDefaults.primaryColor,
                    )

                    // Custom color
                    DotLoadingIndicator(
                        color = YamalTheme.colors.danger,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DotLoadingIndicatorWhitePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.primary) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "White on colored background",
                    style = YamalTheme.typography.bodyMedium,
                    color = Color.White,
                )
                DotLoadingIndicator(
                    color = DotLoadingIndicatorDefaults.whiteColor,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun DotLoadingIndicatorInlinePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Inline with Text",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Loading",
                        style = YamalTheme.typography.body,
                    )
                    DotLoadingIndicator(
                        fontSize = 14.sp,
                        color = DotLoadingIndicatorDefaults.primaryColor,
                    )
                }
            }
        }
    }
}
