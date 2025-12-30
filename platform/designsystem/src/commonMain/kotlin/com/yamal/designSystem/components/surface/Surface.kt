package com.yamal.designSystem.components.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
@OptIn(ExperimentalMaterialApi::class)
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
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        content = content,
    )
}
