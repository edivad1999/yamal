package com.yamal.designSystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.yamal.designSystem.foundation.LocalContentColor

/**
 * Yamal Design System theme following Ant Design Mobile guidelines.
 *
 * @param isDark Whether to use dark theme
 * @param brandColor Optional custom brand color for theming
 * @param shapes Shape definitions for components
 * @param content The composable content
 */
@Composable
fun YamalTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    brandColor: Color = YamalColors.DefaultBrandColor,
    shapes: Shapes = Shapes(),
    content: @Composable () -> Unit,
) {
    val colors: YamalColors =
        remember(isDark, brandColor) {
            if (isDark) YamalColors.dark(brandColor) else YamalColors.light(brandColor)
        }
    val typography: YamalTypography = remember { YamalTypography() }

    CompositionLocalProvider(
        LocalTextSelectionColors provides
            TextSelectionColors(
                handleColor = colors.primary,
                backgroundColor = colors.primary.copy(alpha = 0.4f),
            ),
        LocalContentAlpha provides 1f,
        LocalContentColor provides colors.text,
        LocalYamalColors provides colors,
        LocalYamalTypography provides typography,
    ) {
        MaterialTheme(
            colors = colors.toMaterialColors(isDark),
            typography = typography.toMaterialTypography(),
            shapes = shapes,
            content = content,
        )
    }
}

/**
 * Object providing access to current theme values.
 */
object YamalTheme {
    val colors: YamalColors
        @Composable @ReadOnlyComposable
        get() = LocalYamalColors.current

    val typography: YamalTypography
        @Composable @ReadOnlyComposable
        get() = LocalYamalTypography.current
}
