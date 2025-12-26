package com.yamal.designSystem.components.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.preview.PlatformPreviewContextConfigurationEffect
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Divider direction following Ant Design Mobile guidelines.
 */
@Immutable
enum class DividerDirection {
    /** Horizontal divider line */
    Horizontal,

    /** Vertical divider line */
    Vertical,
}

/**
 * Content position for horizontal dividers following Ant Design Mobile guidelines.
 * Only applies when [DividerDirection] is [DividerDirection.Horizontal].
 */
@Immutable
enum class DividerContentPosition {
    /** Content aligned to the left */
    Left,

    /** Content aligned to the center (default) */
    Center,

    /** Content aligned to the right */
    Right,
}

/**
 * A dividing line that separates content, following Ant Design Mobile guidelines.
 *
 * Use cases:
 * - Separate text paragraphs across different chapters
 * - Split inline elements like table action columns
 *
 * Example usage:
 * ```
 * // Simple horizontal divider
 * YamalDivider()
 *
 * // Divider with centered text
 * YamalDivider { Text("Section") }
 *
 * // Divider with left-aligned content
 * YamalDivider(contentPosition = DividerContentPosition.Left) {
 *     Text("Chapter 1")
 * }
 *
 * // Vertical divider between inline elements
 * Row {
 *     Text("Edit")
 *     YamalDivider(direction = DividerDirection.Vertical)
 *     Text("Delete")
 * }
 * ```
 *
 * @param modifier The [Modifier] to be applied to this divider.
 * @param direction The divider orientation. Defaults to [DividerDirection.Horizontal].
 * @param contentPosition Where content displays (only applies when direction is horizontal).
 *   Defaults to [DividerContentPosition.Center].
 * @param colors [DividerColors] that will be used to resolve the colors used for this divider.
 *   See [DividerDefaults.colors].
 * @param content Optional content to display in the divider (only for horizontal dividers).
 */
@Composable
fun YamalDivider(
    modifier: Modifier = Modifier,
    direction: DividerDirection = DividerDirection.Horizontal,
    contentPosition: DividerContentPosition = DividerContentPosition.Center,
    colors: DividerColors = DividerDefaults.colors(),
    content: @Composable (() -> Unit)? = null,
) {
    when (direction) {
        DividerDirection.Horizontal -> {
            HorizontalDivider(
                modifier = modifier,
                contentPosition = contentPosition,
                colors = colors,
                content = content,
            )
        }

        DividerDirection.Vertical -> {
            VerticalDivider(
                modifier = modifier,
                color = colors.lineColor,
            )
        }
    }
}

@Composable
private fun HorizontalDivider(
    modifier: Modifier,
    contentPosition: DividerContentPosition,
    colors: DividerColors,
    content: @Composable (() -> Unit)?,
) {
    if (content != null) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(vertical = DividerDefaults.VerticalMargin),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val (startWeight, endWeight) =
                when (contentPosition) {
                    DividerContentPosition.Left -> 0.1f to 1f
                    DividerContentPosition.Center -> 1f to 1f
                    DividerContentPosition.Right -> 1f to 0.1f
                }

            DividerLine(
                modifier = Modifier.weight(startWeight),
                color = colors.lineColor,
            )

            Box(
                modifier = Modifier.padding(horizontal = DividerDefaults.ContentPadding),
            ) {
                ProvideTextStyle(value = YamalTheme.typography.body) {
                    CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
                        content()
                    }
                }
            }

            DividerLine(
                modifier = Modifier.weight(endWeight),
                color = colors.lineColor,
            )
        }
    } else {
        Divider(
            modifier = modifier.padding(vertical = DividerDefaults.VerticalMargin),
            color = colors.lineColor,
            thickness = DividerDefaults.Thickness,
        )
    }
}

@Composable
private fun VerticalDivider(
    modifier: Modifier,
    color: Color,
) {
    // Height of 0.9em relative to current font size (14sp default)
    val density = LocalDensity.current
    val height = with(density) { (14.sp * 0.9f).toDp() }

    Box(
        modifier =
            modifier
                .padding(horizontal = DividerDefaults.HorizontalMargin)
                .height(height)
                .width(DividerDefaults.Thickness),
        contentAlignment = Alignment.Center,
    ) {
        Divider(
            modifier =
                Modifier
                    .width(DividerDefaults.Thickness)
                    .height(height),
            color = color,
        )
    }
}

@Composable
private fun DividerLine(
    modifier: Modifier = Modifier,
    color: Color,
    thickness: Dp = DividerDefaults.Thickness,
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
    )
}

/**
 * Represents the colors of the divider elements.
 *
 * @param lineColor The color of the divider line.
 * @param contentColor The color to use for content displayed in the divider.
 */
@Immutable
class DividerColors(
    val lineColor: Color,
    val contentColor: Color,
) {
    /**
     * Returns a copy of this DividerColors, optionally overriding some of the values.
     */
    fun copy(
        lineColor: Color = this.lineColor,
        contentColor: Color = this.contentColor,
    ) = DividerColors(
        lineColor = lineColor,
        contentColor = contentColor,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is DividerColors) return false

        if (lineColor != other.lineColor) return false
        if (contentColor != other.contentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lineColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        return result
    }
}

/**
 * Contains default values used for [YamalDivider] following Ant Design Mobile specs.
 */
object DividerDefaults {
    /** Default thickness: 1dp */
    val Thickness: Dp = 1.dp

    /** Vertical margin for horizontal dividers: 16dp */
    val VerticalMargin: Dp = 16.dp

    /** Horizontal margin for vertical dividers: 16dp */
    val HorizontalMargin: Dp = 16.dp

    /** Horizontal padding around content: 16dp */
    val ContentPadding: Dp = 16.dp

    /**
     * Creates a [DividerColors] with the default colors.
     */
    @Composable
    fun colors(): DividerColors =
        DividerColors(
            lineColor = YamalTheme.colors.neutralColors.divider,
            contentColor = YamalTheme.colors.neutralColors.secondaryText,
        )

    /**
     * Creates a [DividerColors] with custom colors.
     *
     * @param lineColor The color of the divider line.
     * @param contentColor The color to use for content displayed in the divider.
     */
    @Composable
    fun colors(
        lineColor: Color = YamalTheme.colors.neutralColors.divider,
        contentColor: Color = YamalTheme.colors.neutralColors.secondaryText,
    ): DividerColors =
        DividerColors(
            lineColor = lineColor,
            contentColor = contentColor,
        )
}

// Previews

@Preview
@Composable
private fun YamalDividerHorizontalPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Content above")
                YamalDivider()
                Text("Content below")
            }
        }
    }
}

@Preview
@Composable
private fun YamalDividerWithContentPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YamalDivider { Text("Center (default)") }

                YamalDivider(contentPosition = DividerContentPosition.Left) {
                    Text("Left")
                }

                YamalDivider(contentPosition = DividerContentPosition.Right) {
                    Text("Right")
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalDividerVerticalPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Edit")
                YamalDivider(direction = DividerDirection.Vertical)
                Text("Delete")
                YamalDivider(direction = DividerDirection.Vertical)
                Text("More")
            }
        }
    }
}

@Preview
@Composable
private fun YamalDividerCustomColorsPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val customColors =
                    DividerDefaults.colors(
                        lineColor = YamalTheme.colors.paletteColors.color6,
                        contentColor = YamalTheme.colors.paletteColors.color6,
                    )

                YamalDivider(colors = customColors) {
                    Text("Custom Colors")
                }
            }
        }
    }
}
