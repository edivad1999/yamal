package com.yamal.designSystem.components.tag

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Fill mode for [Tag] component.
 */
enum class TagFill {
    /** Solid fill: white text on colored background */
    Solid,

    /** Outline fill: colored text on transparent background with colored border */
    Outline,
}

/**
 * Preset color for [Tag] component following Ant Design Mobile specifications.
 *
 * Each preset maps to specific colors for text, background, and border
 * depending on the [TagFill] mode.
 */
@Immutable
enum class TagColor {
    /** Default gray color */
    Default,

    /** Primary brand color (blue) */
    Primary,

    /** Success color (green) */
    Success,

    /** Warning color (orange) */
    Warning,

    /** Danger/error color (red) */
    Danger,
}

/**
 * Default values for [Tag] component following Ant Design Mobile specifications.
 *
 * CSS Variables mapped:
 * - Padding: 2px 4px
 * - Font size: --adm-font-size-3 (12sp)
 * - Border radius: 2px (--adm-tag-border-radius)
 * - Border: 1px solid
 * - Line height: 1
 *
 * @see <a href="https://mobile.ant.design/components/tag">Ant Design Mobile Tag</a>
 */
object TagDefaults {
    /** Horizontal padding: 4dp (from CSS padding: 2px 4px) */
    val HorizontalPadding: Dp = 4.dp

    /** Vertical padding: 2dp (from CSS padding: 2px 4px) */
    val VerticalPadding: Dp = 2.dp

    /** Content padding combining horizontal and vertical */
    val ContentPadding: PaddingValues =
        PaddingValues(
            horizontal = HorizontalPadding,
            vertical = VerticalPadding,
        )

    /** Font size: 11sp (ADM: font-size-3) */
    val FontSize = 11.sp

    /** Border radius: 2dp (--adm-tag-border-radius default) */
    val BorderRadius: Dp = 2.dp

    /** Round border radius: 100dp (effectively pill shape) */
    val RoundBorderRadius: Dp = 100.dp

    /** Border width: 1dp */
    val BorderWidth: Dp = 1.dp

    /** Shape for default tag */
    val shape: Shape = RoundedCornerShape(BorderRadius)

    /** Shape for round tag */
    val roundShape: Shape = RoundedCornerShape(RoundBorderRadius)

    // Color preset values

    /** Default color - secondary text gray */
    val defaultColor: Color
        @Composable get() = YamalTheme.colors.textSecondary

    /** Primary color - brand blue (#1677ff) */
    val primaryColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Success color - green (#00b578) */
    val successColor: Color
        @Composable get() = YamalTheme.colors.success

    /** Warning color - orange (#ff8f1f) */
    val warningColor: Color
        @Composable get() = YamalTheme.colors.warning

    /** Danger color - red (#ff3141) */
    val dangerColor: Color
        @Composable get() = YamalTheme.colors.danger

    /** Solid fill text color - --adm-color-text-light-solid */
    val solidTextColor: Color
        @Composable get() = YamalTheme.colors.textLightSolid

    /**
     * Get the theme color for a preset.
     */
    @Composable
    fun getPresetColor(color: TagColor): Color =
        when (color) {
            TagColor.Default -> defaultColor
            TagColor.Primary -> primaryColor
            TagColor.Success -> successColor
            TagColor.Warning -> warningColor
            TagColor.Danger -> dangerColor
        }
}

/**
 * Tag component following Ant Design Mobile specifications exactly.
 *
 * Tags are used to mark or classify items.
 *
 * @param modifier Modifier for the tag
 * @param color Preset color for the tag. Use [TagColor] enum for predefined colors.
 * @param fill Fill mode: [TagFill.Solid] for filled background, [TagFill.Outline] for border only.
 * @param round Whether to use rounded (pill) corners.
 * @param content The content to display inside the tag.
 *
 * Example usage:
 * ```
 * // Basic tag
 * Tag { Text("Tag") }
 *
 * // Primary colored tag
 * Tag(color = TagColor.Primary) { Text("Primary") }
 *
 * // Outline tag
 * Tag(color = TagColor.Success, fill = TagFill.Outline) { Text("Success") }
 *
 * // Round tag
 * Tag(color = TagColor.Warning, round = true) { Text("Warning") }
 *
 * // Custom color tag
 * Tag(customColor = Color(0xFF722ED1)) { Text("Custom") }
 * ```
 */
@Composable
fun Tag(
    modifier: Modifier = Modifier,
    color: TagColor = TagColor.Default,
    fill: TagFill = TagFill.Solid,
    round: Boolean = false,
    content: @Composable () -> Unit,
) {
    val themeColor = TagDefaults.getPresetColor(color)
    TagImpl(
        modifier = modifier,
        themeColor = themeColor,
        fill = fill,
        round = round,
        content = content,
    )
}

/**
 * Tag component with custom color following Ant Design Mobile specifications.
 *
 * @param customColor Custom color for the tag (overrides preset colors).
 * @param modifier Modifier for the tag.
 * @param fill Fill mode: [TagFill.Solid] for filled background, [TagFill.Outline] for border only.
 * @param round Whether to use rounded (pill) corners.
 * @param content The content to display inside the tag.
 */
@Composable
fun Tag(
    customColor: Color,
    modifier: Modifier = Modifier,
    fill: TagFill = TagFill.Solid,
    round: Boolean = false,
    content: @Composable () -> Unit,
) {
    TagImpl(
        modifier = modifier,
        themeColor = customColor,
        fill = fill,
        round = round,
        content = content,
    )
}

/**
 * Convenience overload for [Tag] with string content.
 *
 * @param text The text to display in the tag.
 * @param modifier Modifier for the tag.
 * @param color Preset color for the tag.
 * @param fill Fill mode.
 * @param round Whether to use rounded corners.
 */
@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    color: TagColor = TagColor.Default,
    fill: TagFill = TagFill.Solid,
    round: Boolean = false,
) {
    Tag(
        modifier = modifier,
        color = color,
        fill = fill,
        round = round,
    ) {
        Text(text = text)
    }
}

