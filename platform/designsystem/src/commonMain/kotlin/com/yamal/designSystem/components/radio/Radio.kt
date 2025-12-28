package com.yamal.designSystem.components.radio

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
 * Default values for [Radio] component following Ant Design Mobile specifications.
 */
object RadioDefaults {
    /** Icon size: 22dp (--icon-size: 22px) */
    val IconSize: Dp = 22.dp

    /** Font size: 17sp (--font-size: var(--adm-font-size-9)) */
    val FontSize = 17.sp

    /** Gap between icon and content: 8dp (--gap: 8px) */
    val Gap: Dp = 8.dp

    /** Border width */
    val BorderWidth: Dp = 1.dp

    /** Disabled opacity: 0.4 */
    const val DISABLED_ALPHA = 0.4f

    /** Primary color - checked state border and background */
    val primaryColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Light color - unchecked border - --adm-color-light */
    val lightColor: Color
        @Composable get() = YamalTheme.colors.light

    /** Icon content color (checkmark on checked) - --adm-color-text-light-solid */
    val iconColor: Color
        @Composable get() = YamalTheme.colors.textLightSolid

    /** Text color */
    val contentColor: Color
        @Composable get() = YamalTheme.colors.text
}

/**
 * Radio component following Ant Design Mobile specifications exactly.
 *
 * Used for making a single selection from multiple options.
 * Unlike Checkbox which allows multiple selections, Radio only allows one selection at a time.
 *
 * @param checked Whether the radio is currently checked
 * @param onCheckedChange Callback when the radio state changes
 * @param modifier Modifier for the radio
 * @param enabled Whether the radio is enabled (opposite of disabled)
 * @param block Whether to render as block-level element (full width)
 * @param icon Custom icon renderer. Receives checked state and returns composable.
 *   When null, uses default radio icon.
 * @param content Optional content (label) to display next to the radio
 *
 * Example usage:
 * ```
 * // Basic radio
 * Radio(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it }
 * )
 *
 * // With label
 * Radio(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it }
 * ) {
 *     Text("Option A")
 * }
 *
 * // Block mode (full width)
 * Radio(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it },
 *     block = true
 * ) {
 *     Text("Full width radio option")
 * }
 *
 * // Custom icon
 * Radio(
 *     checked = isChecked,
 *     onCheckedChange = { isChecked = it },
 *     icon = { checked ->
 *         Icon(if (checked) Icons.Filled.Star else Icons.Outlined.Star)
 *     }
 * )
 * ```
 */
@Composable
fun Radio(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    block: Boolean = false,
    icon: (@Composable (checked: Boolean) -> Unit)? = null,
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
                    role = Role.RadioButton,
                    interactionSource = interactionSource,
                    indication = null,
                ).alpha(if (enabled) 1f else RadioDefaults.DISABLED_ALPHA),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            icon(checked)
        } else {
            RadioIcon(
                checked = checked,
                enabled = enabled,
            )
        }

        if (content != null) {
            Spacer(Modifier.width(RadioDefaults.Gap))
            val textStyle =
                TextStyle(
                    fontSize = RadioDefaults.FontSize,
                    color = RadioDefaults.contentColor,
                )
            ProvideTextStyle(value = textStyle) {
                CompositionLocalProvider(LocalContentColor provides RadioDefaults.contentColor) {
                    content()
                }
            }
        }
    }
}

/**
 * Convenience overload that accepts a String for the label.
 *
 * @param checked Whether the radio is currently checked
 * @param onCheckedChange Callback when the radio state changes
 * @param text The label text to display
 * @param modifier Modifier for the radio
 * @param enabled Whether the radio is enabled
 * @param block Whether to render as block-level element
 */
@Composable
fun Radio(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    block: Boolean = false,
) {
    Radio(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        block = block,
    ) {
        com.yamal.designSystem.components.text
            .Text(text)
    }
}

