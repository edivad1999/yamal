package com.yamal.designSystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Button types following Ant Design guidelines:
 * - Primary: Main action button, only one per operation area
 * - Default: For actions without priority distinction
 * - Dashed: Commonly used for add operations
 * - Text: For lowest priority actions
 * - Link: For navigation/links
 */
@Immutable
enum class YamalButtonType {
    Primary,
    Default,
    Dashed,
    Text,
    Link,
}

/**
 * Button sizes following Ant Design guidelines:
 * - Large: 40dp height
 * - Middle: 32dp height (default)
 * - Small: 24dp height
 */
@Immutable
enum class YamalButtonSize {
    Large,
    Middle,
    Small,
}

/**
 * Button shapes following Ant Design guidelines:
 * - Default: Slightly rounded corners (6dp)
 * - Round: Fully rounded pill shape
 * - Circle: Perfect circle for icon-only buttons
 */
@Immutable
enum class YamalButtonShape {
    Default,
    Round,
    Circle,
}

@Composable
fun YamalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: YamalButtonType = YamalButtonType.Default,
    size: YamalButtonSize = YamalButtonSize.Middle,
    shape: YamalButtonShape = YamalButtonShape.Default,
    danger: Boolean = false,
    ghost: Boolean = false,
    loading: Boolean = false,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = YamalTheme.colors
    val height =
        when (size) {
            YamalButtonSize.Small -> 24.dp
            YamalButtonSize.Middle -> 32.dp
            YamalButtonSize.Large -> 40.dp
        }
    val contentPadding =
        when (size) {
            YamalButtonSize.Small -> PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            YamalButtonSize.Middle -> PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            YamalButtonSize.Large -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        }
    val buttonShape: Shape =
        when (shape) {
            YamalButtonShape.Default -> RoundedCornerShape(6.dp)
            YamalButtonShape.Round -> RoundedCornerShape(percent = 50)
            YamalButtonShape.Circle -> CircleShape
        }

    // Determine colors based on type, danger, and ghost states
    val primaryColor = if (danger) colors.functionalColors.error else colors.paletteColors.color6
    val primaryHoverColor = if (danger) colors.functionalColors.error else colors.paletteColors.color5

    val actuallyEnabled = enabled && !loading

    when (type) {
        YamalButtonType.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                colors =
                    ButtonDefaults.buttonColors(
                        backgroundColor = if (ghost) Color.Transparent else primaryColor,
                        contentColor = if (ghost) primaryColor else colors.neutralColors.background,
                        disabledBackgroundColor = if (ghost) Color.Transparent else colors.neutralColors.disableText,
                        disabledContentColor = colors.neutralColors.secondaryText,
                    ),
                border = if (ghost) BorderStroke(1.dp, primaryColor) else null,
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = if (ghost) primaryColor else colors.neutralColors.background,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                content()
            }
        }

        YamalButtonType.Default -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                border =
                    BorderStroke(
                        width = 1.dp,
                        color =
                            if (actuallyEnabled) {
                                if (danger) primaryColor else colors.neutralColors.border
                            } else {
                                colors.neutralColors.disableText
                            },
                    ),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = if (ghost) Color.Transparent else colors.neutralColors.background,
                        contentColor = if (danger) primaryColor else colors.neutralColors.primaryText,
                        disabledContentColor = colors.neutralColors.disableText,
                    ),
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = if (danger) primaryColor else colors.neutralColors.primaryText,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                content()
            }
        }

        YamalButtonType.Dashed -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                border =
                    BorderStroke(
                        width = 1.dp,
                        color =
                            if (actuallyEnabled) {
                                if (danger) primaryColor else colors.neutralColors.border
                            } else {
                                colors.neutralColors.disableText
                            },
                    ),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = if (ghost) Color.Transparent else colors.neutralColors.background,
                        contentColor = if (danger) primaryColor else colors.neutralColors.primaryText,
                        disabledContentColor = colors.neutralColors.disableText,
                    ),
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = if (danger) primaryColor else colors.neutralColors.primaryText,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                content()
            }
        }

        YamalButtonType.Text -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                colors =
                    ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = if (danger) primaryColor else colors.neutralColors.primaryText,
                        disabledContentColor = colors.neutralColors.disableText,
                    ),
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = if (danger) primaryColor else colors.neutralColors.primaryText,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                content()
            }
        }

        YamalButtonType.Link -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = actuallyEnabled,
                shape = buttonShape,
                colors =
                    ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = if (danger) primaryColor else colors.paletteColors.color6,
                        disabledContentColor = colors.neutralColors.disableText,
                    ),
                contentPadding = contentPadding,
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = if (danger) primaryColor else colors.paletteColors.color6,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                content()
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
    type: YamalButtonType = YamalButtonType.Default,
    size: YamalButtonSize = YamalButtonSize.Middle,
    shape: YamalButtonShape = YamalButtonShape.Default,
    danger: Boolean = false,
    ghost: Boolean = false,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    val typography = YamalTheme.typography
    val textStyle =
        when (size) {
            YamalButtonSize.Small -> typography.footnote
            YamalButtonSize.Middle -> typography.body
            YamalButtonSize.Large -> typography.body
        }

    YamalButton(
        onClick = onClick,
        modifier = modifier,
        type = type,
        size = size,
        shape = shape,
        danger = danger,
        ghost = ghost,
        loading = loading,
        enabled = enabled,
    ) {
        Text(text = text, style = textStyle)
    }
}

// Previews

@Preview
@Composable
private fun YamalButtonTypesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
                    YamalButton(text = "Primary", type = YamalButtonType.Primary, onClick = {})
                    YamalButton(text = "Default", type = YamalButtonType.Default, onClick = {})
                    YamalButton(text = "Dashed", type = YamalButtonType.Dashed, onClick = {})
                }
                Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
                    YamalButton(text = "Text", type = YamalButtonType.Text, onClick = {})
                    YamalButton(text = "Link", type = YamalButtonType.Link, onClick = {})
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalButtonSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(Dimension.Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YamalButton(text = "Large", size = YamalButtonSize.Large, onClick = {})
                YamalButton(text = "Middle", size = YamalButtonSize.Middle, onClick = {})
                YamalButton(text = "Small", size = YamalButtonSize.Small, onClick = {})
            }
        }
    }
}

@Preview
@Composable
private fun YamalButtonDangerGhostPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(Dimension.Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                YamalButton(text = "Danger", danger = true, onClick = {})
                YamalButton(text = "Ghost", ghost = true, onClick = {})
                YamalButton(text = "Loading", loading = true, onClick = {})
                YamalButton(text = "Disabled", enabled = false, onClick = {})
            }
        }
    }
}

@Preview
@Composable
private fun YamalButtonShapesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(Dimension.Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YamalButton(text = "Default", shape = YamalButtonShape.Default, onClick = {})
                YamalButton(text = "Round", shape = YamalButtonShape.Round, onClick = {})
                YamalButton(shape = YamalButtonShape.Circle, onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                }
                YamalButton(shape = YamalButtonShape.Circle, type = YamalButtonType.Primary, onClick = {}) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
