package com.yamal.designSystem.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Immutable class YamalTypography {
    // Header
    val h1: TextStyle = TextStyle(fontWeight = FontWeight.Medium, lineHeight = Dimension.FontLineHeight.h1, fontSize = Dimension.FontSize.h1)
    val h2: TextStyle = TextStyle(fontWeight = FontWeight.Medium, lineHeight = Dimension.FontLineHeight.h2, fontSize = Dimension.FontSize.h2)
    val h3: TextStyle = TextStyle(fontWeight = FontWeight.Medium, lineHeight = Dimension.FontLineHeight.h3, fontSize = Dimension.FontSize.h3)
    val h4: TextStyle = TextStyle(fontWeight = FontWeight.Medium, lineHeight = Dimension.FontLineHeight.h4, fontSize = Dimension.FontSize.h4)
    val h5: TextStyle = TextStyle(fontWeight = FontWeight.Medium, lineHeight = Dimension.FontLineHeight.h5, fontSize = Dimension.FontSize.h5)
    val h5Light: TextStyle = TextStyle(fontWeight = FontWeight.Normal, lineHeight = Dimension.FontLineHeight.h5, fontSize = Dimension.FontSize.h5)

    // Body
    val body: TextStyle = TextStyle(fontWeight = FontWeight.Normal, lineHeight = Dimension.FontLineHeight.body, fontSize = Dimension.FontSize.body)
    val bodyMedium: TextStyle =
        TextStyle(fontWeight = FontWeight.Medium, lineHeight = Dimension.FontLineHeight.body, fontSize = Dimension.FontSize.body)

    // Footer
    val footnote: TextStyle =
        TextStyle(fontWeight = FontWeight.Medium, lineHeight = Dimension.FontLineHeight.footnote, fontSize = Dimension.FontSize.footnote)
}

fun YamalTypography.toTypography() =
    Typography(
        h1 = h1,
        h2 = h2,
        h3 = h3,
        h4 = h4,
        h5 = h5,
        h6 = h5Light,
        subtitle1 = h5Light,
        subtitle2 = bodyMedium,
        body1 = body,
        body2 = footnote,
        button = body,
        caption = footnote,
        overline = footnote,
    )