@Composable
private fun RadioIcon(
    checked: Boolean,
    enabled: Boolean,
) {
    val backgroundColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> Color.Transparent
                checked -> RadioDefaults.primaryColor
                else -> Color.Transparent
            },
        animationSpec = tween(150),
    )

    val borderColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> RadioDefaults.lightColor
                checked -> RadioDefaults.primaryColor
                else -> RadioDefaults.lightColor
            },
        animationSpec = tween(150),
    )

    val iconTint by animateColorAsState(
        targetValue =
            when {
                !enabled && checked -> RadioDefaults.lightColor
                checked -> RadioDefaults.iconColor
                else -> Color.Transparent
            },
        animationSpec = tween(150),
    )

    Box(
        modifier =
            Modifier
                .size(RadioDefaults.IconSize)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(
                    width = RadioDefaults.BorderWidth,
                    color = borderColor,
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            // Checked state - checkmark icon
            Icon(
                icon = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = iconTint,
            )
        }
    }
}

/**
 * Context for RadioGroup to manage shared state.
 */
internal data class RadioGroupContext<T>(
    val selectedValue: T?,
    val onValueChange: (T) -> Unit,
    val disabled: Boolean,
)

@Suppress("UNCHECKED_CAST")
internal val LocalRadioGroup = compositionLocalOf<RadioGroupContext<Any>?> { null }

/**
 * A group of radio buttons for single selection following Ant Design Mobile specifications.
 *
 * @param value Currently selected value (null if none selected)
 * @param onValueChange Callback when selection changes
 * @param modifier Modifier for the group
 * @param disabled Whether all radios in the group are disabled
 * @param content The radios to display, typically using [RadioGroupItem]
 *
 * Example usage:
 * ```
 * var selected by remember { mutableStateOf<String?>("apple") }
 *
 * RadioGroup(
 *     value = selected,
 *     onValueChange = { selected = it }
 * ) {
 *     RadioGroupItem(value = "apple") { Text("Apple") }
 *     RadioGroupItem(value = "orange") { Text("Orange") }
 *     RadioGroupItem(value = "banana") { Text("Banana") }
 * }
 * ```
 */
@Composable
fun <T : Any> RadioGroup(
    value: T?,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    @Suppress("UNCHECKED_CAST")
    val context =
        RadioGroupContext<Any>(
            selectedValue = value,
            onValueChange = onValueChange as (Any) -> Unit,
            disabled = disabled,
        )

    CompositionLocalProvider(LocalRadioGroup provides context) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            content()
        }
    }
}

/**
 * A radio item for use within [RadioGroup].
 *
 * @param value The value identifier for this item
 * @param modifier Modifier for the item
 * @param enabled Whether this specific item is enabled
 * @param content The label content
 */
@Composable
fun <T : Any> RadioGroupItem(
    value: T,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val groupContext =
        LocalRadioGroup.current
            ?: error("RadioGroupItem must be used within a RadioGroup")

    val isChecked = groupContext.selectedValue == value
    val isEnabled = enabled && !groupContext.disabled

    Radio(
        checked = isChecked,
        onCheckedChange = { checked ->
            if (checked) {
                @Suppress("UNCHECKED_CAST")
                (groupContext.onValueChange as (T) -> Unit)(value)
            }
        },
        modifier = modifier,
        enabled = isEnabled,
        content = content,
    )
}

/**
 * Convenience overload that accepts a String for the label.
 *
 * @param value The value identifier for this item
 * @param text The label text to display
 * @param modifier Modifier for the item
 * @param enabled Whether this specific item is enabled
 */
@Composable
fun <T : Any> RadioGroupItem(
    value: T,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    RadioGroupItem(
        value = value,
        modifier = modifier,
        enabled = enabled,
    ) {
        com.yamal.designSystem.components.text
            .Text(text)
    }
}

// Previews

