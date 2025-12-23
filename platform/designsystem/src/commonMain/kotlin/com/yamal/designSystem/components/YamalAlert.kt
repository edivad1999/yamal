package com.yamal.designSystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Alert types following Ant Design guidelines.
 */
@Immutable
enum class YamalAlertType {
    Success,
    Info,
    Warning,
    Error,
}

/**
 * An alert component following Ant Design guidelines.
 * Used for displaying important messages to users.
 *
 * @param message The main message to display
 * @param modifier Modifier for the alert
 * @param type Type of alert (success, info, warning, error)
 * @param description Optional description text
 * @param showIcon Whether to show the type icon
 * @param closable Whether the alert can be closed
 * @param onClose Callback when alert is closed
 * @param icon Custom icon to display
 * @param action Optional action content (e.g., a button)
 * @param banner Whether to display as a banner (no border, no icon by default)
 */
@Composable
fun YamalAlert(
    message: String,
    modifier: Modifier = Modifier,
    type: YamalAlertType = YamalAlertType.Info,
    description: String? = null,
    showIcon: Boolean = false,
    closable: Boolean = false,
    onClose: (() -> Unit)? = null,
    icon: ImageVector? = null,
    action: @Composable (() -> Unit)? = null,
    banner: Boolean = false,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    // Get colors based on type
    val (backgroundColor, borderColor, iconColor) =
        when (type) {
            YamalAlertType.Success -> {
                Triple(
                    colors.functionalColors.successBg,
                    colors.functionalColors.successBorder,
                    colors.functionalColors.success,
                )
            }

            YamalAlertType.Info -> {
                Triple(
                    colors.functionalColors.infoBg,
                    colors.functionalColors.infoBorder,
                    colors.functionalColors.info,
                )
            }

            YamalAlertType.Warning -> {
                Triple(
                    colors.functionalColors.warningBg,
                    colors.functionalColors.warningBorder,
                    colors.functionalColors.warning,
                )
            }

            YamalAlertType.Error -> {
                Triple(
                    colors.functionalColors.errorBg,
                    colors.functionalColors.errorBorder,
                    colors.functionalColors.error,
                )
            }
        }

    // Default icon based on type
    val defaultIcon =
        when (type) {
            YamalAlertType.Success -> Icons.Default.CheckCircle
            YamalAlertType.Info -> Icons.Default.Info
            YamalAlertType.Warning -> Icons.Default.Warning
            YamalAlertType.Error -> Icons.Default.Warning
        }

    val actualIcon = icon ?: defaultIcon
    val actualShowIcon = if (banner) showIcon else showIcon
    val hasDescription = description != null

    val shape = if (banner) RoundedCornerShape(0.dp) else RoundedCornerShape(8.dp)
    val iconSize = if (hasDescription) 24.dp else 16.dp

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(backgroundColor)
                .then(
                    if (banner) Modifier else Modifier.border(1.dp, borderColor, shape),
                ).padding(
                    horizontal = Dimension.Spacing.md,
                    vertical = if (hasDescription) Dimension.Spacing.md else Dimension.Spacing.sm,
                ),
    ) {
        Row(
            verticalAlignment = if (hasDescription) Alignment.Top else Alignment.CenterVertically,
        ) {
            if (actualShowIcon) {
                Icon(
                    imageVector = actualIcon,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(iconSize)
                            .padding(top = if (hasDescription) 2.dp else 0.dp),
                    tint = iconColor,
                )
                Spacer(Modifier.width(Dimension.Spacing.sm))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.xxs),
            ) {
                Text(
                    text = message,
                    style = if (hasDescription) typography.bodyMedium else typography.body,
                    color = colors.neutralColors.primaryText,
                )

                if (description != null) {
                    Text(
                        text = description,
                        style = typography.body,
                        color = colors.neutralColors.secondaryText,
                    )
                }
            }

            if (action != null) {
                Spacer(Modifier.width(Dimension.Spacing.sm))
                action()
            }

            if (closable) {
                Spacer(Modifier.width(Dimension.Spacing.sm))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier =
                        Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(
                                onClick = { onClose?.invoke() },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ),
                    tint = colors.neutralColors.secondaryText,
                )
            }
        }
    }
}

