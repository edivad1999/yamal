package com.yamal.designSystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Empty image type following Ant Design guidelines.
 */
@Immutable
enum class YamalEmptyImage {
    Default,
    Simple,
}

/**
 * An empty state component following Ant Design guidelines.
 * Used to display a placeholder when no data is available.
 *
 * @param modifier Modifier for the empty component
 * @param image Type of empty image to display
 * @param description Description text to display
 * @param content Optional content to display below the description (e.g., a button)
 */
@Composable
fun YamalEmpty(
    modifier: Modifier = Modifier,
    image: YamalEmptyImage = YamalEmptyImage.Default,
    description: String = "No Data",
    content: @Composable (() -> Unit)? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    Column(
        modifier = modifier.padding(Dimension.Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (image) {
            YamalEmptyImage.Default -> YamalEmptyDefaultImage()
            YamalEmptyImage.Simple -> YamalEmptySimpleImage()
        }

        Spacer(Modifier.height(Dimension.Spacing.sm))

        Text(
            text = description,
            style = typography.body,
            color = colors.neutralColors.secondaryText,
        )

        if (content != null) {
            Spacer(Modifier.height(Dimension.Spacing.md))
            content()
        }
    }
}

/**
 * Default empty image - a more detailed illustration.
 */
@Composable
private fun YamalEmptyDefaultImage() {
    val colors = YamalTheme.colors
    val borderColor = colors.neutralColors.border
    val fillColor = colors.neutralColors.fillQuaternary
    val accentColor = colors.paletteColors.color3

    Canvas(modifier = Modifier.size(width = 120.dp, height = 100.dp)) {
        val width = size.width
        val height = size.height

        // Box/container shape
        val boxPath =
            Path().apply {
                moveTo(width * 0.2f, height * 0.3f)
                lineTo(width * 0.5f, height * 0.15f)
                lineTo(width * 0.8f, height * 0.3f)
                lineTo(width * 0.8f, height * 0.7f)
                lineTo(width * 0.5f, height * 0.85f)
                lineTo(width * 0.2f, height * 0.7f)
                close()
            }

        drawPath(
            path = boxPath,
            color = fillColor,
        )

        drawPath(
            path = boxPath,
            color = borderColor,
            style = Stroke(width = 2f),
        )

        // Inner divider line
        drawLine(
            color = borderColor,
            start = Offset(width * 0.5f, height * 0.15f),
            end = Offset(width * 0.5f, height * 0.5f),
            strokeWidth = 1.5f,
        )

        drawLine(
            color = borderColor,
            start = Offset(width * 0.2f, height * 0.3f),
            end = Offset(width * 0.5f, height * 0.5f),
            strokeWidth = 1.5f,
        )

        drawLine(
            color = borderColor,
            start = Offset(width * 0.8f, height * 0.3f),
            end = Offset(width * 0.5f, height * 0.5f),
            strokeWidth = 1.5f,
        )

        // Document icons inside
        drawRoundRect(
            color = accentColor,
            topLeft = Offset(width * 0.35f, height * 0.4f),
            size = Size(width * 0.12f, height * 0.15f),
            cornerRadius = CornerRadius(4f, 4f),
        )

        drawRoundRect(
            color = accentColor.copy(alpha = 0.7f),
            topLeft = Offset(width * 0.53f, height * 0.45f),
            size = Size(width * 0.12f, height * 0.15f),
            cornerRadius = CornerRadius(4f, 4f),
        )
    }
}

/**
 * Simple empty image - a minimal illustration.
 */
@Composable
private fun YamalEmptySimpleImage() {
    val colors = YamalTheme.colors
    val borderColor = colors.neutralColors.border
    val fillColor = colors.neutralColors.fillTertiary

    Canvas(modifier = Modifier.size(width = 64.dp, height = 40.dp)) {
        val width = size.width
        val height = size.height

        // Simple box shape
        val boxPath =
            Path().apply {
                moveTo(width * 0.1f, height * 0.4f)
                lineTo(width * 0.5f, height * 0.2f)
                lineTo(width * 0.9f, height * 0.4f)
                lineTo(width * 0.9f, height * 0.8f)
                lineTo(width * 0.5f, height * 1f)
                lineTo(width * 0.1f, height * 0.8f)
                close()
            }

        drawPath(
            path = boxPath,
            color = fillColor,
        )

        drawPath(
            path = boxPath,
            color = borderColor,
            style = Stroke(width = 1.5f),
        )

        // Inner line
        drawLine(
            color = borderColor,
            start = Offset(width * 0.1f, height * 0.4f),
            end = Offset(width * 0.5f, height * 0.6f),
            strokeWidth = 1f,
        )

        drawLine(
            color = borderColor,
            start = Offset(width * 0.9f, height * 0.4f),
            end = Offset(width * 0.5f, height * 0.6f),
            strokeWidth = 1f,
        )

        drawLine(
            color = borderColor,
            start = Offset(width * 0.5f, height * 0.6f),
            end = Offset(width * 0.5f, height * 1f),
            strokeWidth = 1f,
        )
    }
}

/**
 * Convenience overload with custom image composable.
 */
@Composable
fun YamalEmpty(
    customImage: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: String = "No Data",
    content: @Composable (() -> Unit)? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    Column(
        modifier = modifier.padding(Dimension.Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        customImage()

        Spacer(Modifier.height(Dimension.Spacing.sm))

        Text(
            text = description,
            style = typography.body,
            color = colors.neutralColors.secondaryText,
        )

        if (content != null) {
            Spacer(Modifier.height(Dimension.Spacing.md))
            content()
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalEmptyDefaultPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Default Empty", style = YamalTheme.typography.bodyMedium)
                YamalEmpty()
            }
        }
    }
}

@Preview
@Composable
private fun YamalEmptySimplePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Simple Empty", style = YamalTheme.typography.bodyMedium)
                YamalEmpty(image = YamalEmptyImage.Simple)
            }
        }
    }
}

@Preview
@Composable
private fun YamalEmptyWithContentPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("With Content", style = YamalTheme.typography.bodyMedium)
                YamalEmpty(
                    description = "No items found",
                ) {
                    YamalButton(
                        text = "Create Now",
                        color = ButtonColor.Primary,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalEmptyCustomDescriptionPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(Dimension.Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            ) {
                YamalEmpty(
                    image = YamalEmptyImage.Simple,
                    description = "No results",
                )
                YamalEmpty(
                    image = YamalEmptyImage.Simple,
                    description = "Empty list",
                )
            }
        }
    }
}
