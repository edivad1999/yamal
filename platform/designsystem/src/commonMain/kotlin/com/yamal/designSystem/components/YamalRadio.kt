package com.yamal.designSystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
 * Radio button sizes following Ant Design guidelines.
 */
@Immutable
enum class YamalRadioSize {
    Small,
    Default,
    Large,
}

/**
 * Radio button option type for Radio.Group
 */
@Immutable
enum class YamalRadioOptionType {
    Default,
    Button,
}

/**
 * Radio button style for button type
 */
@Immutable
enum class YamalRadioButtonStyle {
    Outline,
    Solid,
}

/**
 * A radio button component following Ant Design guidelines.
 * Used for single selection from multiple options.
 *
 * @param selected Whether the radio is selected
 * @param onClick Callback when the radio is clicked
 * @param modifier Modifier for the radio
 * @param enabled Whether the radio is enabled
 * @param size Size of the radio
 * @param label Optional label text
 */
@Composable
fun YamalRadio(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: YamalRadioSize = YamalRadioSize.Default,
    label: String? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val radioSize =
        when (size) {
            YamalRadioSize.Small -> 14.dp
            YamalRadioSize.Default -> 16.dp
            YamalRadioSize.Large -> 20.dp
        }

    val innerDotSize =
        when (size) {
            YamalRadioSize.Small -> 6.dp
            YamalRadioSize.Default -> 8.dp
            YamalRadioSize.Large -> 10.dp
        }

    val textStyle =
        when (size) {
            YamalRadioSize.Small -> typography.footnote
            YamalRadioSize.Default -> typography.body
            YamalRadioSize.Large -> typography.body
        }

    val borderColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> colors.neutralColors.disableText
                selected -> colors.paletteColors.color6
                else -> colors.neutralColors.border
            },
        animationSpec = tween(150),
    )

    val innerDotColor by animateColorAsState(
        targetValue =
            when {
                !enabled && selected -> colors.neutralColors.disableText
                selected -> colors.paletteColors.color6
                else -> Color.Transparent
            },
        animationSpec = tween(150),
    )

    val innerDotAnimatedSize by animateDpAsState(
        targetValue = if (selected) innerDotSize else 0.dp,
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
                    onClick = onClick,
                    role = Role.RadioButton,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ).padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(radioSize)
                    .clip(CircleShape)
                    .border(
                        width = if (selected) 2.dp else 1.dp,
                        color = borderColor,
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(innerDotAnimatedSize)
                        .clip(CircleShape)
                        .background(innerDotColor),
            )
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
 * Radio button option for RadioGroup.
 */
@Immutable
data class YamalRadioOption<T>(
    val value: T,
    val label: String,
    val disabled: Boolean = false,
)

/**
 * A group of radio buttons for single selection.
 *
 * @param options List of options to display
 * @param selectedValue Currently selected value
 * @param onSelectionChange Callback when selection changes
 * @param modifier Modifier for the group
 * @param enabled Whether the group is enabled
 * @param size Size of the radio buttons
 * @param optionType Type of radio (default circle or button style)
 * @param buttonStyle Style for button type (outline or solid)
 * @param direction Layout direction of the group
 */
@Composable
fun <T> YamalRadioGroup(
    options: List<YamalRadioOption<T>>,
    selectedValue: T?,
    onSelectionChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: YamalRadioSize = YamalRadioSize.Default,
    optionType: YamalRadioOptionType = YamalRadioOptionType.Default,
    buttonStyle: YamalRadioButtonStyle = YamalRadioButtonStyle.Outline,
    direction: YamalSpaceDirection = YamalSpaceDirection.Horizontal,
) {
    val spacing =
        when (size) {
            YamalRadioSize.Small -> Dimension.Spacing.xs
            YamalRadioSize.Default -> Dimension.Spacing.sm
            YamalRadioSize.Large -> Dimension.Spacing.md
        }

    when (optionType) {
        YamalRadioOptionType.Default -> {
            if (direction == YamalSpaceDirection.Horizontal) {
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    options.forEach { option ->
                        YamalRadio(
                            selected = selectedValue == option.value,
                            onClick = { onSelectionChange(option.value) },
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
                        YamalRadio(
                            selected = selectedValue == option.value,
                            onClick = { onSelectionChange(option.value) },
                            enabled = enabled && !option.disabled,
                            size = size,
                            label = option.label,
                        )
                    }
                }
            }
        }

        YamalRadioOptionType.Button -> {
            YamalRadioButtonGroup(
                options = options,
                selectedValue = selectedValue,
                onSelectionChange = onSelectionChange,
                modifier = modifier,
                enabled = enabled,
                size = size,
                buttonStyle = buttonStyle,
            )
        }
    }
}

/**
 * Radio button group with button-style options.
 */
