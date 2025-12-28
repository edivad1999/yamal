package com.yamal.designSystem.components.loadingIndicator

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [SpinLoadingIndicator] following Ant Design Mobile specifications.
 *
 * CSS Variables mapped:
 * - `--size`: 32px (default size)
 * - `--color`: var(--adm-color-weak) (default color)
 *
 * @see <a href="https://mobile.ant.design/components/spin-loading">Ant Design Mobile SpinLoading</a>
 */
object SpinLoadingIndicatorDefaults {
    /** Default spinner size: 32dp (from --size CSS variable) */
    val Size: Dp = 32.dp

    /** Sweep animation duration: 1200ms (oscillates between 80% and 30%) */
    const val SWEEP_ANIMATION_DURATION_MS = 1200

    /** Rotation animation duration: 1600ms for full 360° rotation */
    const val ROTATION_ANIMATION_DURATION_MS = 1200

    /** Stroke width relative to size (approximately 1/8 of size) */
    const val STROKE_WIDTH_RATIO = 0.125f

    /** Maximum sweep angle percentage (80% of 360°) */
    const val MAX_SWEEP_PERCENT = 0.80f

    /** Minimum sweep angle percentage (30% of 360°) */
    const val MIN_SWEEP_PERCENT = 0.30f

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
 * Spin loading indicator following Ant Design Mobile specifications.
 *
 * A circular spinning indicator that rotates continuously to show loading state.
 * The spinner uses an arc that rotates 360 degrees in a continuous loop.
 *
 * @param modifier Modifier for the spinner
 * @param size Size of the spinner (default: 32dp)
 * @param color Color of the spinner (default: weak/secondary text color)
 *
 * Example usage:
 * ```
 * // Default spinner
 * SpinLoadingIndicator()
 *
 * // Custom size
 * SpinLoadingIndicator(size = 48.dp)
 *
 * // Primary color
 * SpinLoadingIndicator(color = SpinLoadingIndicatorDefaults.primaryColor)
 *
 * // White color (for dark backgrounds)
 * SpinLoadingIndicator(color = SpinLoadingIndicatorDefaults.whiteColor)
 *
 * // Custom color
 * SpinLoadingIndicator(color = Color.Red)
 * ```
 */
@Composable
fun SpinLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = SpinLoadingIndicatorDefaults.Size,
    color: Color = SpinLoadingIndicatorDefaults.defaultColor,
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Rotation animation - continuous spin
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = SpinLoadingIndicatorDefaults.ROTATION_ANIMATION_DURATION_MS,
                        easing = LinearEasing,
                    ),
            ),
    )

    // Sweep angle animation - oscillates between 80% and 30% (reverse loop)
    val sweepPercent by infiniteTransition.animateFloat(
        initialValue = SpinLoadingIndicatorDefaults.MAX_SWEEP_PERCENT,
        targetValue = SpinLoadingIndicatorDefaults.MIN_SWEEP_PERCENT,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = SpinLoadingIndicatorDefaults.SWEEP_ANIMATION_DURATION_MS,
                        easing = LinearEasing,
                    ),
                repeatMode = RepeatMode.Reverse,
            ),
    )

    val sweepAngle = 360f * sweepPercent
    val strokeWidth = size.value * SpinLoadingIndicatorDefaults.STROKE_WIDTH_RATIO

    Canvas(
        modifier = modifier.size(size),
    ) {
        rotate(rotation) {
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style =
                    Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                    ),
            )
        }
    }
}

// Previews

@Preview
@Composable
private fun SpinLoadingIndicatorDefaultPreview() {
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
                SpinLoadingIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun SpinLoadingIndicatorSizesPreview() {
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
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SpinLoadingIndicator(size = 20.dp)
                    SpinLoadingIndicator(size = 32.dp)
                    SpinLoadingIndicator(size = 48.dp)
                    SpinLoadingIndicator(size = 64.dp)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SpinLoadingIndicatorColorsPreview() {
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
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Default (weak)
                    SpinLoadingIndicator()

                    // Primary
                    SpinLoadingIndicator(
                        color = SpinLoadingIndicatorDefaults.primaryColor,
                    )

                    // Custom color
                    SpinLoadingIndicator(
                        color = YamalTheme.colors.warning,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SpinLoadingIndicatorWhitePreview() {
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
                SpinLoadingIndicator(
                    color = SpinLoadingIndicatorDefaults.whiteColor,
                )
            }
        }
    }
}
