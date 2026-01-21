package com.yamal.designSystem.components.selector

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Represents a selectable option in the Selector component.
 *
 * @param V The type of the value associated with the option
 * @param label The display label for the option
 * @param value The unique value identifier for this option
 * @param description Optional additional descriptive text shown below the label
 * @param disabled Whether this specific option is disabled
 */
@Immutable
data class SelectorOption<V>(
    val label: String,
    val value: V,
    val description: String? = null,
    val disabled: Boolean = false,
)

/**
 * Default values for [Selector] component following Ant Design Mobile specifications.
 */
object SelectorDefaults {
    /** Padding: 8dp 12dp (--padding) */
    val HorizontalPadding: Dp = 12.dp
    val VerticalPadding: Dp = 8.dp

    /** Font size: 15sp (--font-size: var(--adm-font-size-7)) */
    val FontSize = 15.sp

    /** Border radius: 2dp */
    val BorderRadius: Dp = 2.dp

    /** Border width: 1dp */
    val BorderWidth: Dp = 1.dp

    /** Disabled opacity: 0.4 */
    const val DISABLED_ALPHA = 0.4f

    /** Gap between items when not using columns: 8dp */
    val Gap: Dp = 8.dp

    /** Primary color - checked state */
    val primaryColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Border color - unchecked state */
    val borderColor: Color
        @Composable get() = YamalTheme.colors.border

    /** Text color - unchecked state */
    val textColor: Color
        @Composable get() = YamalTheme.colors.text

    /** Text color - checked state */
    val checkedTextColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Background color - unchecked state */
    val backgroundColor: Color
        @Composable get() = Color.Transparent

    /** Background color - checked state */
    val checkedBackgroundColor: Color
        @Composable get() = YamalTheme.colors.wathet

    /** Description color */
    val descriptionColor: Color
        @Composable get() = YamalTheme.colors.textSecondary
}

