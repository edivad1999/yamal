package com.yamal.designSystem.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
class YamalColors(
    val neutralColors: NeutralColors,
    val paletteColors: PaletteColors,
    val functionalColors: FunctionalColors,
) {
    @Immutable
    class PaletteColors(
        val color1: Color,
        val color2: Color,
        val color3: Color,
        val color4: Color,
        val color5: Color,
        val color6: Color,
        val color7: Color,
        val color8: Color,
        val color9: Color,
        val color10: Color,
    ) {
        companion object {
            private val brandColor = Color(0xFFa0d911)

            fun build(colors: NeutralColors) = YamalPaletteColorBuilder(brandColor, colors.background).generate()
        }
    }

    @Immutable
    class FunctionalColors(
        val success: Color,
        val warning: Color,
        val error: Color,
    ) {
        companion object {
            private val warningColor = Color(0xFFfaad14)

            private val errorColor = Color(0xFFfa541c)

            fun build(
                paletteColors: PaletteColors,
                neutralColors: NeutralColors,
            ) = FunctionalColors(
                success = paletteColors.color6,
                warning = YamalPaletteColorBuilder(warningColor, neutralColors.background).generate().color6,
                error = YamalPaletteColorBuilder(errorColor, neutralColors.background).generate().color6,
            )
        }
    }

    class NeutralColors(
        val title: Color,
        val primaryText: Color,
        val secondaryText: Color,
        val disableText: Color,
        val border: Color,
        val divider: Color,
        val background: Color,
        val tableHeader: Color,
    ) {
        companion object {
            private val palette =
                listOf(
                    "#000000",
                    "#141414",
                    "#1f1f1f",
                    "#262626",
                    "#434343",
                    "#595959",
                    "#8c8c8c",
                    "#bfbfbf",
                    "#d9d9d9",
                    "#f0f0f0",
                    "#f5f5f5",
                    "#fafafa",
                    "#ffffff",
                )

            fun dark(): NeutralColors = NeutralColorsFactory.fromPalette(palette, isDark = true)

            fun light(): NeutralColors = NeutralColorsFactory.fromPalette(palette, isDark = false)
        }
    }

    companion object
}

fun YamalColors.Companion.dark(): YamalColors {
    val neutralColors = YamalColors.NeutralColors.dark()
    val paletteColors = YamalColors.PaletteColors.build(neutralColors)
    val functionalColors = YamalColors.FunctionalColors.build(paletteColors, neutralColors)
    return YamalColors(
        neutralColors = neutralColors,
        paletteColors = paletteColors,
        functionalColors = functionalColors,
    )
}

fun YamalColors.Companion.light(): YamalColors {
    val neutralColors = YamalColors.NeutralColors.light()
    val paletteColors = YamalColors.PaletteColors.build(neutralColors)
    val functionalColors = YamalColors.FunctionalColors.build(paletteColors, neutralColors)
    return YamalColors(
        neutralColors = neutralColors,
        paletteColors = paletteColors,
        functionalColors = functionalColors,
    )
}

fun YamalColors.toColors(isDark: Boolean): Colors =
    Colors(
        primary = paletteColors.color6,
        primaryVariant = paletteColors.color7,
        secondary = paletteColors.color6,
        secondaryVariant = paletteColors.color7,
        background = neutralColors.background,
        surface = neutralColors.background,
        error = functionalColors.error,
        onPrimary = neutralColors.primaryText,
        onSecondary = neutralColors.secondaryText,
        onBackground = neutralColors.primaryText,
        onSurface = neutralColors.primaryText,
        onError = neutralColors.title,
        isLight = !isDark,
    )
