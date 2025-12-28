package com.yamal.designSystem.components.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.theme.YamalTheme

/**
 * Surface is a basic building block for components following Yamal Design System.
 *
 * It provides a background, shape, border, and content color configuration.
 *
 * @param modifier Modifier to be applied to the surface.
 * @param shape Defines the shape of the surface.
 * @param color The background color.
 * @param contentColor The preferred content color for children.
 * @param border Optional border to draw around the surface.
 * @param elevation The elevation of the surface (shadow).
 * @param content The content to display inside the surface.
 */
@Composable
@NonRestartableComposable
fun Surface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = YamalTheme.colors.background,
    contentColor: Color = YamalTheme.colors.text,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier =
                modifier
                    .surface(
                        shape = shape,
                        backgroundColor = color,
                        border = border,
                        elevation = elevation,
                    ).semantics(mergeDescendants = false) {
                        isTraversalGroup = true
                    }.pointerInput(Unit) {},
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}

/**
 * Clickable Surface variant.
 *
 * @param onClick Callback when the surface is clicked.
 * @param modifier Modifier to be applied to the surface.
 * @param enabled Whether the surface is enabled for clicks.
 * @param shape Defines the shape of the surface.
 * @param color The background color.
 * @param contentColor The preferred content color for children.
 * @param border Optional border to draw around the surface.
 * @param elevation The elevation of the surface (shadow).
 * @param interactionSource The interaction source for this clickable surface.
 * @param content The content to display inside the surface.
 */
@Composable
@NonRestartableComposable
fun Surface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = YamalTheme.colors.background,
    contentColor: Color = YamalTheme.colors.text,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier =
                modifier
                    .surface(
                        shape = shape,
                        backgroundColor = color,
                        border = border,
                        elevation = elevation,
                    ).clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        onClick = onClick,
                    ),
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    elevation: Dp,
): Modifier =
    this
        .then(if (elevation > 0.dp) Modifier.shadow(elevation, shape, clip = false) else Modifier)
        .then(if (border != null) Modifier.border(border, shape) else Modifier)
        .background(color = backgroundColor, shape = shape)
        .clip(shape)