/**
 * Selector component following Ant Design Mobile specifications.
 *
 * A component for making single or multiple selections from a set of options.
 * Options can be displayed in columns or as a flowing list.
 *
 * @param V The type of the value associated with options
 * @param options List of selectable options
 * @param value Currently selected values
 * @param onValueChange Callback when selection changes, receives the new list of selected values and the selected items
 * @param modifier Modifier for the selector
 * @param multiple Whether to allow multiple selections (default: false for single selection)
 * @param disabled Whether all options are disabled
 * @param columns Number of columns to arrange options in (null for flowing layout)
 * @param showCheckMark Whether to show checkmark on selected items (default: true)
 *
 * Example usage:
 * ```
 * // Single selection
 * var selected by remember { mutableStateOf(emptyList<String>()) }
 * Selector(
 *     options = listOf(
 *         SelectorOption("Option A", "a"),
 *         SelectorOption("Option B", "b"),
 *         SelectorOption("Option C", "c")
 *     ),
 *     value = selected,
 *     onValueChange = { newValue, _ -> selected = newValue }
 * )
 *
 * // Multiple selection with 2 columns
 * var selected by remember { mutableStateOf(listOf("a")) }
 * Selector(
 *     options = listOf(
 *         SelectorOption("Apple", "a"),
 *         SelectorOption("Banana", "b"),
 *         SelectorOption("Cherry", "c"),
 *         SelectorOption("Date", "d")
 *     ),
 *     value = selected,
 *     onValueChange = { newValue, _ -> selected = newValue },
 *     multiple = true,
 *     columns = 2
 * )
 *
 * // With descriptions
 * Selector(
 *     options = listOf(
 *         SelectorOption("Basic", "basic", "Free forever"),
 *         SelectorOption("Pro", "pro", "$9.99/month"),
 *         SelectorOption("Enterprise", "enterprise", "Custom pricing", disabled = true)
 *     ),
 *     value = selected,
 *     onValueChange = { newValue, _ -> selected = newValue }
 * )
 * ```
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <V : Any> Selector(
    options: List<SelectorOption<V>>,
    value: List<V>,
    onValueChange: (List<V>, List<SelectorOption<V>>) -> Unit,
    modifier: Modifier = Modifier,
    multiple: Boolean = false,
    disabled: Boolean = false,
    columns: Int? = null,
    showCheckMark: Boolean = true,
) {
    if (columns != null) {
        // Grid layout with columns
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(SelectorDefaults.Gap),
        ) {
            options.chunked(columns).forEach { rowOptions ->
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(SelectorDefaults.Gap),
                ) {
                    rowOptions.forEach { option ->
                        val isActive = value.contains(option.value)
                        val isDisabled = option.disabled || disabled
                        Box(modifier = Modifier.weight(1f)) {
                            SelectorItem(
                                option = option,
                                isActive = isActive,
                                isDisabled = isDisabled,
                                multiple = multiple,
                                showCheckMark = showCheckMark,
                                onClick = {
                                    if (!isDisabled) {
                                        val newValue =
                                            if (multiple) {
                                                if (isActive) {
                                                    value.filter { it != option.value }
                                                } else {
                                                    value + option.value
                                                }
                                            } else {
                                                if (isActive) emptyList() else listOf(option.value)
                                            }
                                        val selectedItems = options.filter { newValue.contains(it.value) }
                                        onValueChange(newValue, selectedItems)
                                    }
                                },
                            )
                        }
                    }
                    // Fill empty spaces in the last row
                    repeat(columns - rowOptions.size) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    } else {
        // Flowing layout
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(SelectorDefaults.Gap),
            verticalArrangement = Arrangement.spacedBy(SelectorDefaults.Gap),
        ) {
            options.forEach { option ->
                val isActive = value.contains(option.value)
                val isDisabled = option.disabled || disabled
                SelectorItem(
                    option = option,
                    isActive = isActive,
                    isDisabled = isDisabled,
                    multiple = multiple,
                    showCheckMark = showCheckMark,
                    onClick = {
                        if (!isDisabled) {
                            val newValue =
                                if (multiple) {
                                    if (isActive) {
                                        value.filter { it != option.value }
                                    } else {
                                        value + option.value
                                    }
                                } else {
                                    if (isActive) emptyList() else listOf(option.value)
                                }
                            val selectedItems = options.filter { newValue.contains(it.value) }
                            onValueChange(newValue, selectedItems)
                        }
                    },
                )
            }
        }
    }
}

/**
 * Internal composable for rendering a single selector item.
 */
