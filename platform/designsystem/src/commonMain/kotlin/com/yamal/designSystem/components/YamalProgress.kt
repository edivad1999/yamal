package com.yamal.designSystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.min

/**
 * Progress type following Ant Design guidelines.
 */
@Immutable
enum class YamalProgressType {
    Line,
    Circle,
    Dashboard,
}

/**
 * Progress status following Ant Design guidelines.
 */
@Immutable
enum class YamalProgressStatus {
    Normal,
    Success,
    Exception,
    Active,
}

/**
 * Progress size following Ant Design guidelines.
 */
@Immutable
enum class YamalProgressSize {
    Small,
    Default,
}

/**
 * A progress indicator component following Ant Design guidelines.
 *
 * @param percent Current progress percentage (0-100)
 * @param modifier Modifier for the progress
 * @param type Type of progress (Line, Circle, Dashboard)
 * @param status Status of the progress
 * @param showInfo Whether to show percentage or status icon
 * @param strokeColor Custom stroke color
 * @param trailColor Custom trail color
 * @param strokeWidth Custom stroke width (for circle/dashboard)
 * @param size Size of the progress
 * @param format Custom format function for the info text
 */
@Composable
fun YamalProgress(
    percent: Float,
    modifier: Modifier = Modifier,
    type: YamalProgressType = YamalProgressType.Line,
    status: YamalProgressStatus = YamalProgressStatus.Normal,
    showInfo: Boolean = true,
    strokeColor: Color? = null,
    trailColor: Color? = null,
    strokeWidth: Dp? = null,
    size: YamalProgressSize = YamalProgressSize.Default,
    format: ((Float) -> String)? = null,
) {
    val colors = YamalTheme.colors

    // Determine effective status (auto-success at 100%)
    val effectiveStatus =
        if (percent >= 100f && status == YamalProgressStatus.Normal) {
            YamalProgressStatus.Success
        } else {
            status
        }

    // Determine color based on status
    val progressColor =
        strokeColor ?: when (effectiveStatus) {
            YamalProgressStatus.Normal, YamalProgressStatus.Active -> colors.paletteColors.color6
            YamalProgressStatus.Success -> colors.functionalColors.success
            YamalProgressStatus.Exception -> colors.functionalColors.error
        }

    val trackColor = trailColor ?: colors.neutralColors.fillSecondary

    val animatedPercent by animateFloatAsState(
        targetValue = percent.coerceIn(0f, 100f),
        animationSpec = tween(300),
    )

    when (type) {
        YamalProgressType.Line -> {
            YamalLineProgress(
                percent = animatedPercent,
                status = effectiveStatus,
                showInfo = showInfo,
                progressColor = progressColor,
                trackColor = trackColor,
                size = size,
                format = format,
                modifier = modifier,
            )
        }

        YamalProgressType.Circle -> {
            YamalCircleProgress(
                percent = animatedPercent,
                status = effectiveStatus,
                showInfo = showInfo,
                progressColor = progressColor,
                trackColor = trackColor,
                strokeWidth = strokeWidth ?: 6.dp,
                size = size,
                format = format,
                startAngle = -90f,
                sweepAngle = 360f,
                modifier = modifier,
            )
        }

        YamalProgressType.Dashboard -> {
            YamalCircleProgress(
                percent = animatedPercent,
                status = effectiveStatus,
                showInfo = showInfo,
                progressColor = progressColor,
                trackColor = trackColor,
                strokeWidth = strokeWidth ?: 6.dp,
                size = size,
                format = format,
                startAngle = 135f,
                sweepAngle = 270f,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun YamalLineProgress(
    percent: Float,
    status: YamalProgressStatus,
    showInfo: Boolean,
    progressColor: Color,
    trackColor: Color,
    size: YamalProgressSize,
    format: ((Float) -> String)?,
    modifier: Modifier = Modifier,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val height =
        when (size) {
            YamalProgressSize.Small -> 6.dp
            YamalProgressSize.Default -> 8.dp
        }

    val textStyle =
        when (size) {
            YamalProgressSize.Small -> typography.footnote
            YamalProgressSize.Default -> typography.body
        }

    val iconSize =
        when (size) {
            YamalProgressSize.Small -> 12.dp
            YamalProgressSize.Default -> 14.dp
        }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(height)
                    .clip(RoundedCornerShape(height / 2))
                    .background(trackColor),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(percent / 100f)
                        .height(height)
                        .clip(RoundedCornerShape(height / 2))
                        .background(progressColor),
            )
        }

        if (showInfo) {
            Spacer(Modifier.width(8.dp))
            when (status) {
                YamalProgressStatus.Success -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        modifier = Modifier.size(iconSize),
                        tint = colors.functionalColors.success,
                    )
                }

                YamalProgressStatus.Exception -> {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exception",
                        modifier = Modifier.size(iconSize),
                        tint = colors.functionalColors.error,
                    )
                }

                else -> {
                    val text = format?.invoke(percent) ?: "${percent.toInt()}%"
                    Text(
                        text = text,
                        style = textStyle,
                        color = colors.neutralColors.secondaryText,
                    )
                }
            }
        }
    }
}

