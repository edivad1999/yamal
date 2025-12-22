package com.yamal.designSystem.theme

import androidx.compose.ui.graphics.Color

class YamalPaletteColorBuilder(
    private val color: Color,
    private val background: Color,
    private val isDark: Boolean = false,
) {
    fun generate(): YamalColors.PaletteColors {
        val hsv = color.toHsv()
        val palette = buildPalette(hsv)

        val colors =
            if (isDark) {
                darkColorMap.map { (index, amount) ->
                    mix(background, palette[index], amount / 100f)
                }
            } else {
                palette
            }

        return YamalColors.PaletteColors(
            color1 = colors[0],
            color2 = colors[1],
            color3 = colors[2],
            color4 = colors[3],
            color5 = colors[4],
            color6 = colors[5],
            color7 = colors[6],
            color8 = colors[7],
            color9 = colors[8],
            color10 = colors[9],
        )
    }

    // internal constants and logic remain unchanged
    private val hueStep = 2f
    private val saturationStep = 0.16f
    private val saturationStep2 = 0.05f
    private val brightnessStep1 = 0.05f
    private val brightnessStep2 = 0.15f
    private val lightColorCount = 5
    private val darkColorCount = 4

    private val darkColorMap =
        listOf(
            7 to 15,
            6 to 25,
            5 to 30,
            5 to 45,
            5 to 65,
            5 to 85,
            4 to 90,
            3 to 95,
            2 to 97,
            1 to 98,
        )

    private fun buildPalette(hsv: FloatArray): List<Color> {
        val colors = mutableListOf<Color>()

        fun getHue(
            i: Int,
            light: Boolean,
        ): Float {
            val h =
                if (hsv[0] in 60f..240f) {
                    if (light) hsv[0] - hueStep * i else hsv[0] + hueStep * i
                } else {
                    if (light) hsv[0] + hueStep * i else hsv[0] - hueStep * i
                }
            return (h + 360f) % 360f
        }

        fun getSaturation(
            i: Int,
            light: Boolean,
        ): Float {
            if (hsv[0] == 0f && hsv[1] == 0f) return hsv[1]
            var s =
                if (light) {
                    hsv[1] - saturationStep * i
                } else if (i == darkColorCount) {
                    hsv[1] + saturationStep
                } else {
                    hsv[1] + saturationStep2 * i
                }
            s = s.coerceIn(0.06f, 1f)
            if (light && i == lightColorCount && s > 0.1f) s = 0.1f
            return s
        }

        fun getValue(
            i: Int,
            light: Boolean,
        ): Float {
            val v = if (light) hsv[2] + brightnessStep1 * i else hsv[2] - brightnessStep2 * i
            return v.coerceIn(0f, 1f)
        }

        for (i in lightColorCount downTo 1) {
            colors.add(hsvToColor(getHue(i, true), getSaturation(i, true), getValue(i, true)))
        }

        colors.add(color)

        for (i in 1..darkColorCount) {
            colors.add(hsvToColor(getHue(i, false), getSaturation(i, false), getValue(i, false)))
        }

        return colors
    }

    private fun Color.toHsv(): FloatArray {
        val r = red
        val g = green
        val b = blue
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        val h =
            when {
                delta == 0f -> 0f
                max == r -> (60 * ((g - b) / delta) + 360) % 360
                max == g -> (60 * ((b - r) / delta) + 120) % 360
                else -> (60 * ((r - g) / delta) + 240) % 360
            }

        val s = if (max == 0f) 0f else delta / max
        val v = max
        return floatArrayOf(h, s, v)
    }

    private fun hsvToColor(
        h: Float,
        s: Float,
        v: Float,
    ): Color {
        val c = v * s
        val x = c * (1 - kotlin.math.abs((h / 60) % 2 - 1))
        val m = v - c
        val (r1, g1, b1) =
            when {
                h < 60 -> Triple(c, x, 0f)
                h < 120 -> Triple(x, c, 0f)
                h < 180 -> Triple(0f, c, x)
                h < 240 -> Triple(0f, x, c)
                h < 300 -> Triple(x, 0f, c)
                else -> Triple(c, 0f, x)
            }
        return Color(r1 + m, g1 + m, b1 + m, 1f)
    }

    private fun mix(
        c1: Color,
        c2: Color,
        amount: Float,
    ): Color =
        Color(
            red = (1 - amount) * c1.red + amount * c2.red,
            green = (1 - amount) * c1.green + amount * c2.green,
            blue = (1 - amount) * c1.blue + amount * c2.blue,
            alpha = 1f,
        )
}

/**
 * Factory for creating neutral color palettes following Ant Design guidelines.
 * Uses a 13-level grayscale from pure black to pure white.
 */
object NeutralColorsFactory {
    fun String.toComposeColor(): Color =
        Color(
            removePrefix("#").toLong(16) or 0x00000000FF000000,
        )

    fun fromPalette(
        hexColors: List<String>,
        isDark: Boolean,
    ): YamalColors.NeutralColors {
        val colors = hexColors.map { it.toComposeColor() }

        return if (isDark) {
            YamalColors.NeutralColors(
                title = colors[11], // #fafafa
                primaryText = colors[10], // #f5f5f5
                secondaryText = colors[6], // #8c8c8c
                disableText = colors[5], // #595959
                border = colors[4], // #434343
                divider = colors[3], // #262626
                background = colors[1], // #141414
                tableHeader = colors[2], // #1f1f1f
                containerBg = colors[2], // #1f1f1f
                fill = colors[4], // #434343
                fillSecondary = colors[3], // #262626
                fillTertiary = colors[2], // #1f1f1f
                fillQuaternary = colors[1], // #141414
            )
        } else {
            YamalColors.NeutralColors(
                title = colors[1], // #141414
                primaryText = colors[2], // #1f1f1f
                secondaryText = colors[5], // #595959
                disableText = colors[6], // #8c8c8c
                border = colors[8], // #d9d9d9
                divider = colors[9], // #f0f0f0
                background = colors[12], // #ffffff
                tableHeader = colors[11], // #fafafa
                containerBg = colors[11], // #fafafa
                fill = colors[9], // #f0f0f0
                fillSecondary = colors[10], // #f5f5f5
                fillTertiary = colors[11], // #fafafa
                fillQuaternary = colors[12], // #ffffff
            )
        }
    }
}