/**
 * An alert with visibility animation support.
 */
@Composable
fun YamalAnimatedAlert(
    visible: Boolean,
    message: String,
    modifier: Modifier = Modifier,
    type: YamalAlertType = YamalAlertType.Info,
    description: String? = null,
    showIcon: Boolean = false,
    closable: Boolean = false,
    onClose: (() -> Unit)? = null,
    icon: ImageVector? = null,
    action: @Composable (() -> Unit)? = null,
    banner: Boolean = false,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
    ) {
        YamalAlert(
            message = message,
            modifier = modifier,
            type = type,
            description = description,
            showIcon = showIcon,
            closable = closable,
            onClose = onClose,
            icon = icon,
            action = action,
            banner = banner,
        )
    }
}

// Previews

@Preview
@Composable
private fun YamalAlertTypesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text("Alert Types", style = YamalTheme.typography.bodyMedium)
                YamalAlert(message = "Success Tips", type = YamalAlertType.Success)
                YamalAlert(message = "Informational Notes", type = YamalAlertType.Info)
                YamalAlert(message = "Warning", type = YamalAlertType.Warning)
                YamalAlert(message = "Error", type = YamalAlertType.Error)
            }
        }
    }
}

@Preview
@Composable
private fun YamalAlertWithIconPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text("With Icon", style = YamalTheme.typography.bodyMedium)
                YamalAlert(
                    message = "Success Tips",
                    type = YamalAlertType.Success,
                    showIcon = true,
                )
                YamalAlert(
                    message = "Informational Notes",
                    type = YamalAlertType.Info,
                    showIcon = true,
                )
                YamalAlert(
                    message = "Warning",
                    type = YamalAlertType.Warning,
                    showIcon = true,
                )
                YamalAlert(
                    message = "Error",
                    type = YamalAlertType.Error,
                    showIcon = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalAlertWithDescriptionPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text("With Description", style = YamalTheme.typography.bodyMedium)
                YamalAlert(
                    message = "Success Tips",
                    description = "Detailed description and advice about successful copy.",
                    type = YamalAlertType.Success,
                    showIcon = true,
                )
                YamalAlert(
                    message = "Error",
                    description = "This is an error message about copy failure.",
                    type = YamalAlertType.Error,
                    showIcon = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalAlertClosablePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                var showAlert1 by remember { mutableStateOf(true) }
                var showAlert2 by remember { mutableStateOf(true) }

                Text("Closable", style = YamalTheme.typography.bodyMedium)

                if (showAlert1) {
                    YamalAlert(
                        message = "Warning Text Warning Text",
                        type = YamalAlertType.Warning,
                        closable = true,
                        onClose = { showAlert1 = false },
                    )
                }

                if (showAlert2) {
                    YamalAlert(
                        message = "Error Text",
                        description = "Error description text.",
                        type = YamalAlertType.Error,
                        showIcon = true,
                        closable = true,
                        onClose = { showAlert2 = false },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalAlertWithActionPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text("With Action", style = YamalTheme.typography.bodyMedium)
                YamalAlert(
                    message = "Success Tips",
                    type = YamalAlertType.Success,
                    showIcon = true,
                    action = {
                        YamalButton(
                            text = "UNDO",
                            type = YamalButtonType.Text,
                            size = YamalButtonSize.Small,
                            onClick = {},
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalAlertBannerPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text(
                    "Banner",
                    style = YamalTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = Dimension.Spacing.md),
                )
                YamalAlert(
                    message = "Warning banner message",
                    type = YamalAlertType.Warning,
                    banner = true,
                    showIcon = true,
                )
            }
        }
    }
}