@Composable
private fun YamalCircleProgress(
    percent: Float,
    status: YamalProgressStatus,
    showInfo: Boolean,
    progressColor: Color,
    trackColor: Color,
    strokeWidth: Dp,
    size: YamalProgressSize,
    format: ((Float) -> String)?,
    startAngle: Float,
    sweepAngle: Float,
    modifier: Modifier = Modifier,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val circleSize =
        when (size) {
            YamalProgressSize.Small -> 80.dp
            YamalProgressSize.Default -> 120.dp
        }

    val textStyle =
        when (size) {
            YamalProgressSize.Small -> typography.body
            YamalProgressSize.Default -> typography.h4
        }

    val iconSize =
        when (size) {
            YamalProgressSize.Small -> 24.dp
            YamalProgressSize.Default -> 32.dp
        }

    Box(
        modifier = modifier.size(circleSize),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(circleSize)) {
            val stroke = strokeWidth.toPx()
            val diameter = min(this.size.width, this.size.height) - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)

            // Track
            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )

            // Progress
            val progressSweep = sweepAngle * (percent / 100f)
            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }

        if (showInfo) {
            when (status) {
                YamalProgressStatus.Success -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        modifier = Modifier.size(iconSize),
                        tint = colors.functionalColors.success,
                    )
                }

                YamalProgressStatus.Exception -> {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exception",
                        modifier = Modifier.size(iconSize),
                        tint = colors.functionalColors.error,
                    )
                }

                else -> {
                    val text = format?.invoke(percent) ?: "${percent.toInt()}%"
                    Text(
                        text = text,
                        style = textStyle,
                        color = colors.neutralColors.primaryText,
                    )
                }
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalProgressLinePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier =
                    Modifier
                        .padding(Dimension.Spacing.md)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Line Progress", style = YamalTheme.typography.bodyMedium)
                YamalProgress(percent = 30f)
                YamalProgress(percent = 50f)
                YamalProgress(percent = 70f, status = YamalProgressStatus.Active)
                YamalProgress(percent = 100f, status = YamalProgressStatus.Success)
                YamalProgress(percent = 50f, status = YamalProgressStatus.Exception)
            }
        }
    }
}

@Preview
@Composable
private fun YamalProgressCirclePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Circle Progress", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
                    YamalProgress(percent = 75f, type = YamalProgressType.Circle)
                    YamalProgress(
                        percent = 100f,
                        type = YamalProgressType.Circle,
                        status = YamalProgressStatus.Success,
                    )
                    YamalProgress(
                        percent = 70f,
                        type = YamalProgressType.Circle,
                        status = YamalProgressStatus.Exception,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalProgressDashboardPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Dashboard Progress", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
                    YamalProgress(percent = 75f, type = YamalProgressType.Dashboard)
                    YamalProgress(
                        percent = 100f,
                        type = YamalProgressType.Dashboard,
                        status = YamalProgressStatus.Success,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalProgressSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Sizes", style = YamalTheme.typography.bodyMedium)
                YamalProgress(
                    percent = 50f,
                    size = YamalProgressSize.Small,
                    modifier = Modifier.fillMaxWidth(),
                )
                YamalProgress(
                    percent = 50f,
                    size = YamalProgressSize.Default,
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    YamalProgress(
                        percent = 75f,
                        type = YamalProgressType.Circle,
                        size = YamalProgressSize.Small,
                    )
                    YamalProgress(
                        percent = 75f,
                        type = YamalProgressType.Circle,
                        size = YamalProgressSize.Default,
                    )
                }
            }
        }
    }
}
