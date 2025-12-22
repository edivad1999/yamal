package com.yamal.designSystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Divider type following Ant Design guidelines.
 */
@Immutable
enum class YamalDividerType {
    Horizontal,
    Vertical,
}

/**
 * Divider orientation for text placement following Ant Design guidelines.
 */
@Immutable
enum class YamalDividerOrientation {
    Start,
    Center,
    End,
}

/**
 * A divider component following Ant Design guidelines.
 *
 * @param modifier Modifier for the divider
 * @param type Divider type (Horizontal or Vertical)
 * @param dashed Whether to use dashed line style
 * @param orientation Text placement orientation (Start, Center, End)
 * @param plain Whether text should be plain style (no bold)
 * @param text Optional text to display in divider
 */
@Composable
fun YamalDivider(
    modifier: Modifier = Modifier,
    type: YamalDividerType = YamalDividerType.Horizontal,
    dashed: Boolean = false,
    orientation: YamalDividerOrientation = YamalDividerOrientation.Center,
    plain: Boolean = false,
    text: String? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography
    val dividerColor = colors.neutralColors.divider

    when (type) {
        YamalDividerType.Horizontal -> {
            if (text != null) {
                Row(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val startWeight =
                        when (orientation) {
                            YamalDividerOrientation.Start -> 0.05f
                            YamalDividerOrientation.Center -> 1f
                            YamalDividerOrientation.End -> 1f
                        }
                    val endWeight =
                        when (orientation) {
                            YamalDividerOrientation.Start -> 1f
                            YamalDividerOrientation.Center -> 1f
                            YamalDividerOrientation.End -> 0.05f
                        }

                    DividerLine(
                        modifier = Modifier.weight(startWeight),
                        dashed = dashed,
                    )

                    Text(
                        text = text,
                        style = if (plain) typography.body else typography.bodyMedium,
                        color = colors.neutralColors.title,
                        modifier = Modifier.padding(horizontal = Dimension.Spacing.md),
                    )

                    DividerLine(
                        modifier = Modifier.weight(endWeight),
                        dashed = dashed,
                    )
                }
            } else {
                if (dashed) {
                    DividerLine(
                        modifier =
                            modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimension.Spacing.md),
                        dashed = true,
                    )
                } else {
                    Divider(
                        modifier = modifier.padding(vertical = Dimension.Spacing.md),
                        color = dividerColor,
                        thickness = 1.dp,
                    )
                }
            }
        }

        YamalDividerType.Vertical -> {
            if (dashed) {
                VerticalDashedDivider(
                    modifier =
                        modifier
                            .fillMaxHeight()
                            .padding(horizontal = Dimension.Spacing.xs),
                )
            } else {
                Box(
                    modifier =
                        modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .padding(horizontal = Dimension.Spacing.xs),
                ) {
                    Divider(
                        modifier = Modifier.fillMaxHeight().width(1.dp),
                        color = dividerColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun DividerLine(
    modifier: Modifier = Modifier,
    dashed: Boolean = false,
    thickness: Dp = 1.dp,
) {
    val colors = YamalTheme.colors
    val dividerColor = colors.neutralColors.divider

    if (dashed) {
        Canvas(
            modifier = modifier.height(thickness),
        ) {
            drawLine(
                color = dividerColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = thickness.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f),
            )
        }
    } else {
        Divider(
            modifier = modifier,
            color = dividerColor,
            thickness = thickness,
        )
    }
}

@Composable
private fun VerticalDashedDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) {
    val colors = YamalTheme.colors
    val dividerColor = colors.neutralColors.divider

    Canvas(
        modifier = modifier.width(thickness),
    ) {
        drawLine(
            color = dividerColor,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            strokeWidth = thickness.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f),
        )
    }
}

// Previews

@Preview
@Composable
private fun YamalDividerHorizontalPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Content above")
                YamalDivider()
                Text("Content below")

                YamalDivider(dashed = true)
                Text("After dashed divider")
            }
        }
    }
}

@Preview
@Composable
private fun YamalDividerWithTextPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YamalDivider(text = "Center (default)")

                YamalDivider(
                    text = "Left",
                    orientation = YamalDividerOrientation.Start,
                )

                YamalDivider(
                    text = "Right",
                    orientation = YamalDividerOrientation.End,
                )

                YamalDivider(
                    text = "Dashed",
                    dashed = true,
                )

                YamalDivider(
                    text = "Plain text",
                    plain = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalDividerVerticalPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Left")
                YamalDivider(type = YamalDividerType.Vertical)
                Text("Middle")
                YamalDivider(type = YamalDividerType.Vertical, dashed = true)
                Text("Right")
            }
        }
    }
}