/**
 * Convenience overload for [Tag] with string content and custom color.
 *
 * @param text The text to display in the tag.
 * @param customColor Custom color for the tag.
 * @param modifier Modifier for the tag.
 * @param fill Fill mode.
 * @param round Whether to use rounded corners.
 */
@Composable
fun Tag(
    text: String,
    customColor: Color,
    modifier: Modifier = Modifier,
    fill: TagFill = TagFill.Solid,
    round: Boolean = false,
) {
    Tag(
        customColor = customColor,
        modifier = modifier,
        fill = fill,
        round = round,
    ) {
        Text(text = text)
    }
}

/**
 * Internal implementation of Tag component.
 */
@Composable
private fun TagImpl(
    modifier: Modifier,
    themeColor: Color,
    fill: TagFill,
    round: Boolean,
    content: @Composable () -> Unit,
) {
    val shape = if (round) TagDefaults.roundShape else TagDefaults.shape

    val backgroundColor =
        when (fill) {
            TagFill.Solid -> themeColor
            TagFill.Outline -> Color.Transparent
        }

    val textColor =
        when (fill) {
            TagFill.Solid -> TagDefaults.solidTextColor
            TagFill.Outline -> themeColor
        }

    val border =
        when (fill) {
            TagFill.Solid -> null
            TagFill.Outline -> BorderStroke(TagDefaults.BorderWidth, themeColor)
        }

    val textStyle =
        TextStyle(
            fontSize = TagDefaults.FontSize,
            color = textColor,
            lineHeight = TagDefaults.FontSize, // line-height: 1 means same as font size
        )

    Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        border = border,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides textColor,
            LocalTextStyle provides textStyle,
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.padding(TagDefaults.ContentPadding),
            ) {
                content()
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun TagColorPresetsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Color Presets (Solid)",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(text = "Default", color = TagColor.Default)
                    Tag(text = "Primary", color = TagColor.Primary)
                    Tag(text = "Success", color = TagColor.Success)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(text = "Warning", color = TagColor.Warning)
                    Tag(text = "Danger", color = TagColor.Danger)
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagOutlinePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Color Presets (Outline)",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(text = "Default", color = TagColor.Default, fill = TagFill.Outline)
                    Tag(text = "Primary", color = TagColor.Primary, fill = TagFill.Outline)
                    Tag(text = "Success", color = TagColor.Success, fill = TagFill.Outline)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(text = "Warning", color = TagColor.Warning, fill = TagFill.Outline)
                    Tag(text = "Danger", color = TagColor.Danger, fill = TagFill.Outline)
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagRoundPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Round Tags",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(text = "Solid", color = TagColor.Primary, round = true)
                    Tag(
                        text = "Outline",
                        color = TagColor.Primary,
                        fill = TagFill.Outline,
                        round = true,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagCustomColorPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Custom Colors",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(text = "Purple", customColor = Color(0xFF722ED1))
                    Tag(text = "Cyan", customColor = Color(0xFF13C2C2))
                    Tag(text = "Magenta", customColor = Color(0xFFEB2F96))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(
                        text = "Purple",
                        customColor = Color(0xFF722ED1),
                        fill = TagFill.Outline,
                    )
                    Tag(
                        text = "Cyan",
                        customColor = Color(0xFF13C2C2),
                        fill = TagFill.Outline,
                    )
                    Tag(
                        text = "Magenta",
                        customColor = Color(0xFFEB2F96),
                        fill = TagFill.Outline,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagComposableContentPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Composable Content",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(color = TagColor.Primary) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("NEW")
                        }
                    }
                    Tag(color = TagColor.Success, round = true) {
                        Text("Active")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagAllVariantsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "All Variants",
                    style = YamalTheme.typography.bodyMedium,
                )

                // All presets in solid mode
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TagColor.entries.forEach { color ->
                        Tag(text = color.name, color = color)
                    }
                }

                // All presets in outline mode
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TagColor.entries.forEach { color ->
                        Tag(text = color.name, color = color, fill = TagFill.Outline)
                    }
                }

                // Round variants
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Tag(text = "Solid Round", color = TagColor.Primary, round = true)
                    Tag(
                        text = "Outline Round",
                        color = TagColor.Primary,
                        fill = TagFill.Outline,
                        round = true,
                    )
                }
            }
        }
    }
}
