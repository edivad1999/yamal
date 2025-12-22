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
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun YamalTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    shapes: Shapes = Shapes(),
    content: @Composable () -> Unit,
) {
    val colors: YamalColors =
        remember(isDark) {
            if (isDark) YamalColors.dark() else YamalColors.light()
        }
    val typography: YamalTypography = remember { YamalTypography() }

    CompositionLocalProvider(
        LocalTextSelectionColors provides
            TextSelectionColors(
                handleColor = colors.paletteColors.color6,
                backgroundColor = colors.paletteColors.color6.copy(.4f),
            ),
        LocalContentAlpha provides 1f,
        LocalYamalColors provides colors,
        LocalYamalTypography provides typography,
    ) {
        MaterialTheme(
            colors = colors.toColors(isDark),
            typography = typography.toTypography(),
            shapes = shapes,
            content = content,
        )
    }
}

object YamalTheme {
    val colors: YamalColors
        @Composable @ReadOnlyComposable
        get() = LocalYamalColors.current

    val typography: YamalTypography
        @Composable @ReadOnlyComposable
        get() = LocalYamalTypography.current

//    val shapes: Shapes
//        @Composable @ReadOnlyComposable
//        get() = LocalShapes.current
}

val LocalYamalColors =
    staticCompositionLocalOf<YamalColors> {
        error("No YamalColors provided")
    }

val LocalYamalTypography =
    staticCompositionLocalOf<YamalTypography> {
        error("No YamalTypography provided")
    }

// val LocalShapes =
//    staticCompositionLocalOf<Shapes> {
//        error("No Shapes provided")
//    }
