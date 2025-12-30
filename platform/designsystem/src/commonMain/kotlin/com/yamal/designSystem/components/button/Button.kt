package com.yamal.designSystem.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Button colors following Ant Design Mobile guidelines:
 * - Default: Standard button without emphasis
 * - Primary: Main action button, indicates the primary operation
 * - Success: Indicates a successful or positive action
 * - Warning: Indicates a warning that might need attention
 * - Danger: Indicates a dangerous or potentially destructive action
 */
@Immutable
enum class ButtonColor {
    Default,
    Primary,
    Success,
    Warning,
    Danger,
}

/**
 * Button fill styles following Ant Design Mobile guidelines:
 * - Solid: Filled background with contrasting text
 * - Outline: Transparent background with colored border and text
 * - None: No background or border, just colored text
 */
@Immutable
enum class ButtonFill {
    Solid,
    Outline,
    None,
}

/**
 * Button sizes following Ant Design Mobile guidelines:
 * - Large: 48dp height, for prominent actions
 * - Middle: 40dp height (default)
 * - Small: 32dp height
 * - Mini: 24dp height, for compact UIs
 */
@Immutable
enum class ButtonSize {
    Large,
    Middle,
    Small,
    Mini,
}

/**
 * Button shapes following Ant Design Mobile guidelines:
 * - Default: Slightly rounded corners (4dp)
 * - Rounded: Fully rounded pill shape
 * - Rectangular: Sharp corners (0dp radius)
 */
@Immutable
enum class ButtonShape {
    Default,
    Rounded,
    Rectangular,
}

/**
 * Button component following Ant Design Mobile specifications.
 *
 * @param onClick Callback invoked when the button is clicked
 * @param modifier Modifier for the button
 * @param color The semantic color of the button (default, primary, success, warning, danger)
 * @param fill The fill style of the button (solid, outline, none)
 * @param size The size of the button (large, middle, small, mini)
 * @param shape The shape of the button (default, rounded, rectangular)
 * @param block Whether the button should take full width
 * @param loading Whether to show loading state
 * @param loadingText Optional text to show during loading state
 * @param loadingIcon Optional composable for custom loading indicator
 * @param disabled Whether the button is disabled
 * @param content The button content
 */
@Composable
fun YamalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: ButtonColor = ButtonColor.Default,
    fill: ButtonFill = ButtonFill.Solid,
    size: ButtonSize = ButtonSize.Middle,
    shape: ButtonShape = ButtonShape.Default,
    block: Boolean = false,
    loading: Boolean = false,
    loadingText: String? = null,
    loadingIcon: (@Composable () -> Unit)? = null,
    disabled: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = YamalTheme.colors

    val height =
        when (size) {
            ButtonSize.Mini -> 24.dp
            ButtonSize.Small -> 32.dp
            ButtonSize.Middle -> 40.dp
            ButtonSize.Large -> 48.dp
        }

    // ADM: padding: 7px 12px (default), mini/small: 3px 12px, large: 11px 12px
    val contentPadding =
        when (size) {
            ButtonSize.Mini -> PaddingValues(horizontal = 12.dp, vertical = 3.dp)
            ButtonSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 3.dp)
            ButtonSize.Middle -> PaddingValues(horizontal = 12.dp, vertical = 7.dp)
            ButtonSize.Large -> PaddingValues(horizontal = 12.dp, vertical = 11.dp)
        }

    val buttonShape: Shape =
        when (shape) {
            ButtonShape.Default -> RoundedCornerShape(4.dp)
            ButtonShape.Rounded -> RoundedCornerShape(percent = 50)
            ButtonShape.Rectangular -> RoundedCornerShape(0.dp)
        }

    val loadingIndicatorSize =
        when (size) {
            ButtonSize.Mini -> 12.dp
            ButtonSize.Small -> 14.dp
            ButtonSize.Middle -> 16.dp
            ButtonSize.Large -> 18.dp
        }

    // Determine colors based on color and fill
    val semanticColor =
        when (color) {
            ButtonColor.Default -> colors.text
            ButtonColor.Primary -> colors.primary
            ButtonColor.Success -> colors.success
            ButtonColor.Warning -> colors.warning
            ButtonColor.Danger -> colors.danger
        }

    val actuallyEnabled = !disabled && !loading
    val blockModifier = if (block) modifier.fillMaxWidth() else modifier

    @Composable
    fun LoadingContent() {
        if (loadingIcon != null) {
            loadingIcon()
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(loadingIndicatorSize),
                color =
                    when (fill) {
                        ButtonFill.Solid -> {
                            if (color == ButtonColor.Default) {
                                colors.text
                            } else {
                                colors.textLightSolid
                            }
                        }

                        ButtonFill.Outline, ButtonFill.None -> {
                            semanticColor
                        }
                    },
                strokeWidth = 2.dp,
            )
        }
        if (loadingText != null) {
            Spacer(Modifier.width(8.dp))
            Text(text = loadingText)
        }
    }

    when (fill) {
        ButtonFill.Solid -> {
            Button(
                onClick = onClick,
                modifier = blockModifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                colors =
                    ButtonDefaults.buttonColors(
                        backgroundColor =
                            if (color == ButtonColor.Default) {
                                colors.background
                            } else {
                                semanticColor
                            },
                        contentColor =
                            if (color == ButtonColor.Default) {
                                colors.text
                            } else {
                                colors.textLightSolid
                            },
                        disabledBackgroundColor = colors.weak.copy(alpha = 0.3f),
                        disabledContentColor = colors.weak,
                    ),
                border =
                    if (color == ButtonColor.Default) {
                        BorderStroke(1.dp, colors.border)
                    } else {
                        null
                    },
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    LoadingContent()
                    if (loadingText == null) {
                        Spacer(Modifier.width(8.dp))
                        content()
                    }
                } else {
                    content()
                }
            }
        }

        ButtonFill.Outline -> {
            OutlinedButton(
                onClick = onClick,
                modifier = blockModifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = if (actuallyEnabled) semanticColor else colors.weak,
                    ),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = semanticColor,
                        disabledContentColor = colors.weak,
                    ),
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    LoadingContent()
                    if (loadingText == null) {
                        Spacer(Modifier.width(8.dp))
                        content()
                    }
                } else {
                    content()
                }
            }
        }

        ButtonFill.None -> {
            TextButton(
                onClick = onClick,
                modifier = blockModifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                colors =
                    ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = semanticColor,
                        disabledContentColor = colors.weak,
                    ),
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    LoadingContent()
                    if (loadingText == null) {
                        Spacer(Modifier.width(8.dp))
                        content()
                    }
                } else {
                    content()
                }
            }
        }
    }
}

