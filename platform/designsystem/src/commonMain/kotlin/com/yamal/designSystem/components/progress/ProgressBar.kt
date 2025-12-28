package com.yamal.designSystem.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.preview.PlatformPreviewContextConfigurationEffect
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [ProgressBar] component following Ant Design Mobile specifications.
 */
object ProgressBarDefaults {
    /** Track width (height): 8dp */
    val TrackWidth: Dp = 8.dp

    /** Text width: 40dp */
    val TextWidth: Dp = 40.dp

    /** Text left padding: 8dp */
    val TextPadding: Dp = 8.dp

    /** Animation duration: 300ms */
    const val AnimationDurationMs = 300

    /** Font size for text */
    val TextFontSize = 14.sp

    /** Track color */
    val trackColor: Color
        @Composable get() = YamalTheme.colors.border

    /** Fill color (progress) */
    val fillColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Text color - --adm-color-weak */
    val textColor: Color
        @Composable get() = YamalTheme.colors.weak
}

/**
 * Linear progress bar component following Ant Design Mobile specifications.
 *
 * Displays a horizontal progress indicator with optional text.
 *
 * @param percent Progress percentage value (0-100)
 * @param modifier Modifier for the progress bar
 * @param rounded Whether to apply rounded corners (default true)
 * @param trackWidth Height of the progress track
 * @param trackColor Background track color
 * @param fillColor Progress fill color
 * @param text Optional composable slot for text content displayed after the bar.
 *   When null, no text is shown.
 *
 * Example usage:
 * ```
 * // Basic progress bar
 * ProgressBar(percent = 50f)
 *
 * // With percentage text
 * ProgressBar(
 *     percent = 75f,
 *     text = { Text("${it.toInt()}%") }
 * )
 *
 * // Custom colors
 * ProgressBar(
 *     percent = 30f,
 *     fillColor = Color.Green,
 *     trackColor = Color.LightGray
 * )
 *
 * // Square corners
 * ProgressBar(
 *     percent = 60f,
 *     rounded = false
 * )
 * ```
 */
@Composable
fun ProgressBar(
    percent: Float,
    modifier: Modifier = Modifier,
    rounded: Boolean = true,
    trackWidth: Dp = ProgressBarDefaults.TrackWidth,
    trackColor: Color = ProgressBarDefaults.trackColor,
    fillColor: Color = ProgressBarDefaults.fillColor,
    text: @Composable (RowScope.(percent: Float) -> Unit)? = null,
) {
    val animatedPercent by animateFloatAsState(
        targetValue = percent.coerceIn(0f, 100f),
        animationSpec = tween(ProgressBarDefaults.AnimationDurationMs),
    )

    val shape: Shape =
        if (rounded) {
            RoundedCornerShape(trackWidth / 2)
        } else {
            RoundedCornerShape(0.dp)
        }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Trail (background track) with fill
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(trackWidth)
                    .clip(shape)
                    .background(trackColor),
        ) {
            // Fill (progress)
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(animatedPercent / 100f)
                        .height(trackWidth)
                        .clip(shape)
                        .background(fillColor),
            )
        }

        // Text (optional)
        if (text != null) {
            Spacer(Modifier.width(ProgressBarDefaults.TextPadding))
            text(animatedPercent)
        }
    }
}

/**
 * Convenience overload that shows percentage text.
 *
 * @param percent Progress percentage value (0-100)
 * @param modifier Modifier for the progress bar
 * @param showText Whether to show the percentage text
 * @param rounded Whether to apply rounded corners (default true)
 * @param trackWidth Height of the progress track
 * @param trackColor Background track color
 * @param fillColor Progress fill color
 * @param textColor Color for the percentage text
 */
@Composable
fun ProgressBar(
    percent: Float,
    showText: Boolean,
    modifier: Modifier = Modifier,
    rounded: Boolean = true,
    trackWidth: Dp = ProgressBarDefaults.TrackWidth,
    trackColor: Color = ProgressBarDefaults.trackColor,
    fillColor: Color = ProgressBarDefaults.fillColor,
    textColor: Color = ProgressBarDefaults.textColor,
) {
    ProgressBar(
        percent = percent,
        modifier = modifier,
        rounded = rounded,
        trackWidth = trackWidth,
        trackColor = trackColor,
        fillColor = fillColor,
        text =
            if (showText) {
                { p ->
                    Text(
                        text = "${p.toInt()}%",
                        fontSize = ProgressBarDefaults.TextFontSize,
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
private fun ProgressBarBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Basic Progress Bar",
                    style = YamalTheme.typography.bodyMedium,
                )
                ProgressBar(percent = 0f)
                ProgressBar(percent = 30f)
                ProgressBar(percent = 50f)
                ProgressBar(percent = 75f)
                ProgressBar(percent = 100f)
            }
        }
    }
}

@Preview
@Composable
private fun ProgressBarWithTextPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "With Text",
                    style = YamalTheme.typography.bodyMedium,
                )
                ProgressBar(percent = 30f, showText = true)
                ProgressBar(percent = 50f, showText = true)
                ProgressBar(percent = 100f, showText = true)
            }
        }
    }
}

@Preview
@Composable
private fun ProgressBarRoundedPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Rounded vs Square",
                    style = YamalTheme.typography.bodyMedium,
                )
                ProgressBar(percent = 50f, rounded = true, showText = true)
                ProgressBar(percent = 50f, rounded = false, showText = true)
            }
        }
    }
}

@Preview
@Composable
private fun ProgressBarCustomColorsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Custom Colors",
                    style = YamalTheme.typography.bodyMedium,
                )
                ProgressBar(
                    percent = 60f,
                    fillColor = YamalTheme.colors.success,
                    showText = true,
                )
                ProgressBar(
                    percent = 40f,
                    fillColor = YamalTheme.colors.warning,
                    showText = true,
                )
                ProgressBar(
                    percent = 80f,
                    fillColor = YamalTheme.colors.danger,
                    showText = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProgressBarCustomTextPreview() {
    YamalTheme {
        PlatformPreviewContextConfigurationEffect()
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Custom Text",
                    style = YamalTheme.typography.bodyMedium,
                )
                ProgressBar(
                    percent = 75f,
                    text = { p -> Text("${p.toInt()}/100") },
                )
                ProgressBar(
                    percent = 50f,
                    text = { Text("Loading...") },
                )
            }
        }
    }
}
