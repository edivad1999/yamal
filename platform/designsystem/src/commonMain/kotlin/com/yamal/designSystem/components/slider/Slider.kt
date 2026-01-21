package com.yamal.designSystem.components.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.YamalTheme
import kotlin.math.roundToInt

/**
 * A custom slider component following Ant Design Mobile patterns.
 *
 * This is a Material-free implementation that provides a simple, customizable slider
 * with optional stepped values.
 *
 * @param value Current value of the slider
 * @param onValueChange Callback when the value changes
 * @param modifier Modifier for the slider
 * @param valueRange Range of valid values
 * @param steps Number of discrete steps (0 for continuous)
 * @param enabled Whether the slider is enabled
 * @param thumbColor Color of the thumb
 * @param activeTrackColor Color of the active (filled) track
 * @param inactiveTrackColor Color of the inactive track
 * @param thumbRadius Radius of the thumb
 * @param trackHeight Height of the track
 */
@Composable
fun YamalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    thumbColor: Color = YamalTheme.colors.primary,
    activeTrackColor: Color = YamalTheme.colors.primary,
    inactiveTrackColor: Color = YamalTheme.colors.border,
    thumbRadius: Dp = 12.dp,
    trackHeight: Dp = 4.dp,
) {
    val density = LocalDensity.current
    val thumbRadiusPx = with(density) { thumbRadius.toPx() }
    val trackHeightPx = with(density) { trackHeight.toPx() }

    // Calculate the total number of positions (steps + 1 or continuous)
    val totalSteps = if (steps > 0) steps + 1 else 0

    // Convert value to fraction
    val fraction =
        ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
            .coerceIn(0f, 1f)

    Box(
        modifier =
            modifier
                .height(thumbRadius * 2 + 8.dp)
                .padding(horizontal = thumbRadius),
    ) {
        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(thumbRadius * 2)
                    .pointerInput(enabled, valueRange, steps) {
                        if (!enabled) return@pointerInput

                        detectTapGestures { offset ->
                            val trackWidth = size.width.toFloat()
                            val newFraction = (offset.x / trackWidth).coerceIn(0f, 1f)
                            val newValue = calculateValue(newFraction, valueRange, totalSteps)
                            onValueChange(newValue)
                        }
                    }.pointerInput(enabled, valueRange, steps) {
                        if (!enabled) return@pointerInput

                        detectDragGestures { change, _ ->
                            change.consume()
                            val trackWidth = size.width.toFloat()
                            val newFraction = (change.position.x / trackWidth).coerceIn(0f, 1f)
                            val newValue = calculateValue(newFraction, valueRange, totalSteps)
                            onValueChange(newValue)
                        }
                    },
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerY = canvasHeight / 2

            // Draw inactive track
            drawRoundRect(
                color = if (enabled) inactiveTrackColor else inactiveTrackColor.copy(alpha = 0.38f),
                topLeft = Offset(0f, centerY - trackHeightPx / 2),
                size = Size(canvasWidth, trackHeightPx),
                cornerRadius = CornerRadius(trackHeightPx / 2),
            )

            // Draw active track
            val activeWidth = canvasWidth * fraction
            if (activeWidth > 0) {
                drawRoundRect(
                    color = if (enabled) activeTrackColor else activeTrackColor.copy(alpha = 0.38f),
                    topLeft = Offset(0f, centerY - trackHeightPx / 2),
                    size = Size(activeWidth, trackHeightPx),
                    cornerRadius = CornerRadius(trackHeightPx / 2),
                )
            }

            // Draw step marks if steps > 0
            if (steps > 0) {
                val stepWidth = canvasWidth / (steps + 1)
                for (i in 1..steps) {
                    val x = stepWidth * i
                    val isActive = x <= activeWidth
                    drawCircle(
                        color =
                            if (isActive) {
                                if (enabled) activeTrackColor else activeTrackColor.copy(alpha = 0.38f)
                            } else {
                                if (enabled) inactiveTrackColor else inactiveTrackColor.copy(alpha = 0.38f)
                            },
                        radius = trackHeightPx,
                        center = Offset(x, centerY),
                    )
                }
            }

            // Draw thumb
            val thumbX = canvasWidth * fraction
            drawCircle(
                color = if (enabled) thumbColor else thumbColor.copy(alpha = 0.38f),
                radius = thumbRadiusPx,
                center = Offset(thumbX, centerY),
            )

            // Draw thumb border
            drawCircle(
                color = if (enabled) thumbColor else thumbColor.copy(alpha = 0.38f),
                radius = thumbRadiusPx,
                center = Offset(thumbX, centerY),
                style = Stroke(width = 2f),
            )
        }
    }
}

/**
 * Calculate the actual value based on fraction, range, and steps.
 */
private fun calculateValue(
    fraction: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    totalSteps: Int,
): Float {
    val range = valueRange.endInclusive - valueRange.start
    return if (totalSteps > 0) {
        // Snap to nearest step
        val stepFraction = 1f / totalSteps
        val nearestStep = (fraction / stepFraction).roundToInt()
        valueRange.start + (nearestStep * range / totalSteps)
    } else {
        valueRange.start + (fraction * range)
    }
}
