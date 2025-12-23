package com.yamal.designSystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preset tag colors following Ant Design guidelines.
 */
@Immutable
enum class YamalTagPreset {
    Default,
    Success,
    Processing,
    Error,
    Warning,
}

/**
 * A tag component following Ant Design guidelines.
 *
 * @param text Tag text content
 * @param modifier Modifier for the tag
 * @param preset Preset color scheme
 * @param color Custom color (overrides preset)
 * @param bordered Whether to show border
 * @param closable Whether the tag can be closed
 * @param onClose Callback when close button is clicked
 * @param icon Optional leading icon
 */
@Composable
fun YamalTag(
    text: String,
    modifier: Modifier = Modifier,
    preset: YamalTagPreset = YamalTagPreset.Default,
    color: Color? = null,
    bordered: Boolean = true,
    closable: Boolean = false,
    onClose: (() -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val tagColor =
        color ?: when (preset) {
            YamalTagPreset.Default -> colors.neutralColors.primaryText
            YamalTagPreset.Success -> colors.functionalColors.success
            YamalTagPreset.Processing -> colors.paletteColors.color6
            YamalTagPreset.Error -> colors.functionalColors.error
            YamalTagPreset.Warning -> colors.functionalColors.warning
        }

    val backgroundColor =
        when {
            color != null -> color.copy(alpha = 0.1f)
            preset == YamalTagPreset.Default -> colors.neutralColors.tableHeader
            else -> tagColor.copy(alpha = 0.1f)
        }

    val borderColor = if (bordered) tagColor.copy(alpha = 0.3f) else Color.Transparent

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimension.BorderRadius.sm),
        color = backgroundColor,
        border = if (bordered) BorderStroke(1.dp, borderColor) else null,
    ) {
        Row(
            modifier =
                Modifier.padding(
                    horizontal = Dimension.Spacing.xs,
                    vertical = Dimension.Spacing.xxs,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xxs),
        ) {
            icon?.invoke()

            Text(
                text = text,
                style = typography.footnote,
                color = tagColor,
            )

            if (closable && onClose != null) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(12.dp),
                        tint = tagColor,
                    )
                }
            }
        }
    }
}

/**
 * A checkable tag component following Ant Design Tag.CheckableTag.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun YamalCheckableTag(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val backgroundColor =
        if (checked) {
            colors.paletteColors.color6
        } else {
            colors.neutralColors.background
        }

    val textColor =
        if (checked) {
            colors.neutralColors.background
        } else {
            colors.neutralColors.primaryText
        }

    Surface(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        shape = RoundedCornerShape(Dimension.BorderRadius.sm),
        color = backgroundColor,
        border = BorderStroke(1.dp, if (checked) colors.paletteColors.color6 else colors.neutralColors.border),
    ) {
        Text(
            text = text,
            style = typography.footnote,
            color = textColor,
            modifier =
                Modifier.padding(
                    horizontal = Dimension.Spacing.xs,
                    vertical = Dimension.Spacing.xxs,
                ),
        )
    }
}

// Previews

@Preview
@Composable
private fun YamalTagPresetsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    YamalTag(text = "Default")
                    YamalTag(text = "Success", preset = YamalTagPreset.Success)
                    YamalTag(text = "Processing", preset = YamalTagPreset.Processing)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    YamalTag(text = "Error", preset = YamalTagPreset.Error)
                    YamalTag(text = "Warning", preset = YamalTagPreset.Warning)
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalTagVariantsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YamalTag(text = "Bordered", bordered = true)
                YamalTag(text = "Borderless", bordered = false)
                YamalTag(
                    text = "Closable",
                    closable = true,
                    onClose = {},
                )
                YamalTag(
                    text = "Custom Color",
                    color = Color(0xFF722ED1),
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalCheckableTagPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YamalCheckableTag(
                    text = "Checked",
                    checked = true,
                    onCheckedChange = {},
                )
                YamalCheckableTag(
                    text = "Unchecked",
                    checked = false,
                    onCheckedChange = {},
                )
            }
        }
    }
}
