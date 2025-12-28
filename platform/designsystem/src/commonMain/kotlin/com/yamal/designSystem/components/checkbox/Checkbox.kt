package com.yamal.designSystem.components.checkbox

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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.ProvideTextStyle
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [Checkbox] component following Ant Design Mobile specifications.
 */
object CheckboxDefaults {
    /** Icon size: 22dp */
    val IconSize: Dp = 22.dp

    /** Font size: 17sp (adm-font-size-9) */
    val FontSize = 17.sp

    /** Gap between icon and content: 8dp */
    val Gap: Dp = 8.dp

    /** Border width */
    val BorderWidth: Dp = 1.dp

    /** Disabled opacity: 0.4 */
    const val DISABLED_ALPHA = 0.4f

    /** Primary color - checked state border and background */
    val primaryColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Light color - unchecked border, disabled states - --adm-color-light */
    val lightColor: Color
        @Composable get() = YamalTheme.colors.light

    /** Icon content color (checkmark on checked) - --adm-color-text-light-solid */
    val iconColor: Color
        @Composable get() = YamalTheme.colors.textLightSolid

    /** Background color - indeterminate state background - --adm-color-background */
    val backgroundColor: Color
        @Composable get() = YamalTheme.colors.background

    /** Fill content color - disabled icon background - --adm-color-fill-content */
    val fillContentColor: Color
        @Composable get() = YamalTheme.colors.fillContent

    /** Text color */
    val contentColor: Color
        @Composable get() = YamalTheme.colors.text
}

/**
 * Checkbox component following Ant Design Mobile specifications exactly.
 *
 * Used for making multiple selections from a set of options.
 *
 * @param checked Whether the checkbox is currently checked
 * @param onCheckedChange Callback when the checkbox state changes
 * @param modifier Modifier for the checkbox
 * @param indeterminate Whether to show indeterminate state (partial selection).
 *   Note: indeterminate only affects visual appearance, not the checked value.
 * @param enabled Whether the checkbox is enabled (opposite of disabled)
 * @param block Whether to render as block-level element (full width)
 * @param icon Custom icon renderer. Receives (checked, indeterminate) and returns composable.
 *   When null, uses default checkbox icon.
 * @param content Optional content (label) to display next to the checkbox
 *
 * Example usage:
 * ```
 * // Basic checkbox
 * Checkbox(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it }
 * )
 *
 * // With label
 * Checkbox(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it }
 * ) {
 *     Text("Remember me")
 * }
 *
 * // Indeterminate state (select all with partial selection)
 * Checkbox(
 *     checked = allSelected,
 *     onCheckedChange = { selectAll(it) },
 *     indeterminate = someSelected && !allSelected
 * ) {
 *     Text("Select all")
 * }
 *
 * // Block mode (full width)
 * Checkbox(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it },
 *     block = true
 * ) {
 *     Text("Accept terms and conditions")
 * }
 *
 * // Custom icon
 * Checkbox(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it },
 *     icon = { checked, indeterminate ->
 *         Icon(if (checked) Icons.Filled.Heart else Icons.Outlined.Heart)
 *     }
 * )
 * ```
 */
