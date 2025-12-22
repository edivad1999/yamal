package com.yamal.designSystem.components

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Switch sizes following Ant Design guidelines.
 */
@Immutable
enum class YamalSwitchSize {
    Small,
    Default,
}

/**
 * A switch component following Ant Design guidelines.
 * Used for toggling between two states, triggers state change immediately.
 *
 * @param checked Whether the switch is on
 * @param onCheckedChange Callback when the switch state changes
 * @param modifier Modifier for the switch
 * @param enabled Whether the switch is enabled
 * @param loading Whether the switch is in loading state
 * @param size Size of the switch
 * @param checkedContent Content to show when checked (e.g., "ON")
 * @param uncheckedContent Content to show when unchecked (e.g., "OFF")
 */
@Composable
fun YamalSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    size: YamalSwitchSize = YamalSwitchSize.Default,
    checkedContent: @Composable (() -> Unit)? = null,
    uncheckedContent: @Composable (() -> Unit)? = null,
) {
    val colors = YamalTheme.colors

    // Dimensions based on size
    val trackWidth =
        when (size) {
            YamalSwitchSize.Small -> 28.dp
            YamalSwitchSize.Default -> 44.dp
        }

    val trackHeight =
        when (size) {
            YamalSwitchSize.Small -> 16.dp
            YamalSwitchSize.Default -> 22.dp
        }

    val thumbSize =
        when (size) {
            YamalSwitchSize.Small -> 12.dp
            YamalSwitchSize.Default -> 18.dp
        }

    val thumbPadding = 2.dp
    val loadingSize = thumbSize - 4.dp

    // Calculate thumb offset
    val thumbOffset by animateDpAsState(
        targetValue =
            if (checked) {
                trackWidth - thumbSize - thumbPadding * 2
            } else {
                0.dp
            },
        animationSpec = tween(200),
    )

    // Colors
    val trackColor by animateColorAsState(
        targetValue =
            when {
                !enabled -> colors.neutralColors.disableText
                checked -> colors.paletteColors.color6
                else -> colors.neutralColors.fillSecondary
            },
        animationSpec = tween(200),
    )

    val thumbColor = colors.neutralColors.background

    val actuallyEnabled = enabled && !loading

    Box(
        modifier =
            modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(trackColor)
                .clickable(
                    enabled = actuallyEnabled,
                    onClick = { onCheckedChange(!checked) },
                    role = Role.Switch,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ).padding(thumbPadding),
        contentAlignment = Alignment.CenterStart,
    ) {
        // Content labels (only for default size)
        if (size == YamalSwitchSize.Default) {
            if (checked && checkedContent != null) {
                Box(
                    modifier =
                        Modifier
                            .padding(start = 6.dp, end = thumbSize + 4.dp)
                            .align(Alignment.CenterStart),
                ) {
                    checkedContent()
                }
            } else if (!checked && uncheckedContent != null) {
                Box(
                    modifier =
                        Modifier
                            .padding(start = thumbSize + 4.dp, end = 6.dp)
                            .align(Alignment.CenterEnd),
                ) {
                    uncheckedContent()
                }
            }
        }

        // Thumb with loading indicator
        Box(
            modifier =
                Modifier
                    .offset(x = thumbOffset)
                    .size(thumbSize)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(thumbColor),
            contentAlignment = Alignment.Center,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(loadingSize),
                    color = colors.paletteColors.color6,
                    strokeWidth = 1.5.dp,
                )
            }
        }
    }
}

/**
 * Creates text content for switch labels.
 */
@Composable
private fun SwitchTextContent(text: String) {
    val typography = YamalTheme.typography.footnote
    val textColor = YamalTheme.colors.neutralColors.background
    Text(text, style = typography, color = textColor)
}

// Previews

@Preview
@Composable
private fun YamalSwitchBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
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
                    YamalSwitch(
                        checked = checked1,
                        onCheckedChange = { checked1 = it },
                    )
                    YamalSwitch(
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
private fun YamalSwitchSizesPreview() {
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
                    YamalSwitch(
                        checked = true,
                        onCheckedChange = {},
                        size = YamalSwitchSize.Default,
                    )
                    YamalSwitch(
                        checked = true,
                        onCheckedChange = {},
                        size = YamalSwitchSize.Small,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalSwitchStatesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Disabled & Loading", style = YamalTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    YamalSwitch(
                        checked = false,
                        onCheckedChange = {},
                        enabled = false,
                    )
                    YamalSwitch(
                        checked = true,
                        onCheckedChange = {},
                        enabled = false,
                    )
                    YamalSwitch(
                        checked = false,
                        onCheckedChange = {},
                        loading = true,
                    )
                    YamalSwitch(
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
private fun YamalSwitchWithTextPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                var checked by remember { mutableStateOf(true) }

                Text("With Text", style = YamalTheme.typography.bodyMedium)
                YamalSwitch(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    checkedContent = { SwitchTextContent("1") },
                    uncheckedContent = { SwitchTextContent("0") },
                )
            }
        }
    }
}
