package com.yamal.designSystem.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Yamal Design System colors following Ant Design guidelines.
 * https://ant.design/docs/spec/colors
 *
 * The color system consists of:
 * - Neutral colors: For text, backgrounds, borders
 * - Palette colors: 10-color gradient based on brand color
 * - Functional colors: Semantic colors for feedback states
 * - Preset colors: Standard color palettes for components
 */
@Immutable
class YamalColors(
    val neutralColors: NeutralColors,
    val paletteColors: PaletteColors,
    val functionalColors: FunctionalColors,
    val presetColors: PresetColors,
) {
    /**
     * 10-level color palette generated from brand color following Ant Design algorithm.
     * color6 is the primary/brand color, lighter colors (1-5) and darker colors (7-10).
     */
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
            /** Lime green - Yamal brand color */
            private val brandColor = Color(0xFF1161D9)

            fun build(colors: NeutralColors) = YamalPaletteColorBuilder(brandColor, colors.background).generate()
        }
    }

    /**
     * Functional colors for feedback and status following Ant Design guidelines.
     * https://ant.design/docs/spec/colors#functional-color
     */
    @Immutable
    class FunctionalColors(
        val success: Color,
        val successBg: Color,
        val successBorder: Color,
        val warning: Color,
        val warningBg: Color,
        val warningBorder: Color,
        val error: Color,
        val errorBg: Color,
        val errorBorder: Color,
        val info: Color,
        val infoBg: Color,
        val infoBorder: Color,
        val link: Color,
        val linkHover: Color,
        val linkActive: Color,
    ) {
        companion object {
            private val warningColor = Color(0xFFfaad14)
            private val errorColor = Color(0xFFff4d4f)
            private val infoColor = Color(0xFF5CFF16)

            fun build(
                paletteColors: PaletteColors,
                neutralColors: NeutralColors,
            ): FunctionalColors {
                val warningPalette = YamalPaletteColorBuilder(warningColor, neutralColors.background).generate()
                val errorPalette = YamalPaletteColorBuilder(errorColor, neutralColors.background).generate()
                val infoPalette = YamalPaletteColorBuilder(infoColor, neutralColors.background).generate()

                return FunctionalColors(
                    success = paletteColors.color6,
                    successBg = paletteColors.color1,
                    successBorder = paletteColors.color3,
                    warning = warningPalette.color6,
                    warningBg = warningPalette.color1,
                    warningBorder = warningPalette.color3,
                    error = errorPalette.color6,
                    errorBg = errorPalette.color1,
                    errorBorder = errorPalette.color3,
                    info = infoPalette.color6,
                    infoBg = infoPalette.color1,
                    infoBorder = infoPalette.color3,
                    link = infoPalette.color6,
                    linkHover = infoPalette.color5,
                    linkActive = infoPalette.color7,
                )
            }
        }
    }

    /**
     * Preset color palettes for components like Tag, Badge following Ant Design.
     * https://ant.design/docs/spec/colors#preset-colors
     */
    @Immutable
    class PresetColors(
        val red: ColorPalette,
        val volcano: ColorPalette,
        val orange: ColorPalette,
        val gold: ColorPalette,
        val yellow: ColorPalette,
        val lime: ColorPalette,
        val green: ColorPalette,
        val cyan: ColorPalette,
        val blue: ColorPalette,
        val geekblue: ColorPalette,
        val purple: ColorPalette,
        val magenta: ColorPalette,
    ) {
        @Immutable
        class ColorPalette(
            val color: Color,
            val bg: Color,
            val border: Color,
        )

        companion object {
            fun build(neutralColors: NeutralColors): PresetColors {
                fun palette(baseColor: Color): ColorPalette {
                    val p = YamalPaletteColorBuilder(baseColor, neutralColors.background).generate()
                    return ColorPalette(color = p.color6, bg = p.color1, border = p.color3)
                }

                return PresetColors(
                    red = palette(Color(0xFFf5222d)),
                    volcano = palette(Color(0xFFfa541c)),
                    orange = palette(Color(0xFFfa8c16)),
                    gold = palette(Color(0xFFfaad14)),
                    yellow = palette(Color(0xFFfadb14)),
                    lime = palette(Color(0xFFa0d911)),
                    green = palette(Color(0xFF52c41a)),
                    cyan = palette(Color(0xFF13c2c2)),
                    blue = palette(Color(0xFF1677ff)),
                    geekblue = palette(Color(0xFF2f54eb)),
                    purple = palette(Color(0xFF722ed1)),
                    magenta = palette(Color(0xFFeb2f96)),
                )
            }
        }
    }

    /**
     * Neutral colors for text, backgrounds, and borders following Ant Design.
     * https://ant.design/docs/spec/colors#neutral-color
     */
    class NeutralColors(
        val title: Color,
        val primaryText: Color,
        val secondaryText: Color,
        val disableText: Color,
        val border: Color,
        val divider: Color,
        val background: Color,
        val tableHeader: Color,
        val containerBg: Color,
        val fill: Color,
        val fillSecondary: Color,
        val fillTertiary: Color,
        val fillQuaternary: Color,
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
    val presetColors = YamalColors.PresetColors.build(neutralColors)
    return YamalColors(
        neutralColors = neutralColors,
        paletteColors = paletteColors,
        functionalColors = functionalColors,
        presetColors = presetColors,
    )
}

fun YamalColors.Companion.light(): YamalColors {
    val neutralColors = YamalColors.NeutralColors.light()
    val paletteColors = YamalColors.PaletteColors.build(neutralColors)
    val functionalColors = YamalColors.FunctionalColors.build(paletteColors, neutralColors)
    val presetColors = YamalColors.PresetColors.build(neutralColors)
    return YamalColors(
        neutralColors = neutralColors,
        paletteColors = paletteColors,
        functionalColors = functionalColors,
        presetColors = presetColors,
    )
}

fun YamalColors.toColors(isDark: Boolean): Colors =
    Colors(
        primary = paletteColors.color6,
        primaryVariant = paletteColors.color7,
        secondary = paletteColors.color5,
        secondaryVariant = paletteColors.color7,
        background = neutralColors.background,
        surface = neutralColors.background,
        error = functionalColors.error,
        onPrimary = if (isDark) neutralColors.background else Color.White,
        onSecondary = if (isDark) neutralColors.background else Color.White,
        onBackground = neutralColors.primaryText,
        onSurface = neutralColors.primaryText,
        onError = if (isDark) neutralColors.background else Color.White,
        isLight = !isDark,
    )