@Preview
@Composable
private fun RadioBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                var selectedOption by remember { mutableStateOf(1) }

                com.yamal.designSystem.components.text.Text(
                    "Basic",
                    style = YamalTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Radio(
                        checked = selectedOption == 1,
                        onCheckedChange = { if (it) selectedOption = 1 },
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Option 1")
                    }
                    Radio(
                        checked = selectedOption == 2,
                        onCheckedChange = { if (it) selectedOption = 2 },
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Option 2")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RadioStatesPreview() {
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
                    Radio(
                        checked = false,
                        onCheckedChange = {},
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Unchecked")
                    }
                    Radio(
                        checked = true,
                        onCheckedChange = {},
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
private fun RadioDisabledPreview() {
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
                    Radio(
                        checked = false,
                        onCheckedChange = {},
                        enabled = false,
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Disabled")
                    }
                    Radio(
                        checked = true,
                        onCheckedChange = {},
                        enabled = false,
                    ) {
                        com.yamal.designSystem.components.text
                            .Text("Disabled Checked")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RadioBlockPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                var selectedOption by remember { mutableStateOf(1) }

                com.yamal.designSystem.components.text.Text(
                    "Block Mode",
                    style = YamalTheme.typography.bodyMedium,
                )
                Radio(
                    checked = selectedOption == 1,
                    onCheckedChange = { if (it) selectedOption = 1 },
                    block = true,
                ) {
                    com.yamal.designSystem.components.text
                        .Text("Block radio takes full width")
                }
                Radio(
                    checked = selectedOption == 2,
                    onCheckedChange = { if (it) selectedOption = 2 },
                    block = true,
                ) {
                    com.yamal.designSystem.components.text
                        .Text("Another block radio")
                }
            }
        }
    }
}

@Preview
@Composable
private fun RadioGroupPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf<String?>("apple") }

                com.yamal.designSystem.components.text.Text(
                    "Radio Group",
                    style = YamalTheme.typography.bodyMedium,
                )
                RadioGroup(
                    value = selected,
                    onValueChange = { selected = it },
                ) {
                    RadioGroupItem(value = "apple") {
                        com.yamal.designSystem.components.text
                            .Text("Apple")
                    }
                    RadioGroupItem(value = "orange") {
                        com.yamal.designSystem.components.text
                            .Text("Orange")
                    }
                    RadioGroupItem(value = "banana") {
                        com.yamal.designSystem.components.text
                            .Text("Banana")
                    }
                }

                com.yamal.designSystem.components.text.Text(
                    "Selected: ${selected ?: "None"}",
                    style = YamalTheme.typography.small,
                )
            }
        }
    }
}

@Preview
@Composable
private fun RadioGroupDisabledPreview() {
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
                RadioGroup(
                    value = "apple",
                    onValueChange = {},
                    disabled = true,
                ) {
                    RadioGroupItem(value = "apple") {
                        com.yamal.designSystem.components.text
                            .Text("Apple")
                    }
                    RadioGroupItem(value = "orange") {
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
private fun RadioGroupWithDisabledItemPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf<String?>("apple") }

                com.yamal.designSystem.components.text.Text(
                    "Group with Disabled Item",
                    style = YamalTheme.typography.bodyMedium,
                )
                RadioGroup(
                    value = selected,
                    onValueChange = { selected = it },
                ) {
                    RadioGroupItem(value = "apple") {
                        com.yamal.designSystem.components.text
                            .Text("Apple")
                    }
                    RadioGroupItem(value = "orange") {
                        com.yamal.designSystem.components.text
                            .Text("Orange")
                    }
                    RadioGroupItem(value = "banana", enabled = false) {
                        com.yamal.designSystem.components.text
                            .Text("Banana (disabled)")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RadioStringOverloadPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf<String?>("a") }

                com.yamal.designSystem.components.text.Text(
                    "String Overload",
                    style = YamalTheme.typography.bodyMedium,
                )
                RadioGroup(
                    value = selected,
                    onValueChange = { selected = it },
                ) {
                    RadioGroupItem(value = "a", text = "Option A")
                    RadioGroupItem(value = "b", text = "Option B")
                    RadioGroupItem(value = "c", text = "Option C")
                }
            }
        }
    }
}
