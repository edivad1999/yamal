package com.yamal.designSystem.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Yamal Design System typography following Ant Design guidelines.
 * https://ant.design/docs/spec/font
 *
 * Font sizes follow the modular scale with 14sp as base:
 * - Display: 38sp, 30sp, 24sp, 20sp, 16sp
 * - Body: 14sp (base), 12sp (small)
 *
 * Line heights use 1.5715 ratio for body text and closer ratios for headings.
 */
@Immutable class YamalTypography {
    // Display / Title styles
    /** Display Large: 38sp, used for large titles */
    val displayLarge: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.h1,
            fontSize = Dimension.FontSize.h1,
        )

    /** Display Medium: 30sp */
    val displayMedium: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.h2,
            fontSize = Dimension.FontSize.h2,
        )

    /** Display Small: 24sp */
    val displaySmall: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.h3,
            fontSize = Dimension.FontSize.h3,
        )

    // Heading styles (h1-h5 for backwards compatibility)
    val h1: TextStyle = displayLarge
    val h2: TextStyle = displayMedium
    val h3: TextStyle = displaySmall
    val h4: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.h4,
            fontSize = Dimension.FontSize.h4,
        )
    val h5: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.h5,
            fontSize = Dimension.FontSize.h5,
        )

    /** Title style with normal weight */
    val titleMedium: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.h5,
            fontSize = Dimension.FontSize.h5,
        )

    /** Title style with regular weight */
    val titleRegular: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.h5,
            fontSize = Dimension.FontSize.h5,
        )

    // Body styles

    /** Body Large: 16sp for emphasized body text */
    val bodyLarge: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.h5,
            fontSize = Dimension.FontSize.h5,
        )

    /** Body: 14sp base text style */
    val body: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.body,
            fontSize = Dimension.FontSize.body,
        )

    /** Body Medium: 14sp with medium weight */
    val bodyMedium: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.body,
            fontSize = Dimension.FontSize.body,
        )

    /** Body Strong: 14sp with semibold weight */
    val bodyStrong: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            lineHeight = Dimension.FontLineHeight.body,
            fontSize = Dimension.FontSize.body,
        )

    // Small / Caption styles

    /** Small: 12sp for secondary text */
    val small: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Normal,
            lineHeight = Dimension.FontLineHeight.footnote,
            fontSize = Dimension.FontSize.footnote,
        )

    /** Small Medium: 12sp with medium weight */
    val smallMedium: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            lineHeight = Dimension.FontLineHeight.footnote,
            fontSize = Dimension.FontSize.footnote,
        )

    /** Footnote: alias for small medium */
    val footnote: TextStyle = smallMedium

    // Link styles

    /** Link style: inherits body but typically colored */
    val link: TextStyle = body

    /** Link Small: 12sp link style */
    val linkSmall: TextStyle = small

    // Backwards compatibility
    val h5Light: TextStyle = titleRegular
    val bodySmall: TextStyle = small
    val caption: TextStyle = small
    val subtitle: TextStyle = titleRegular
    val heading1: TextStyle = h1
    val heading2: TextStyle = h2
}

/**
 * Converts YamalTypography to Material Typography.
 */
fun YamalTypography.toTypography() =
    Typography(
        h1 = h1,
        h2 = h2,
        h3 = h3,
        h4 = h4,
        h5 = h5,
        h6 = titleMedium,
        subtitle1 = titleRegular,
        subtitle2 = bodyMedium,
        body1 = body,
        body2 = small,
        button = bodyMedium,
        caption = small,
        overline = smallMedium,
    )
