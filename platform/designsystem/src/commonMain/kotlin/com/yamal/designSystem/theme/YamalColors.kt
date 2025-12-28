package com.yamal.designSystem.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Yamal Design System colors following Ant Design Mobile guidelines.
 * https://github.com/ant-design/ant-design-mobile/blob/master/src/global/theme-default.less
 * https://github.com/ant-design/ant-design-mobile/blob/master/src/global/theme-dark.less
 *
 * Flat structure matching ADM CSS variables exactly.
 */
@Immutable
data class YamalColors(
    // --adm-color-primary
    val primary: Color,
    // --adm-color-success
    val success: Color,
    // --adm-color-warning
    val warning: Color,
    // --adm-color-danger
    val danger: Color,
    // --adm-color-yellow
    val yellow: Color,
    // --adm-color-orange
    val orange: Color,
    // --adm-color-wathet
    val wathet: Color,
    // --adm-color-text
    val text: Color,
    // --adm-color-text-secondary
    val textSecondary: Color,
    // --adm-color-weak
    val weak: Color,
    // --adm-color-light
    val light: Color,
    // --adm-color-border
    val border: Color,
    // --adm-color-box
    val box: Color,
    // --adm-color-background
    val background: Color,
    // --adm-color-white
    val white: Color,
    // --adm-color-text-light-solid (references --adm-color-white)
    val textLightSolid: Color,
    // --adm-color-text-dark-solid
    val textDarkSolid: Color,
    // --adm-color-fill-content (references --adm-color-box)
    val fillContent: Color,
    // --adm-color-highlight (references --adm-color-danger)
    val highlight: Color,
) {
    companion object {
        val DefaultBrandColor = Color(0xFF1677FF)
    }
}

/**
 * Light theme - ADM theme-default.less
 */
fun YamalColors.Companion.light(brandColor: Color = DefaultBrandColor): YamalColors =
    YamalColors(
        primary = brandColor,
        success = Color(0xFF00B578),
        warning = Color(0xFFFF8F1F),
        danger = Color(0xFFFF3141),
        yellow = Color(0xFFFF9F18),
        orange = Color(0xFFFF6430),
        wathet = Color(0xFFE7F1FF),
        text = Color(0xFF333333),
        textSecondary = Color(0xFF666666),
        weak = Color(0xFF999999),
        light = Color(0xFFCCCCCC),
        border = Color(0xFFEEEEEE),
        box = Color(0xFFF5F5F5),
        background = Color(0xFFFFFFFF),
        white = Color(0xFFFFFFFF),
        textLightSolid = Color(0xFFFFFFFF),
        textDarkSolid = Color(0xFF000000),
        fillContent = Color(0xFFF5F5F5),
        highlight = Color(0xFFFF3141),
    )

/**
 * Dark theme - ADM theme-dark.less
 */
fun YamalColors.Companion.dark(brandColor: Color = DefaultBrandColor): YamalColors =
    YamalColors(
        primary = if (brandColor == DefaultBrandColor) Color(0xFF3086FF) else brandColor,
        success = Color(0xFF34B368),
        warning = Color(0xFFFFA930),
        danger = Color(0xFFFF4A58),
        yellow = Color(0xFFFFA930),
        orange = Color(0xFFE65A2B),
        wathet = Color(0xFF0D2543),
        text = Color(0xFFE6E6E6),
        textSecondary = Color(0xFFB3B3B3),
        weak = Color(0xFF808080),
        light = Color(0xFF4D4D4D),
        border = Color(0xFF2B2B2B),
        box = Color(0xFF0A0A0A),
        background = Color(0xFF1A1A1A),
        white = Color(0xFFFFFFFF),
        textLightSolid = Color(0xFFFFFFFF),
        textDarkSolid = Color(0xFF000000),
        fillContent = Color(0xFF0A0A0A),
        highlight = Color(0xFFFF4A58),
    )

/**
 * Material Colors interop
 */
fun YamalColors.toMaterialColors(isDark: Boolean): Colors =
    Colors(
        primary = primary,
        primaryVariant = primary,
        secondary = primary,
        secondaryVariant = primary,
        background = box,
        surface = background,
        error = danger,
        onPrimary = if (isDark) textDarkSolid else textLightSolid,
        onSecondary = if (isDark) textDarkSolid else textLightSolid,
        onBackground = text,
        onSurface = text,
        onError = textLightSolid,
        isLight = !isDark,
    )

val LocalYamalColors = staticCompositionLocalOf { YamalColors.light() }
