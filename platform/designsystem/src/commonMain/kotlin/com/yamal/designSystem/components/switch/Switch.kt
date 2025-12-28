package com.yamal.designSystem.components.switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [Switch] component following Ant Design Mobile specifications.
 *
 * CSS Variables mapped:
 * - `--width`: 51px
 * - `--height`: 31px
 * - `--border-width`: 2px
 * - `--checked-color`: var(--adm-color-primary)
 *
 * @see <a href="https://mobile.ant.design/components/switch">Ant Design Mobile Switch</a>
 */
object SwitchDefaults {
    /** Switch width: 51dp (from --width CSS variable) */
    val Width: Dp = 51.dp

    /** Switch height: 31dp (from --height CSS variable) */
    val Height: Dp = 31.dp

    /** Border/padding width: 2dp (from --border-width CSS variable) */
    val BorderWidth: Dp = 2.dp

    /** Handle/thumb size: height - (2 * border-width) = 27dp */
    val HandleSize: Dp = 27.dp

    /** Spin icon size within handle: 14dp */
    val SpinIconSize: Dp = 14.dp

    /** Animation duration: 200ms (from transition CSS) */
    const val ANIMATION_DURATION_MS = 200

    /** Disabled opacity: 0.4 (from .adm-switch-disabled opacity) */
    const val DISABLED_ALPHA = 0.4f

    /** Background color when checked - maps to --checked-color / --adm-color-primary */
    val checkedColor: Color
        @Composable get() = YamalTheme.colors.primary

    /** Background color when unchecked - maps to --adm-color-border */
    val uncheckedColor: Color
        @Composable get() = YamalTheme.colors.border

    /** Handle color - maps to --adm-color-text-light-solid (white) */
    val handleColor: Color
        @Composable get() = YamalTheme.colors.textLightSolid

    /** Inner text color when checked - maps to --adm-color-text-light-solid */
    val checkedTextColor: Color
        @Composable get() = YamalTheme.colors.textLightSolid

    /** Inner text color when unchecked - maps to --adm-color-weak */
    val uncheckedTextColor: Color
        @Composable get() = YamalTheme.colors.weak

    /** Spinner color - matches weak text color */
    val spinnerColor: Color
        @Composable get() = YamalTheme.colors.weak
}

/**
 * Switch component following Ant Design Mobile specifications exactly.
 *
 * A toggle switch for switching between two states (on/off).
 * Triggers state change immediately when tapped.
 *
 * @param checked Whether the switch is currently on
 * @param onCheckedChange Callback when the switch state changes
 * @param modifier Modifier for the switch
 * @param disabled Whether the switch is disabled
 * @param loading Whether the switch is in loading state. When loading,
 *   a spinner is shown instead of the handle and interaction is disabled.
 * @param beforeChange Optional async callback before state change.
 *   If provided, the switch will show loading state while the callback executes.
 *   If the callback throws an exception, the state change is cancelled.
 * @param checkedText Optional text/content to display when checked
 * @param uncheckedText Optional text/content to display when unchecked
 *
 * Example usage:
 * ```
 * // Basic switch
 * var checked by remember { mutableStateOf(false) }
 * Switch(
 *     checked = checked,
 *     onCheckedChange = { checked = it }
 * )
 *
 * // Disabled switch
 * Switch(
 *     checked = true,
 *     onCheckedChange = {},
 *     disabled = true
 * )
 *
 * // Loading switch
 * Switch(
 *     checked = false,
 *     onCheckedChange = {},
 *     loading = true
 * )
 *
 * // With async beforeChange callback
 * Switch(
 *     checked = checked,
 *     onCheckedChange = { checked = it },
 *     beforeChange = { newValue ->
 *         // Perform async validation
 *         delay(1000)
 *         // Throw to cancel the change
 *     }
 * )
 *
 * // With text labels
 * Switch(
 *     checked = checked,
 *     onCheckedChange = { checked = it },
 *     checkedText = { Text("ON") },
 *     uncheckedText = { Text("OFF") }
 * )
 * ```
 */
