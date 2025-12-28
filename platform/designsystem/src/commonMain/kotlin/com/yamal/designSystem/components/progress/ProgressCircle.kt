package com.yamal.designSystem.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.min

/**
 * Default values for [ProgressCircle] component following Ant Design Mobile specifications.
 */
object ProgressCircleDefaults {
    /** Circle size: 50dp */
    val Size: Dp = 50.dp

    /** Track width (stroke): 3dp */
    val TrackWidth: Dp = 3.dp

    /** Animation duration: 350ms */
    const val AnimationDurationMs = 350

    /** Start angle: -90 degrees (top) */
    const val StartAngle = -90f

    /** Track color */
    val trackColor: Color
        @Composable get() = YamalTheme.colors.border

    /** Fill color (progress) */
    val fillColor: Color
        @Composable get() = YamalTheme.colors.primary
}

/**
 * Circular progress indicator component following Ant Design Mobile specifications.
 *
 * Displays a circular progress indicator with optional content in the center.
 *
 * @param percent Progress percentage value (0-100)
 * @param modifier Modifier for the progress circle
 * @param size Diameter of the circle
 * @param trackWidth Width of the progress track stroke
 * @param trackColor Background track color
 * @param fillColor Progress fill color
 * @param content Optional composable slot for content displayed in the center of the circle
 *
 * Example usage:
 * ```
 * // Basic progress circle
 * ProgressCircle(percent = 50f)
 *
 * // With percentage text
 * ProgressCircle(percent = 75f) {
 *     Text("75%")
 * }
 *
 * // Custom size and colors
 * ProgressCircle(
 *     percent = 60f,
 *     size = 80.dp,
 *     trackWidth = 6.dp,
 *     fillColor = Color.Green
 * ) {
 *     Icon(Icons.Outlined.Check, contentDescription = null)
 * }
 * ```
 */
@Composable
fun ProgressCircle(
    percent: Float,
    modifier: Modifier = Modifier,
    size: Dp = ProgressCircleDefaults.Size,
    trackWidth: Dp = ProgressCircleDefaults.TrackWidth,
    trackColor: Color = ProgressCircleDefaults.trackColor,
    fillColor: Color = ProgressCircleDefaults.fillColor,
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    val animatedPercent by animateFloatAsState(
        targetValue = percent.coerceIn(0f, 100f),
        animationSpec = tween(ProgressCircleDefaults.AnimationDurationMs),
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = trackWidth.toPx()
            val diameter = min(this.size.width, this.size.height) - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)
            val arcSize = Size(diameter, diameter)

            // Track (background circle)
            drawArc(
                color = trackColor,
                startAngle = ProgressCircleDefaults.StartAngle,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )

            // Fill (progress arc)
            val sweepAngle = 360f * (animatedPercent / 100f)
            drawArc(
                color = fillColor,
                startAngle = ProgressCircleDefaults.StartAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }

        // Center content
        if (content != null) {
            content()
        }
    }
}

/**
 * Convenience overload that shows percentage text in the center.
 *
 * @param percent Progress percentage value (0-100)
 * @param showText Whether to show the percentage text in the center
 * @param modifier Modifier for the progress circle
 * @param size Diameter of the circle
 * @param trackWidth Width of the progress track stroke
 * @param trackColor Background track color
 * @param fillColor Progress fill color
 * @param textColor Color for the percentage text
 */
@Composable
fun ProgressCircle(
    percent: Float,
    showText: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = ProgressCircleDefaults.Size,
    trackWidth: Dp = ProgressCircleDefaults.TrackWidth,
    trackColor: Color = ProgressCircleDefaults.trackColor,
    fillColor: Color = ProgressCircleDefaults.fillColor,
    textColor: Color = YamalTheme.colors.text,
) {
    ProgressCircle(
        percent = percent,
        modifier = modifier,
        size = size,
        trackWidth = trackWidth,
        trackColor = trackColor,
        fillColor = fillColor,
        content =
            if (showText) {
                {
                    Text(
                        text = "${percent.toInt()}%",
                        fontSize = (size.value / 4).sp,
                        color = textColor,
                    )
                }
            } else {
                null
            },
    )
}

// Previews

@Preview
@Composable
private fun ProgressCircleBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Basic Progress Circle",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProgressCircle(percent = 0f)
                    ProgressCircle(percent = 25f)
                    ProgressCircle(percent = 50f)
                    ProgressCircle(percent = 75f)
                    ProgressCircle(percent = 100f)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProgressCircleWithTextPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "With Text",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProgressCircle(percent = 25f, showText = true)
                    ProgressCircle(percent = 50f, showText = true)
                    ProgressCircle(percent = 75f, showText = true)
                    ProgressCircle(percent = 100f, showText = true)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProgressCircleSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Different Sizes",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ProgressCircle(
                        percent = 60f,
                        size = 32.dp,
                        trackWidth = 2.dp,
                    )
                    ProgressCircle(
                        percent = 60f,
                        size = 50.dp,
                        trackWidth = 3.dp,
                        showText = true,
                    )
                    ProgressCircle(
                        percent = 60f,
                        size = 80.dp,
                        trackWidth = 5.dp,
                        showText = true,
                    )
                    ProgressCircle(
                        percent = 60f,
                        size = 120.dp,
                        trackWidth = 6.dp,
                        showText = true,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProgressCircleCustomColorsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Custom Colors",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProgressCircle(
                        percent = 60f,
                        fillColor = YamalTheme.colors.success,
                        showText = true,
                    )
                    ProgressCircle(
                        percent = 40f,
                        fillColor = YamalTheme.colors.warning,
                        showText = true,
                    )
                    ProgressCircle(
                        percent = 80f,
                        fillColor = YamalTheme.colors.danger,
                        showText = true,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProgressCircleCustomContentPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Custom Content",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProgressCircle(
                        percent = 100f,
                        size = 60.dp,
                        trackWidth = 4.dp,
                        fillColor = YamalTheme.colors.success,
                    ) {
                        Text(
                            text = "Done",
                            fontSize = 12.sp,
                            color = YamalTheme.colors.success,
                        )
                    }
                    ProgressCircle(
                        percent = 75f,
                        size = 60.dp,
                        trackWidth = 4.dp,
                    ) {
                        Text(
                            text = "3/4",
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
    }
}
