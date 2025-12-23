package com.yamal.designSystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Checkbox sizes following Ant Design guidelines.
 */
@Immutable
enum class YamalCheckboxSize {
    Small,
    Default,
    Large,
}

/**
 * A checkbox component following Ant Design guidelines.
 *
 * @param checked Whether the checkbox is checked
 * @param onCheckedChange Callback when the checkbox state changes
 * @param modifier Modifier for the checkbox
 * @param enabled Whether the checkbox is enabled
 * @param indeterminate Whether the checkbox is in indeterminate state (half-checked)
 * @param size Size of the checkbox
 * @param label Optional label text
 */
@Composable
fun YamalCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    indeterminate: Boolean = false,
    size: YamalCheckboxSize = YamalCheckboxSize.Default,
    label: String? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val checkboxSize =
        when (size) {
            YamalCheckboxSize.Small -> 14.dp
            YamalCheckboxSize.Default -> 16.dp
            YamalCheckboxSize.Large -> 20.dp
        }

    val iconSize =
        when (size) {
            YamalCheckboxSize.Small -> 10.dp
            YamalCheckboxSize.Default -> 12.dp
            YamalCheckboxSize.Large -> 14.dp
        }

    val textStyle =
        when (size) {
            YamalCheckboxSize.Small -> typography.footnote
            YamalCheckboxSize.Default -> typography.body
            YamalCheckboxSize.Large -> typography.body
        }

    val isCheckedOrIndeterminate = checked || indeterminate

    val backgroundColor by animateColorAsState(
        targetValue =
            when {
                !enabled && isCheckedOrIndeterminate -> colors.neutralColors.disableText
                !enabled -> colors.neutralColors.fillQuaternary
                isCheckedOrIndeterminate -> colors.paletteColors.color6
                else -> Color.Transparent
            },
        animationSpec = tween(150),
    )

    val borderColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> colors.neutralColors.disableText
                isCheckedOrIndeterminate -> colors.paletteColors.color6
                else -> colors.neutralColors.border
            },
        animationSpec = tween(150),
    )

    val contentColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> colors.neutralColors.background
                isCheckedOrIndeterminate -> colors.neutralColors.background
                else -> Color.Transparent
            },
        animationSpec = tween(150),
    )

    val labelColor =
        if (enabled) {
            colors.neutralColors.primaryText
        } else {
            colors.neutralColors.disableText
        }

    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable(
                    enabled = enabled,
                    onClick = { onCheckedChange(!checked) },
                    role = Role.Checkbox,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ).padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(checkboxSize)
                    .clip(RoundedCornerShape(2.dp))
                    .background(backgroundColor)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(2.dp),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (indeterminate) {
                // Indeterminate state - horizontal line
                Box(
                    modifier =
                        Modifier
                            .size(width = iconSize - 2.dp, height = 2.dp)
                            .background(contentColor),
                )
            } else if (checked) {
                // Checked state - checkmark
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = contentColor,
                )
            }
        }

        if (label != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                style = textStyle,
                color = labelColor,
            )
        }
    }
}

/**
 * A group of checkboxes for multiple selection.
 *
 * @param options List of options to display
 * @param selectedValues Currently selected values
 * @param onSelectionChange Callback when selection changes
 * @param modifier Modifier for the group
 * @param enabled Whether the group is enabled
 * @param size Size of the checkboxes
 * @param direction Layout direction of the group
 */