@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    loading: Boolean = false,
    beforeChange: (suspend (Boolean) -> Unit)? = null,
    checkedText: @Composable (() -> Unit)? = null,
    uncheckedText: @Composable (() -> Unit)? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    var changing by remember { mutableStateOf(false) }

    // Determine if interaction is enabled
    val isInteractionEnabled = !disabled && !loading && !changing

    // Calculate handle offset
    val handleOffset by animateDpAsState(
        targetValue =
            if (checked) {
                SwitchDefaults.Width - SwitchDefaults.HandleSize - SwitchDefaults.BorderWidth * 2
            } else {
                0.dp
            },
        animationSpec = tween(SwitchDefaults.ANIMATION_DURATION_MS),
    )

    // Track background color
    val backgroundColor by animateColorAsState(
        targetValue =
            if (checked) {
                SwitchDefaults.checkedColor
            } else {
                SwitchDefaults.uncheckedColor
            },
        animationSpec = tween(SwitchDefaults.ANIMATION_DURATION_MS),
    )

    // Show spinner when loading or changing
    val showSpinner = loading || changing

    Box(
        modifier =
            modifier
                .width(SwitchDefaults.Width)
                .height(SwitchDefaults.Height)
                .alpha(if (disabled) SwitchDefaults.DISABLED_ALPHA else 1f)
                .clip(RoundedCornerShape(SwitchDefaults.Height))
                .background(backgroundColor)
                .clickable(
                    enabled = isInteractionEnabled,
                    onClick = {
                        val newValue = !checked
                        if (beforeChange != null) {
                            changing = true
                            coroutineScope.launch {
                                try {
                                    beforeChange(newValue)
                                    onCheckedChange(newValue)
                                } catch (_: Exception) {
                                    // Change cancelled
                                } finally {
                                    changing = false
                                }
                            }
                        } else {
                            onCheckedChange(newValue)
                        }
                    },
                    role = Role.Switch,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ).padding(SwitchDefaults.BorderWidth),
        contentAlignment = Alignment.CenterStart,
    ) {
        // Inner text content (checkedText or uncheckedText)
        if (checked && checkedText != null) {
            Box(
                modifier =
                    Modifier
                        .padding(start = 4.dp, end = SwitchDefaults.HandleSize + 4.dp)
                        .align(Alignment.CenterStart),
            ) {
                CompositionLocalProvider(LocalContentColor provides SwitchDefaults.checkedTextColor) {
                    checkedText()
                }
            }
        } else if (!checked && uncheckedText != null) {
            Box(
                modifier =
                    Modifier
                        .padding(start = SwitchDefaults.HandleSize + 4.dp, end = 4.dp)
                        .align(Alignment.CenterEnd),
            ) {
                CompositionLocalProvider(LocalContentColor provides SwitchDefaults.uncheckedTextColor) {
                    uncheckedText()
                }
            }
        }

        // Handle with optional spinner
        Box(
            modifier =
                Modifier
                    .offset(x = handleOffset)
                    .size(SwitchDefaults.HandleSize)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        clip = false,
                    ).clip(CircleShape)
                    .background(SwitchDefaults.handleColor),
            contentAlignment = Alignment.Center,
        ) {
            if (showSpinner) {
                SpinLoadingIndicator(
                    size = SwitchDefaults.SpinIconSize,
                    color = SwitchDefaults.spinnerColor,
                )
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun SwitchBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var checked1 by remember { mutableStateOf(false) }
                var checked2 by remember { mutableStateOf(true) }

                Text("Basic", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Switch(
                        checked = checked1,
                        onCheckedChange = { checked1 = it },
                    )
                    Switch(
                        checked = checked2,
                        onCheckedChange = { checked2 = it },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwitchDisabledPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Disabled", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        disabled = true,
                    )
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        disabled = true,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwitchLoadingPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Loading", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        loading = true,
                    )
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        loading = true,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwitchWithTextPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var checked by remember { mutableStateOf(true) }

                Text("With Text", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Switch(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        checkedText = {
                            Text(
                                text = "ON",
                                style = YamalTheme.typography.small,
                            )
                        },
                        uncheckedText = {
                            Text(
                                text = "OFF",
                                style = YamalTheme.typography.small,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwitchAllStatesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("All States", style = YamalTheme.typography.bodyMedium)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Normal:", style = YamalTheme.typography.small)
                    Switch(checked = false, onCheckedChange = {})
                    Switch(checked = true, onCheckedChange = {})
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Disabled:", style = YamalTheme.typography.small)
                    Switch(checked = false, onCheckedChange = {}, disabled = true)
                    Switch(checked = true, onCheckedChange = {}, disabled = true)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Loading:", style = YamalTheme.typography.small)
                    Switch(checked = false, onCheckedChange = {}, loading = true)
                    Switch(checked = true, onCheckedChange = {}, loading = true)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwitchWithIconPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var checked by remember { mutableStateOf(true) }

                Text("With Icons", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Switch(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        checkedText = {
                            Icon(
                                icon = Icons.Outlined.Check,
                                contentDescription = null,
                            )
                        },
                        uncheckedText = {
                            Icon(
                                icon = Icons.Outlined.Close,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                            )
                        },
                    )
                }
            }
        }
    }
}
