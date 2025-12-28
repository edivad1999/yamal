package com.yamal.designSystem.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.foundation.LocalContentAlpha
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.theme.YamalTheme

/**
 * Icon button following Yamal Design System.
 *
 * IconButton is a clickable icon, used to represent actions. It's typically used
 * in app bars, toolbars, and as standalone actions.
 *
 * @param onClick Callback when the button is clicked
 * @param modifier Modifier for the button
 * @param enabled Whether the button is enabled
 * @param interactionSource The interaction source for this button
 * @param content The icon content, typically an [Icon]
 *
 * Example usage:
 * ```
 * IconButton(onClick = { /* handle click */ }) {
 *     Icon(Icons.Outlined.ArrowLeft, contentDescription = "Back")
 * }
 * ```
 */
@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .clickable(
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = null,
                ),
        contentAlignment = Alignment.Center,
    ) {
        val contentAlpha = if (enabled) 1f else 0.38f
        CompositionLocalProvider(
            LocalContentColor provides YamalTheme.colors.text,
            LocalContentAlpha provides contentAlpha,
            content = content,
        )
    }
}