@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    indeterminate: Boolean = false,
    enabled: Boolean = true,
    block: Boolean = false,
    icon: (@Composable (checked: Boolean, indeterminate: Boolean) -> Unit)? = null,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val rowModifier =
        if (block) {
            modifier.fillMaxWidth()
        } else {
            modifier
        }

    Row(
        modifier =
            rowModifier
                .clip(CircleShape)
                .clickable(
                    enabled = enabled,
                    onClick = { onCheckedChange(!checked) },
                    role = Role.Checkbox,
                    interactionSource = interactionSource,
                    indication = null,
                ).alpha(if (enabled) 1f else CheckboxDefaults.DISABLED_ALPHA),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            icon(checked, indeterminate)
        } else {
            CheckboxIcon(
                checked = checked,
                indeterminate = indeterminate,
                enabled = enabled,
            )
        }

        if (content != null) {
            Spacer(Modifier.width(CheckboxDefaults.Gap))
            val textStyle =
                TextStyle(
                    fontSize = CheckboxDefaults.FontSize,
                    color = CheckboxDefaults.contentColor,
                )
            ProvideTextStyle(value = textStyle) {
                CompositionLocalProvider(LocalContentColor provides CheckboxDefaults.contentColor) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun CheckboxIcon(
    checked: Boolean,
    indeterminate: Boolean,
    enabled: Boolean,
) {
    // Determine colors based on state
    // Priority: indeterminate > checked > unchecked
    // Disabled affects all states

    val backgroundColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> CheckboxDefaults.fillContentColor
                indeterminate -> CheckboxDefaults.backgroundColor
                checked -> CheckboxDefaults.primaryColor
                else -> Color.Transparent
            },
        animationSpec = tween(150),
    )

    val borderColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> CheckboxDefaults.lightColor
                checked -> CheckboxDefaults.primaryColor
                else -> CheckboxDefaults.lightColor
            },
        animationSpec = tween(150),
    )

    val iconTint by animateColorAsState(
        targetValue =
            when {
                !enabled -> CheckboxDefaults.lightColor
                indeterminate -> CheckboxDefaults.primaryColor
                checked -> CheckboxDefaults.iconColor
                else -> Color.Transparent
            },
        animationSpec = tween(150),
    )

    Box(
        modifier =
            Modifier
                .size(CheckboxDefaults.IconSize)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(
                    width = CheckboxDefaults.BorderWidth,
                    color = borderColor,
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        when {
            indeterminate -> {
                // Indeterminate state - filled circle (55% of checkbox size)
                Box(
                    modifier =
                        Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(iconTint),
                )
            }

            checked -> {
                // Checked state - checkmark
                Icon(
                    icon = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = iconTint,
                )
            }
            // Unchecked - no icon
        }
    }
}

/**
 * Context for CheckboxGroup to manage shared state.
 */
internal data class CheckboxGroupContext(
    val selectedValues: Set<String>,
    val onValueChange: (String, Boolean) -> Unit,
    val disabled: Boolean,
)

internal val LocalCheckboxGroup = compositionLocalOf<CheckboxGroupContext?> { null }

/**
 * A group of checkboxes for multiple selection following Ant Design Mobile specifications.
 *
 * @param value Currently selected values
 * @param onValueChange Callback when selection changes
 * @param modifier Modifier for the group
 * @param disabled Whether all checkboxes in the group are disabled
 * @param content The checkboxes to display, typically using [CheckboxGroupItem]
 *
 * Example usage:
 * ```
 * var selected by remember { mutableStateOf(setOf("apple")) }
 *
 * CheckboxGroup(
 *     value = selected,
 *     onValueChange = { selected = it }
 * ) {
 *     CheckboxGroupItem(value = "apple") { Text("Apple") }
 *     CheckboxGroupItem(value = "orange") { Text("Orange") }
 *     CheckboxGroupItem(value = "banana") { Text("Banana") }
 * }
 * ```
 */
@Composable
fun CheckboxGroup(
    value: Set<String>,
    onValueChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context =
        CheckboxGroupContext(
            selectedValues = value,
            onValueChange = { itemValue, isChecked ->
                val newSelection =
                    if (isChecked) {
                        value + itemValue
                    } else {
                        value - itemValue
                    }
                onValueChange(newSelection)
            },
            disabled = disabled,
        )

    CompositionLocalProvider(LocalCheckboxGroup provides context) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            content()
        }
    }
}

/**
 * A checkbox item for use within [CheckboxGroup].
 *
 * @param value The value identifier for this item
 * @param modifier Modifier for the item
 * @param enabled Whether this specific item is enabled
 * @param content The label content
 */
@Composable
fun CheckboxGroupItem(
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val groupContext =
        LocalCheckboxGroup.current
            ?: error("CheckboxGroupItem must be used within a CheckboxGroup")

    val isChecked = groupContext.selectedValues.contains(value)
    val isEnabled = enabled && !groupContext.disabled

    Checkbox(
        checked = isChecked,
        onCheckedChange = { checked ->
            groupContext.onValueChange(value, checked)
        },
        modifier = modifier,
        enabled = isEnabled,
        content = content,
    )
}