@Composable
fun YamalCheckboxGroup(
    options: List<YamalCheckboxOption>,
    selectedValues: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: YamalCheckboxSize = YamalCheckboxSize.Default,
    direction: YamalSpaceDirection = YamalSpaceDirection.Horizontal,
) {
    val spacing =
        when (size) {
            YamalCheckboxSize.Small -> Dimension.Spacing.xs
            YamalCheckboxSize.Default -> Dimension.Spacing.sm
            YamalCheckboxSize.Large -> Dimension.Spacing.md
        }

    if (direction == YamalSpaceDirection.Horizontal) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(spacing),
        ) {
            options.forEach { option ->
                YamalCheckbox(
                    checked = selectedValues.contains(option.value),
                    onCheckedChange = { isChecked ->
                        val newSelection =
                            if (isChecked) {
                                selectedValues + option.value
                            } else {
                                selectedValues - option.value
                            }
                        onSelectionChange(newSelection)
                    },
                    enabled = enabled && !option.disabled,
                    size = size,
                    label = option.label,
                )
            }
        }
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            options.forEach { option ->
                YamalCheckbox(
                    checked = selectedValues.contains(option.value),
                    onCheckedChange = { isChecked ->
                        val newSelection =
                            if (isChecked) {
                                selectedValues + option.value
                            } else {
                                selectedValues - option.value
                            }
                        onSelectionChange(newSelection)
                    },
                    enabled = enabled && !option.disabled,
                    size = size,
                    label = option.label,
                )
            }
        }
    }
}

/**
 * Option for checkbox group.
 */
@Immutable
data class YamalCheckboxOption(
    val value: String,
    val label: String,
    val disabled: Boolean = false,
)

// Previews

@Preview
@Composable
private fun YamalCheckboxBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                var checked1 by remember { mutableStateOf(false) }
                var checked2 by remember { mutableStateOf(true) }

                Text("Basic", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
                    YamalCheckbox(
                        checked = checked1,
                        onCheckedChange = { checked1 = it },
                        label = "Unchecked",
                    )
                    YamalCheckbox(
                        checked = checked2,
                        onCheckedChange = { checked2 = it },
                        label = "Checked",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCheckboxStatesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text("States", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
                    YamalCheckbox(
                        checked = false,
                        onCheckedChange = {},
                        label = "Unchecked",
                    )
                    YamalCheckbox(
                        checked = true,
                        onCheckedChange = {},
                        label = "Checked",
                    )
                    YamalCheckbox(
                        checked = false,
                        onCheckedChange = {},
                        indeterminate = true,
                        label = "Indeterminate",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCheckboxDisabledPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text("Disabled", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
                    YamalCheckbox(
                        checked = false,
                        onCheckedChange = {},
                        enabled = false,
                        label = "Disabled",
                    )
                    YamalCheckbox(
                        checked = true,
                        onCheckedChange = {},
                        enabled = false,
                        label = "Disabled Checked",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCheckboxSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text("Sizes", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    YamalCheckbox(
                        checked = true,
                        onCheckedChange = {},
                        size = YamalCheckboxSize.Small,
                        label = "Small",
                    )
                    YamalCheckbox(
                        checked = true,
                        onCheckedChange = {},
                        size = YamalCheckboxSize.Default,
                        label = "Default",
                    )
                    YamalCheckbox(
                        checked = true,
                        onCheckedChange = {},
                        size = YamalCheckboxSize.Large,
                        label = "Large",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCheckboxGroupPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var selected by remember { mutableStateOf(setOf("apple")) }

                Text("Checkbox Group", style = YamalTheme.typography.bodyMedium)
                YamalCheckboxGroup(
                    options =
                        listOf(
                            YamalCheckboxOption("apple", "Apple"),
                            YamalCheckboxOption("pear", "Pear"),
                            YamalCheckboxOption("orange", "Orange"),
                            YamalCheckboxOption("banana", "Banana", disabled = true),
                        ),
                    selectedValues = selected,
                    onSelectionChange = { selected = it },
                )

                Text("Vertical Group", style = YamalTheme.typography.bodyMedium)
                YamalCheckboxGroup(
                    options =
                        listOf(
                            YamalCheckboxOption("a", "Option A"),
                            YamalCheckboxOption("b", "Option B"),
                            YamalCheckboxOption("c", "Option C"),
                        ),
                    selectedValues = selected,
                    onSelectionChange = { selected = it },
                    direction = YamalSpaceDirection.Vertical,
                )
            }
        }
    }
}