/**
 * Convenience overload for text-only buttons
 */
@Composable
fun YamalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: ButtonColor = ButtonColor.Default,
    fill: ButtonFill = ButtonFill.Solid,
    size: ButtonSize = ButtonSize.Middle,
    shape: ButtonShape = ButtonShape.Default,
    block: Boolean = false,
    loading: Boolean = false,
    loadingText: String? = null,
    loadingIcon: (@Composable () -> Unit)? = null,
    disabled: Boolean = false,
) {
    // ADM font sizes: mini=13sp (font-size-5), small=15sp (font-size-7),
    // middle=17sp (font-size-9), large=18sp (font-size-10)
    val textStyle =
        when (size) {
            ButtonSize.Mini -> TextStyle(fontSize = 13.sp)
            ButtonSize.Small -> TextStyle(fontSize = 15.sp)
            ButtonSize.Middle -> TextStyle(fontSize = 17.sp)
            ButtonSize.Large -> TextStyle(fontSize = 18.sp)
        }

    YamalButton(
        onClick = onClick,
        modifier = modifier,
        color = color,
        fill = fill,
        size = size,
        shape = shape,
        block = block,
        loading = loading,
        loadingText = loadingText,
        loadingIcon = loadingIcon,
        disabled = disabled,
    ) {
        Text(text = text, style = textStyle)
    }
}

// Previews

@Preview
@Composable
private fun ButtonColorsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
                    YamalButton(text = "Default", color = ButtonColor.Default, onClick = {})
                    YamalButton(text = "Primary", color = ButtonColor.Primary, onClick = {})
                    YamalButton(text = "Success", color = ButtonColor.Success, onClick = {})
                }
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
                    YamalButton(text = "Warning", color = ButtonColor.Warning, onClick = {})
                    YamalButton(text = "Danger", color = ButtonColor.Danger, onClick = {})
                }
            }
        }
    }
}

@Preview
@Composable
private fun ButtonFillsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
                    YamalButton(
                        text = "Solid",
                        color = ButtonColor.Primary,
                        fill = ButtonFill.Solid,
                        onClick = {},
                    )
                    YamalButton(
                        text = "Outline",
                        color = ButtonColor.Primary,
                        fill = ButtonFill.Outline,
                        onClick = {},
                    )
                    YamalButton(
                        text = "None",
                        color = ButtonColor.Primary,
                        fill = ButtonFill.None,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ButtonSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(Dimension.Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YamalButton(text = "Large", size = ButtonSize.Large, onClick = {})
                YamalButton(text = "Middle", size = ButtonSize.Middle, onClick = {})
                YamalButton(text = "Small", size = ButtonSize.Small, onClick = {})
                YamalButton(text = "Mini", size = ButtonSize.Mini, onClick = {})
            }
        }
    }
}

@Preview
@Composable
private fun ButtonShapesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(Dimension.Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YamalButton(text = "Default", shape = ButtonShape.Default, onClick = {})
                YamalButton(text = "Rounded", shape = ButtonShape.Rounded, onClick = {})
                YamalButton(text = "Rectangular", shape = ButtonShape.Rectangular, onClick = {})
            }
        }
    }
}

@Preview
@Composable
private fun ButtonStatesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
                    YamalButton(
                        text = "Loading",
                        color = ButtonColor.Primary,
                        loading = true,
                        onClick = {},
                    )
                    YamalButton(
                        text = "Loading",
                        color = ButtonColor.Primary,
                        loading = true,
                        loadingText = "Submitting...",
                        onClick = {},
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
                    YamalButton(text = "Disabled", disabled = true, onClick = {})
                    YamalButton(
                        text = "Disabled",
                        color = ButtonColor.Primary,
                        disabled = true,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ButtonBlockPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                YamalButton(
                    text = "Block Button",
                    color = ButtonColor.Primary,
                    block = true,
                    onClick = {},
                )
                YamalButton(
                    text = "Block Outline",
                    color = ButtonColor.Primary,
                    fill = ButtonFill.Outline,
                    block = true,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun ButtonWithIconPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(Dimension.Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YamalButton(color = ButtonColor.Primary, onClick = {}) {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Search")
                }
                YamalButton(
                    color = ButtonColor.Primary,
                    shape = ButtonShape.Rounded,
                    onClick = {},
                ) {
                    Icon(
                        Icons.Outlined.Plus,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}