@Composable
private fun <V : Any> SelectorItem(
    option: SelectorOption<V>,
    isActive: Boolean,
    isDisabled: Boolean,
    multiple: Boolean,
    showCheckMark: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val backgroundColor by animateColorAsState(
        targetValue =
            if (isActive) {
                SelectorDefaults.checkedBackgroundColor
            } else {
                SelectorDefaults.backgroundColor
            },
        animationSpec = tween(200),
    )

    val borderColor by animateColorAsState(
        targetValue =
            if (isActive) {
                SelectorDefaults.primaryColor
            } else {
                SelectorDefaults.borderColor
            },
        animationSpec = tween(200),
    )

    val textColor by animateColorAsState(
        targetValue =
            if (isActive) {
                SelectorDefaults.checkedTextColor
            } else {
                SelectorDefaults.textColor
            },
        animationSpec = tween(200),
    )

    Surface(
        modifier =
            Modifier
                .clickable(
                    enabled = !isDisabled,
                    onClick = onClick,
                    role = Role.Checkbox,
                    interactionSource = interactionSource,
                    indication = null,
                ).alpha(if (isDisabled) SelectorDefaults.DISABLED_ALPHA else 1f),
        shape = RoundedCornerShape(SelectorDefaults.BorderRadius),
        color = backgroundColor,
        border = BorderStroke(SelectorDefaults.BorderWidth, borderColor),
    ) {
        Row(
            modifier =
                Modifier.padding(
                    horizontal = SelectorDefaults.HorizontalPadding,
                    vertical = SelectorDefaults.VerticalPadding,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Main content
            Column(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                val textStyle =
                    TextStyle(
                        fontSize = SelectorDefaults.FontSize,
                        color = textColor,
                    )

                CompositionLocalProvider(
                    LocalContentColor provides textColor,
                    LocalTextStyle provides textStyle,
                ) {
                    Text(text = option.label)

                    if (option.description != null) {
                        val descriptionStyle =
                            TextStyle(
                                fontSize = 13.sp,
                                color = SelectorDefaults.descriptionColor,
                            )
                        CompositionLocalProvider(
                            LocalTextStyle provides descriptionStyle,
                            LocalContentColor provides SelectorDefaults.descriptionColor,
                        ) {
                            Text(text = option.description)
                        }
                    }
                }
            }

            // Checkmark
            if (isActive && showCheckMark) {
                CheckMark(
                    modifier = Modifier.size(17.dp),
                    tint = SelectorDefaults.primaryColor,
                )
            }
        }
    }
}

/**
 * Checkmark icon for selected items.
 * Based on the Ant Design Mobile checkmark SVG.
 */
@Composable
private fun CheckMark(
    modifier: Modifier = Modifier,
    tint: Color = SelectorDefaults.primaryColor,
) {
    Icon(
        icon = Icons.Outlined.Check,
        contentDescription = "Selected",
        modifier = modifier,
        tint = tint,
    )
}

// Previews

@Preview
@Composable
private fun SelectorSinglePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf(listOf("a")) }

                Text(
                    "Single Selection",
                    style = YamalTheme.typography.bodyMedium,
                )

                Selector(
                    options =
                        listOf(
                            SelectorOption("Option A", "a"),
                            SelectorOption("Option B", "b"),
                            SelectorOption("Option C", "c"),
                        ),
                    value = selected,
                    onValueChange = { newValue, _ -> selected = newValue },
                )

                Text(
                    "Selected: ${selected.joinToString()}",
                    style = YamalTheme.typography.small,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectorMultiplePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf(listOf("a", "c")) }

                Text(
                    "Multiple Selection",
                    style = YamalTheme.typography.bodyMedium,
                )

                Selector(
                    options =
                        listOf(
                            SelectorOption("Option A", "a"),
                            SelectorOption("Option B", "b"),
                            SelectorOption("Option C", "c"),
                            SelectorOption("Option D", "d"),
                        ),
                    value = selected,
                    onValueChange = { newValue, _ -> selected = newValue },
                    multiple = true,
                )

                Text(
                    "Selected: ${selected.joinToString()}",
                    style = YamalTheme.typography.small,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectorColumnsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected1 by remember { mutableStateOf(listOf("a")) }
                var selected2 by remember { mutableStateOf(listOf("a", "c")) }
                var selected3 by remember { mutableStateOf(listOf("a")) }

                Text(
                    "Single Column",
                    style = YamalTheme.typography.bodyMedium,
                )
                Selector(
                    options =
                        listOf(
                            SelectorOption("Option A", "a"),
                            SelectorOption("Option B", "b"),
                            SelectorOption("Option C", "c"),
                        ),
                    value = selected1,
                    onValueChange = { newValue, _ -> selected1 = newValue },
                    columns = 1,
                )

                Text(
                    "Two Columns",
                    style = YamalTheme.typography.bodyMedium,
                )
                Selector(
                    options =
                        listOf(
                            SelectorOption("Option A", "a"),
                            SelectorOption("Option B", "b"),
                            SelectorOption("Option C", "c"),
                            SelectorOption("Option D", "d"),
                        ),
                    value = selected2,
                    onValueChange = { newValue, _ -> selected2 = newValue },
                    multiple = true,
                    columns = 2,
                )

                Text(
                    "Three Columns",
                    style = YamalTheme.typography.bodyMedium,
                )
                Selector(
                    options =
                        listOf(
                            SelectorOption("A", "a"),
                            SelectorOption("B", "b"),
                            SelectorOption("C", "c"),
                            SelectorOption("D", "d"),
                            SelectorOption("E", "e"),
                            SelectorOption("F", "f"),
                        ),
                    value = selected3,
                    onValueChange = { newValue, _ -> selected3 = newValue },
                    columns = 3,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectorWithDescriptionPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf(listOf("basic")) }

                Text(
                    "With Descriptions",
                    style = YamalTheme.typography.bodyMedium,
                )

                Selector(
                    options =
                        listOf(
                            SelectorOption("Basic", "basic", "Free forever"),
                            SelectorOption("Pro", "pro", "$9.99/month"),
                            SelectorOption("Enterprise", "enterprise", "Custom pricing"),
                        ),
                    value = selected,
                    onValueChange = { newValue, _ -> selected = newValue },
                )

                Text(
                    "Selected: ${selected.joinToString()}",
                    style = YamalTheme.typography.small,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectorDisabledPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf(listOf("a")) }

                Text(
                    "Disabled States",
                    style = YamalTheme.typography.bodyMedium,
                )

                // Individual disabled option
                Text(
                    "Individual Disabled",
                    style = YamalTheme.typography.small,
                )
                Selector(
                    options =
                        listOf(
                            SelectorOption("Option A", "a"),
                            SelectorOption("Option B (disabled)", "b", disabled = true),
                            SelectorOption("Option C", "c"),
                        ),
                    value = selected,
                    onValueChange = { newValue, _ -> selected = newValue },
                )

                // Entire selector disabled
                Text(
                    "All Disabled",
                    style = YamalTheme.typography.small,
                )
                Selector(
                    options =
                        listOf(
                            SelectorOption("Option A", "a"),
                            SelectorOption("Option B", "b"),
                            SelectorOption("Option C", "c"),
                        ),
                    value = selected,
                    onValueChange = { newValue, _ -> selected = newValue },
                    disabled = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectorNoCheckMarkPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var selected by remember { mutableStateOf(listOf("a")) }

                Text(
                    "Without Checkmarks",
                    style = YamalTheme.typography.bodyMedium,
                )

                Selector(
                    options =
                        listOf(
                            SelectorOption("Option A", "a"),
                            SelectorOption("Option B", "b"),
                            SelectorOption("Option C", "c"),
                        ),
                    value = selected,
                    onValueChange = { newValue, _ -> selected = newValue },
                    showCheckMark = false,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectorCompleteExamplePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                var selectedFruits by remember { mutableStateOf(listOf("apple", "banana")) }
                var selectedSize by remember { mutableStateOf(listOf("medium")) }

                Text(
                    "Complete Example",
                    style = YamalTheme.typography.displaySmall,
                )

                Text(
                    "Select Fruits (Multiple)",
                    style = YamalTheme.typography.bodyMedium,
                )
                Selector(
                    options =
                        listOf(
                            SelectorOption("Apple", "apple", "Fresh and crispy"),
                            SelectorOption("Banana", "banana", "Rich in potassium"),
                            SelectorOption("Orange", "orange", "Vitamin C"),
                            SelectorOption("Grapes", "grapes", "Seedless", disabled = true),
                        ),
                    value = selectedFruits,
                    onValueChange = { newValue, _ -> selectedFruits = newValue },
                    multiple = true,
                    columns = 2,
                )

                Text(
                    "Select Size (Single)",
                    style = YamalTheme.typography.bodyMedium,
                )
                Selector(
                    options =
                        listOf(
                            SelectorOption("Small", "small"),
                            SelectorOption("Medium", "medium"),
                            SelectorOption("Large", "large"),
                        ),
                    value = selectedSize,
                    onValueChange = { newValue, _ -> selectedSize = newValue },
                    columns = 3,
                )

                Text(
                    "Fruits: ${selectedFruits.joinToString()}\nSize: ${selectedSize.joinToString()}",
                    style = YamalTheme.typography.small,
                )
            }
        }
    }
}
