package com.yamal.designSystem.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Yamal Design System typography following Ant Design Mobile guidelines.
 * https://github.com/ant-design/ant-design-mobile/tree/master/src/global
 *
 * ADM uses a 10-level font size scale from 9sp to 18sp with 13sp as main body text.
 */
@Immutable
class YamalTypography {
    // Display / Heading styles (beyond the standard 10-level scale)

    /** Display Large: 24sp, for large titles */
    val displayLarge: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.displayLarge,
            fontSize = Dimension.FontSize.displayLarge,
        )

    /** Display Medium: 20sp */
    val displayMedium: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.displayMedium,
            fontSize = Dimension.FontSize.displayMedium,
        )

    /** Display Small: 18sp (font-size-10) */
    val displaySmall: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.size10,
            fontSize = Dimension.FontSize.size10,
        )

    // Title styles

    /** Title Large: 17sp (font-size-9) */
    val titleLarge: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.size9,
            fontSize = Dimension.FontSize.size9,
        )

    /** Title: 16sp (font-size-8) */
    val title: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.size8,
            fontSize = Dimension.FontSize.size8,
        )

    /** Title Small: 15sp (font-size-7) */
    val titleSmall: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.size7,
            fontSize = Dimension.FontSize.size7,
        )

    // Body styles (13sp is the main body text size in ADM)

    /** Body Large: 14sp (font-size-6) */
    val bodyLarge: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.size6,
            fontSize = Dimension.FontSize.size6,
        )

    /** Body: 13sp (font-size-5, --adm-font-size-main) - the main body text */
    val body: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.main,
            fontSize = Dimension.FontSize.main,
        )

    /** Body Medium: 13sp with medium weight */
    val bodyMedium: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.main,
            fontSize = Dimension.FontSize.main,
        )

    /** Body Strong: 13sp with semibold weight */
    val bodyStrong: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.main,
            fontSize = Dimension.FontSize.main,
        )

    // Caption / Footnote styles

    /** Caption: 12sp (font-size-4) */
    val caption: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.size4,
            fontSize = Dimension.FontSize.size4,
        )

    /** Caption Medium: 12sp with medium weight */
    val captionMedium: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.size4,
            fontSize = Dimension.FontSize.size4,
        )

    /** Small: 11sp (font-size-3) */
    val small: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.size3,
            fontSize = Dimension.FontSize.size3,
        )

    /** Mini: 10sp (font-size-2) */
    val mini: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.size2,
            fontSize = Dimension.FontSize.size2,
        )

    /** Tiny: 9sp (font-size-1) - smallest size */
    val tiny: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.size1,
            fontSize = Dimension.FontSize.size1,
        )

    // Link styles

    /** Link style: same as body */
    val link: TextStyle = body

    /** Link Small: same as caption */
    val linkSmall: TextStyle = caption
}

/**
 * Converts YamalTypography to Material Typography for interoperability.
 */
fun YamalTypography.toMaterialTypography(): Typography =
    Typography(
        h1 = displayLarge,
        h2 = displayMedium,
        h3 = displaySmall,
        h4 = title,
        h5 = titleSmall,
        h6 = bodyLarge,
        subtitle1 = bodyMedium,
        subtitle2 = caption,
        body1 = body,
        body2 = small,
        button = bodyMedium,
        caption = caption,
        overline = captionMedium,
    )

/**
 * CompositionLocal for providing YamalTypography throughout the app.
 */
val LocalYamalTypography = staticCompositionLocalOf { YamalTypography() }
