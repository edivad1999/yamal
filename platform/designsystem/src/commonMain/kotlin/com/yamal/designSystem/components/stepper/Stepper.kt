package com.yamal.designSystem.components.stepper

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Stepper size variants.
 */
@Immutable
enum class StepperSize {
    /** Small stepper: 24dp height */
    Small,

    /** Medium stepper: 28dp height (default) */
    Medium,

    /** Large stepper: 36dp height */
    Large,
}

/**
 * Default values for Stepper component following Ant Design Mobile specifications.
 *
 * ADM CSS values:
 * - Height: 28dp
 * - Input width: 44dp
 * - Button width: equal to height
 * - Border radius: 2dp
 * - Input background: fill-content color
 * - Button background: fill-content color
 *
 * @see <a href="https://mobile.ant.design/components/stepper">Ant Design Mobile Stepper</a>
 */
object StepperDefaults {
    /** Animation duration */
    const val ANIMATION_DURATION_MS = 150

    /** Border radius: 2dp */
    val borderRadius = 2.dp

    /** Default height: 28dp */
    val heightMedium = 28.dp
    val heightSmall = 24.dp
    val heightLarge = 36.dp

    /** Input width: 44dp */
    val inputWidthMedium = 44.dp
    val inputWidthSmall = 36.dp
    val inputWidthLarge = 56.dp

    /** Icon size relative to button */
    val iconSizeSmall = 14.dp
    val iconSizeMedium = 17.dp
    val iconSizeLarge = 21.dp

    /** Input background color */
    val inputBackgroundColor: Color
        @Composable get() = YamalTheme.colors.fillContent

    /** Button background color */
    val buttonBackgroundColor: Color
        @Composable get() = YamalTheme.colors.fillContent

    /** Button text/icon color */
    val buttonColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Disabled color */
    val disabledColor: Color
        @Composable get() = YamalTheme.colors.weak

    /** Input text color */
    val inputTextColor: Color
        @Composable get() = YamalTheme.colors.text
}

/**
 * Stepper component following Ant Design Mobile specifications.
 *
 * A control to increment or decrement a numeric value.
 * Useful for episode progress, quantities, etc.
 *
 * @param value The current value
 * @param onValueChange Callback when value changes
 * @param modifier Modifier for the stepper
 * @param min Minimum allowed value
 * @param max Maximum allowed value
 * @param step Step increment/decrement amount
 * @param size Stepper size variant
 * @param disabled Whether the stepper is disabled
 * @param inputReadOnly Whether the input field is read-only (only buttons work)
 */
