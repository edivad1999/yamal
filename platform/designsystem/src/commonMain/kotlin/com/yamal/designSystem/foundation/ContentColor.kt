package com.yamal.designSystem.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.yamal.designSystem.theme.YamalTheme

/**
 * CompositionLocal containing the preferred content color for a given position in the hierarchy.
 * Defaults to [YamalTheme.colors.text].
 *
 * This is used by components like [Text] to set a default color.
 */
val LocalContentColor = compositionLocalOf { Color.Unspecified }

/**
 * CompositionLocal containing the preferred content alpha for a given position in the hierarchy.
 * Used to apply emphasis/de-emphasis to content.
 */
val LocalContentAlpha = compositionLocalOf { 1f }

/**
 * Retrieves the current content color with alpha applied.
 */
val currentContentColor: Color
    @Composable
    get() = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)

/**
 * Sets the content color for the children.
 */
@Composable
fun ProvideContentColor(
    color: Color,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides color, content = content)
}

/**
 * Sets the content alpha for the children.
 */
@Composable
fun ProvideContentAlpha(
    alpha: Float,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentAlpha provides alpha, content = content)
}

/**
 * Sets both content color and alpha for the children.
 */
@Composable
fun ProvideContentColorAndAlpha(
    color: Color,
    alpha: Float = 1f,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides color,
        LocalContentAlpha provides alpha,
        content = content,
    )
}
