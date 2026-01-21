package com.yamal.designSystem.components.notificationBanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonShape
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.IconButton
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Notification banner component for promotional or informational messages.
 *
 * Displays a styled banner with an icon, title, description, and action button.
 * Typically used for unauthenticated user prompts or feature announcements.
 *
 * Example usage:
 * ```
 * NotificationBanner(
 *     icon = Icons.Outlined.Notification,
 *     title = "Never miss an episode!",
 *     description = "Log in to create your watchlist...",
 *     actionText = "Sign Up Free",
 *     onActionClick = { /* navigate to login */ },
 *     onDismiss = { /* hide banner */ }
 * )
 * ```
 *
 * @param icon The icon to display in the banner
 * @param title The main title text
 * @param description The descriptive text
 * @param actionText The action button text
 * @param modifier Modifier for the banner
 * @param onActionClick Callback when action button is clicked
 * @param onDismiss Optional callback when dismiss button is clicked
 */
@Composable
fun NotificationBanner(
    icon: com.yamal.designSystem.icons.IconPainter,
    title: String,
    description: String,
    actionText: String,
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        // Decorative background blobs
        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 24.dp, y = (-24).dp)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(YamalTheme.colors.primary.copy(alpha = 0.2f))
                    .blur(32.dp),
        )

        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-24).dp, y = 24.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3B82F6).copy(alpha = 0.2f))
                    .blur(24.dp),
        )

        // Main content
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = YamalTheme.colors.box,
            border = androidx.compose.foundation.BorderStroke(1.dp, YamalTheme.colors.border),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                // Icon
                Box(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(YamalTheme.colors.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        icon = icon,
                        contentDescription = null,
                        tint = YamalTheme.colors.primary,
                        modifier = Modifier.size(24.dp),
                    )
                }

                // Content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = title,
                        style = YamalTheme.typography.title,
                        color = YamalTheme.colors.text,
                    )

                    Text(
                        text = description,
                        style = YamalTheme.typography.body,
                        color = YamalTheme.colors.textSecondary,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    YamalButton(
                        text = actionText,
                        onClick = onActionClick,
                        color = ButtonColor.Primary,
                        fill = ButtonFill.Solid,
                        size = ButtonSize.Small,
                        shape = ButtonShape.Default,
                    )
                }

                // Dismiss button
                if (onDismiss != null) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            icon = Icons.Outlined.Close,
                            contentDescription = "Dismiss",
                            tint = YamalTheme.colors.textSecondary,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun NotificationBannerPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            NotificationBanner(
                icon = Icons.Outlined.Notification,
                title = "Never miss an episode!",
                description = "Log in to create your watchlist and get notified when new episodes air.",
                actionText = "Sign Up Free",
                onActionClick = {},
                onDismiss = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun NotificationBannerNoDismissPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            NotificationBanner(
                icon = Icons.Filled.Bell,
                title = "New feature available!",
                description = "Check out our brand new recommendations engine.",
                actionText = "Learn More",
                onActionClick = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