@Composable
private fun <T> YamalRadioButtonGroup(
    options: List<YamalRadioOption<T>>,
    selectedValue: T?,
    onSelectionChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: YamalRadioSize = YamalRadioSize.Default,
    buttonStyle: YamalRadioButtonStyle = YamalRadioButtonStyle.Outline,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val height =
        when (size) {
            YamalRadioSize.Small -> 24.dp
            YamalRadioSize.Default -> 32.dp
            YamalRadioSize.Large -> 40.dp
        }

    val textStyle =
        when (size) {
            YamalRadioSize.Small -> typography.footnote
            YamalRadioSize.Default -> typography.body
            YamalRadioSize.Large -> typography.body
        }

    val horizontalPadding =
        when (size) {
            YamalRadioSize.Small -> 8.dp
            YamalRadioSize.Default -> 16.dp
            YamalRadioSize.Large -> 16.dp
        }

    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(6.dp))
                .border(1.dp, colors.neutralColors.border, RoundedCornerShape(6.dp)),
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedValue == option.value
            val isDisabled = !enabled || option.disabled

            val backgroundColor by animateColorAsState(
                targetValue =
                    when {
                        isDisabled && isSelected -> colors.neutralColors.disableText
                        isDisabled -> colors.neutralColors.fillQuaternary
                        isSelected && buttonStyle == YamalRadioButtonStyle.Solid -> colors.paletteColors.color6
                        isSelected -> colors.paletteColors.color1
                        else -> Color.Transparent
                    },
                animationSpec = tween(150),
            )

            val textColor by animateColorAsState(
                targetValue =
                    when {
                        isDisabled -> colors.neutralColors.disableText
                        isSelected && buttonStyle == YamalRadioButtonStyle.Solid -> colors.neutralColors.background
                        isSelected -> colors.paletteColors.color6
                        else -> colors.neutralColors.primaryText
                    },
                animationSpec = tween(150),
            )

            val borderColor = if (index > 0) colors.neutralColors.border else Color.Transparent

            Box(
                modifier =
                    Modifier
                        .border(width = if (index > 0) 1.dp else 0.dp, color = borderColor)
                        .background(backgroundColor)
                        .clickable(
                            enabled = !isDisabled,
                            onClick = { onSelectionChange(option.value) },
                            role = Role.RadioButton,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ).padding(horizontal = horizontalPadding)
                        .size(width = androidx.compose.ui.unit.Dp.Unspecified, height = height),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option.label,
                    style = textStyle,
                    color = textColor,
                )
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalRadioBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var selected by remember { mutableStateOf(false) }

                Text("Basic", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
                    YamalRadio(
                        selected = !selected,
                        onClick = { selected = false },
                        label = "Option A",
                    )
                    YamalRadio(
                        selected = selected,
                        onClick = { selected = true },
                        label = "Option B",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalRadioSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Sizes", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    YamalRadio(
                        selected = true,
                        onClick = {},
                        size = YamalRadioSize.Small,
                        label = "Small",
                    )
                    YamalRadio(
                        selected = true,
                        onClick = {},
                        size = YamalRadioSize.Default,
                        label = "Default",
                    )
                    YamalRadio(
                        selected = true,
                        onClick = {},
                        size = YamalRadioSize.Large,
                        label = "Large",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalRadioDisabledPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Disabled", style = YamalTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
                    YamalRadio(
                        selected = false,
                        onClick = {},
                        enabled = false,
                        label = "Disabled",
                    )
                    YamalRadio(
                        selected = true,
                        onClick = {},
                        enabled = false,
                        label = "Disabled Selected",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalRadioGroupPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var selected by remember { mutableStateOf("apple") }

                Text("Radio Group", style = YamalTheme.typography.bodyMedium)
                YamalRadioGroup(
                    options =
                        listOf(
                            YamalRadioOption("apple", "Apple"),
                            YamalRadioOption("pear", "Pear"),
                            YamalRadioOption("orange", "Orange"),
                            YamalRadioOption("banana", "Banana", disabled = true),
                        ),
                    selectedValue = selected,
                    onSelectionChange = { selected = it },
                )

                Text("Vertical Group", style = YamalTheme.typography.bodyMedium)
                YamalRadioGroup(
                    options =
                        listOf(
                            YamalRadioOption("a", "Option A"),
                            YamalRadioOption("b", "Option B"),
                            YamalRadioOption("c", "Option C"),
                        ),
                    selectedValue = selected,
                    onSelectionChange = { selected = it },
                    direction = YamalSpaceDirection.Vertical,
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalRadioButtonGroupPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var selected1 by remember { mutableStateOf("a") }
                var selected2 by remember { mutableStateOf("a") }

                Text("Button Style - Outline", style = YamalTheme.typography.bodyMedium)
                YamalRadioGroup(
                    options =
                        listOf(
                            YamalRadioOption("a", "Hangzhou"),
                            YamalRadioOption("b", "Shanghai"),
                            YamalRadioOption("c", "Beijing"),
                        ),
                    selectedValue = selected1,
                    onSelectionChange = { selected1 = it },
                    optionType = YamalRadioOptionType.Button,
                    buttonStyle = YamalRadioButtonStyle.Outline,
                )

                Text("Button Style - Solid", style = YamalTheme.typography.bodyMedium)
                YamalRadioGroup(
                    options =
                        listOf(
                            YamalRadioOption("a", "Hangzhou"),
                            YamalRadioOption("b", "Shanghai"),
                            YamalRadioOption("c", "Beijing"),
                        ),
                    selectedValue = selected2,
                    onSelectionChange = { selected2 = it },
                    optionType = YamalRadioOptionType.Button,
                    buttonStyle = YamalRadioButtonStyle.Solid,
                )
            }
        }
    }
}