@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
    step: Int = 1,
    size: StepperSize = StepperSize.Medium,
    disabled: Boolean = false,
    inputReadOnly: Boolean = false,
) {
    val height =
        when (size) {
            StepperSize.Small -> StepperDefaults.heightSmall
            StepperSize.Medium -> StepperDefaults.heightMedium
            StepperSize.Large -> StepperDefaults.heightLarge
        }

    val inputWidth =
        when (size) {
            StepperSize.Small -> StepperDefaults.inputWidthSmall
            StepperSize.Medium -> StepperDefaults.inputWidthMedium
            StepperSize.Large -> StepperDefaults.inputWidthLarge
        }

    val iconSize =
        when (size) {
            StepperSize.Small -> StepperDefaults.iconSizeSmall
            StepperSize.Medium -> StepperDefaults.iconSizeMedium
            StepperSize.Large -> StepperDefaults.iconSizeLarge
        }

    val typography =
        when (size) {
            StepperSize.Small -> YamalTheme.typography.caption
            StepperSize.Medium -> YamalTheme.typography.body
            StepperSize.Large -> YamalTheme.typography.titleSmall
        }

    val minusDisabled = disabled || value <= min
    val plusDisabled = disabled || value >= max

    var textValue by remember(value) { mutableStateOf(value.toString()) }
    var isFocused by remember { mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue = if (disabled) StepperDefaults.disabledColor else StepperDefaults.buttonColor,
        animationSpec = tween(StepperDefaults.ANIMATION_DURATION_MS),
    )

    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(StepperDefaults.borderRadius)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Minus button
        StepperButton(
            onClick = {
                val newValue = (value - step).coerceAtLeast(min)
                onValueChange(newValue)
            },
            enabled = !minusDisabled,
            height = height,
            iconSize = iconSize,
            contentDescription = "Decrease",
        ) {
            Icon(
                Icons.Outlined.Minus,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (minusDisabled) StepperDefaults.disabledColor else buttonColor,
            )
        }

        // Input field
        Box(
            modifier =
                Modifier
                    .width(inputWidth)
                    .height(height)
                    .background(StepperDefaults.inputBackgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            if (inputReadOnly) {
                Text(
                    text = value.toString(),
                    style = typography,
                    color = if (disabled) StepperDefaults.disabledColor else StepperDefaults.inputTextColor,
                    textAlign = TextAlign.Center,
                )
            } else {
                BasicTextField(
                    value = if (isFocused) textValue else value.toString(),
                    onValueChange = { newText ->
                        // Only allow digits
                        val filtered = newText.filter { it.isDigit() }
                        textValue = filtered

                        // Parse and validate
                        filtered.toIntOrNull()?.let { newValue ->
                            val clampedValue = newValue.coerceIn(min, max)
                            if (clampedValue != value) {
                                onValueChange(clampedValue)
                            }
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                if (!focusState.isFocused) {
                                    // Reset to current value when focus lost
                                    textValue = value.toString()
                                }
                            }.semantics {
                                contentDescription = "Episode count: $value"
                            },
                    enabled = !disabled,
                    textStyle =
                        typography.copy(
                            textAlign = TextAlign.Center,
                            color = if (disabled) StepperDefaults.disabledColor else StepperDefaults.inputTextColor,
                        ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    cursorBrush = SolidColor(YamalTheme.colors.primary),
                )
            }
        }

        // Plus button
        StepperButton(
            onClick = {
                val newValue = (value + step).coerceAtMost(max)
                onValueChange(newValue)
            },
            enabled = !plusDisabled,
            height = height,
            iconSize = iconSize,
            contentDescription = "Increase",
        ) {
            Icon(
                Icons.Outlined.Plus,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (plusDisabled) StepperDefaults.disabledColor else buttonColor,
            )
        }
    }
}

@Composable
private fun StepperButton(
    onClick: () -> Unit,
    enabled: Boolean,
    height: Dp,
    iconSize: Dp,
    contentDescription: String,
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(height) // Square button
                .background(StepperDefaults.buttonBackgroundColor)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ).semantics {
                    this.contentDescription = contentDescription
                },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * Episode stepper variant with label showing progress.
 *
 * @param watchedEpisodes Current watched episode count
 * @param totalEpisodes Total episodes (or null if unknown)
 * @param onValueChange Callback when watched count changes
 * @param modifier Modifier
 * @param size Stepper size
 * @param disabled Whether disabled
 */
@Composable
fun EpisodeStepper(
    watchedEpisodes: Int,
    totalEpisodes: Int?,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    size: StepperSize = StepperSize.Medium,
    disabled: Boolean = false,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
    ) {
        Stepper(
            value = watchedEpisodes,
            onValueChange = onValueChange,
            min = 0,
            max = totalEpisodes ?: Int.MAX_VALUE,
            size = size,
            disabled = disabled,
        )

        if (totalEpisodes != null) {
            Text(
                text = "/ $totalEpisodes",
                style = YamalTheme.typography.caption,
                color = YamalTheme.colors.textSecondary,
            )
        }
    }
}

// Previews

@Preview
@Composable
private fun StepperBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Basic Stepper", style = YamalTheme.typography.bodyMedium)

                var value by remember { mutableStateOf(5) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    min = 0,
                    max = 10,
                )

                Text("Value: $value", style = YamalTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
private fun StepperSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Sizes", style = YamalTheme.typography.bodyMedium)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(
                        value = 5,
                        onValueChange = {},
                        size = StepperSize.Small,
                    )
                    Text("Small", style = YamalTheme.typography.caption)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(
                        value = 5,
                        onValueChange = {},
                        size = StepperSize.Medium,
                    )
                    Text("Medium", style = YamalTheme.typography.caption)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(
                        value = 5,
                        onValueChange = {},
                        size = StepperSize.Large,
                    )
                    Text("Large", style = YamalTheme.typography.caption)
                }
            }
        }
    }
}

@Preview
@Composable
private fun StepperStatesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("States", style = YamalTheme.typography.bodyMedium)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(
                        value = 0,
                        onValueChange = {},
                        min = 0,
                        max = 10,
                    )
                    Text("At minimum", style = YamalTheme.typography.caption)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(
                        value = 10,
                        onValueChange = {},
                        min = 0,
                        max = 10,
                    )
                    Text("At maximum", style = YamalTheme.typography.caption)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(
                        value = 5,
                        onValueChange = {},
                        disabled = true,
                    )
                    Text("Disabled", style = YamalTheme.typography.caption)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(
                        value = 5,
                        onValueChange = {},
                        inputReadOnly = true,
                    )
                    Text("Read-only input", style = YamalTheme.typography.caption)
                }
            }
        }
    }
}

@Preview
@Composable
private fun EpisodeStepperPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Episode Stepper", style = YamalTheme.typography.bodyMedium)

                var watched by remember { mutableStateOf(7) }
                EpisodeStepper(
                    watchedEpisodes = watched,
                    totalEpisodes = 12,
                    onValueChange = { watched = it },
                )

                Text("Unknown total episodes", style = YamalTheme.typography.caption)
                EpisodeStepper(
                    watchedEpisodes = 24,
                    totalEpisodes = null,
                    onValueChange = {},
                )
            }
        }
    }
}