// Previews

@Preview
@Composable
private fun CheckboxBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                var checked1 by remember { mutableStateOf(false) }
                var checked2 by remember { mutableStateOf(true) }

                com.yamal.designSystem.components.text.Text(
                    "Basic",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Checkbox(
                        checked = checked1,
                        onCheckedChange = { checked1 = it },
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Unchecked")
                    }
                    Checkbox(
                        checked = checked2,
                        onCheckedChange = { checked2 = it },
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Checked")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckboxStatesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "States",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Unchecked")
                    }
                    Checkbox(
                        checked = true,
                        onCheckedChange = {},
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Checked")
                    }
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        indeterminate = true,
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Indeterminate")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckboxDisabledPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Disabled",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        enabled = false,
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Disabled")
                    }
                    Checkbox(
                        checked = true,
                        onCheckedChange = {},
                        enabled = false,
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Disabled Checked")
                    }
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        enabled = false,
                        indeterminate = true,
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Disabled Indeterminate")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckboxBlockPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Block Mode",
                    style = YamalTheme.typography.bodyMedium,
                )
                Checkbox(
                    checked = true,
                    onCheckedChange = {},
                    block = true,
                ) {
                    com.yamal.designSystem.components.text
                        .Text("Block checkbox takes full width")
                }
                Checkbox(
                    checked = false,
                    onCheckedChange = {},
                    block = true,
                ) {
                    com.yamal.designSystem.components.text
                        .Text("Another block checkbox")
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckboxGroupPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf(setOf("apple")) }

                com.yamal.designSystem.components.text.Text(
                    "Checkbox Group",
                    style = YamalTheme.typography.bodyMedium,
                )
                CheckboxGroup(
                    value = selected,
                    onValueChange = { selected = it },
                ) {
                    CheckboxGroupItem(value = "apple") {
                        com.yamal.designSystem.components.text
                            .Text("Apple")
                    }
                    CheckboxGroupItem(value = "orange") {
                        com.yamal.designSystem.components.text
                            .Text("Orange")
                    }
                    CheckboxGroupItem(value = "banana") {
                        com.yamal.designSystem.components.text
                            .Text("Banana")
                    }
                }

                com.yamal.designSystem.components.text.Text(
                    "Selected: ${selected.joinToString()}",
                    style = YamalTheme.typography.small,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CheckboxGroupDisabledPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                com.yamal.designSystem.components.text.Text(
                    "Disabled Group",
                    style = YamalTheme.typography.bodyMedium,
                )
                CheckboxGroup(
                    value = setOf("apple"),
                    onValueChange = {},
                    disabled = true,
                ) {
                    CheckboxGroupItem(value = "apple") {
                        com.yamal.designSystem.components.text
                            .Text("Apple")
                    }
                    CheckboxGroupItem(value = "orange") {
                        com.yamal.designSystem.components.text
                            .Text("Orange")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckboxSelectAllPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val allItems = setOf("apple", "orange", "banana")
                var selected by remember { mutableStateOf(setOf("apple")) }

                val allSelected = selected == allItems
                val someSelected = selected.isNotEmpty() && !allSelected

                com.yamal.designSystem.components.text.Text(
                    "Select All Pattern",
                    style = YamalTheme.typography.bodyMedium,
                )

                Checkbox(
                    checked = allSelected,
                    onCheckedChange = { checked ->
                        selected = if (checked) allItems else emptySet()
                    },
                    indeterminate = someSelected,
                ) {
                    com.yamal.designSystem.components.text
                        .Text("Select all")
                }

                CheckboxGroup(
                    value = selected,
                    onValueChange = { selected = it },
                    modifier = Modifier.padding(start = 24.dp),
                ) {
                    CheckboxGroupItem(value = "apple") {
                        com.yamal.designSystem.components.text
                            .Text("Apple")
                    }
                    CheckboxGroupItem(value = "orange") {
                        com.yamal.designSystem.components.text
                            .Text("Orange")
                    }
                    CheckboxGroupItem(value = "banana") {
                        com.yamal.designSystem.components.text
                            .Text("Banana")
                    }
                }
            }
        }
    }
}
