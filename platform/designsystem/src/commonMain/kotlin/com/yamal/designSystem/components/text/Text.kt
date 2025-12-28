package com.yamal.designSystem.components.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.foundation.LocalContentAlpha
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.preview.PlatformPreviewContextConfigurationEffect
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * High-level element that displays text following Yamal Design System guidelines.
 *
 * The default [style] uses the [LocalTextStyle] provided by the theme. If you are setting
 * your own style, consider using [YamalTheme.typography] styles.
 *
 * Additionally, the [color] parameter can be used to override the text color. If [Color.Unspecified]
 * is provided, the text color will be derived from [LocalContentColor] and [LocalContentAlpha].
 *
 * Example usage:
 * ```
 * // Simple text with default style
 * Text("Hello, World!")
 *
 * // Text with custom style
 * Text(
 *     text = "Title",
 *     style = YamalTheme.typography.h3,
 * )
 *
 * // Text with custom color
 * Text(
 *     text = "Colored text",
 *     color = YamalTheme.colors.paletteColors.color6,
 * )
 * ```
 *
 * @param text The text to be displayed.
 * @param modifier The [Modifier] to be applied to this text.
 * @param color [Color] to apply to the text. If [Color.Unspecified], the current
 *   [LocalContentColor] with [LocalContentAlpha] will be used.
 * @param fontSize The size of glyphs to use when painting the text. See [TextStyle.fontSize].
 * @param fontStyle The typeface variant to use when drawing the letters (e.g., italic).
 *   See [TextStyle.fontStyle].
 * @param fontWeight The typeface thickness to use when painting the text (e.g., bold).
 * @param fontFamily The font family to be used when rendering the text.
 * @param letterSpacing The amount of space to add between each letter.
 * @param textDecoration The decorations to paint on the text (e.g., underline).
 * @param textAlign The alignment of the text within the lines of the paragraph.
 * @param lineHeight Line height for the paragraph.
 * @param overflow How visual overflow should be handled.
 * @param softWrap Whether the text should break at soft line breaks.
 * @param maxLines Maximum number of lines for the text to span.
 * @param minLines Minimum number of lines for the text to span.
 * @param onTextLayout Callback that is executed when a new text layout is calculated.
 * @param style Style configuration for the text such as color, font, line height etc.
 */
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    val textColor =
        color.takeOrElse {
            style.color.takeOrElse {
                LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            }
        }

    val mergedStyle =
        style.merge(
            TextStyle(
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontStyle = fontStyle,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign ?: TextAlign.Unspecified,
                lineHeight = lineHeight,
            ),
        )

    BasicText(
        text = text,
        modifier = modifier,
        style = mergedStyle,
        onTextLayout = onTextLayout ?: {},
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
    )
}

/**
 * High-level element that displays styled text following Yamal Design System guidelines.
 *
 * This overload accepts an [AnnotatedString] which allows for applying multiple styles
 * to different parts of the text, including inline content.
 *
 * @param text The annotated text to be displayed.
 * @param modifier The [Modifier] to be applied to this text.
 * @param color [Color] to apply to the text. If [Color.Unspecified], the current
 *   [LocalContentColor] with [LocalContentAlpha] will be used.
 * @param fontSize The size of glyphs to use when painting the text.
 * @param fontStyle The typeface variant to use when drawing the letters.
 * @param fontWeight The typeface thickness to use when painting the text.
 * @param fontFamily The font family to be used when rendering the text.
 * @param letterSpacing The amount of space to add between each letter.
 * @param textDecoration The decorations to paint on the text.
 * @param textAlign The alignment of the text within the lines of the paragraph.
 * @param lineHeight Line height for the paragraph.
 * @param overflow How visual overflow should be handled.
 * @param softWrap Whether the text should break at soft line breaks.
 * @param maxLines Maximum number of lines for the text to span.
 * @param minLines Minimum number of lines for the text to span.
 * @param inlineContent Map of composables to be inserted inline in the text.
 * @param onTextLayout Callback that is executed when a new text layout is calculated.
 * @param style Style configuration for the text.
 */
@Composable
fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    val textColor =
        color.takeOrElse {
            style.color.takeOrElse {
                LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            }
        }

    val mergedStyle =
        style.merge(
            TextStyle(
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontStyle = fontStyle,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign ?: TextAlign.Unspecified,
                lineHeight = lineHeight,
            ),
        )

    BasicText(
        text = text,
        modifier = modifier,
        style = mergedStyle,
        onTextLayout = onTextLayout ?: {},
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
    )
}

/**
 * CompositionLocal to provide text style to child composables.
 * This is a wrapper around Material's LocalTextStyle that can be used
 * to provide default text styling within a component tree.
 */
@Composable
fun ProvideTextStyle(
    value: TextStyle,
    content: @Composable () -> Unit,
) {
    val mergedStyle = LocalTextStyle.current.merge(value)
    CompositionLocalProvider(LocalTextStyle provides mergedStyle, content = content)
}

// Previews

@Preview
@Composable
private fun TextBasicPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Default text style")

                Text(
                    text = "Primary text color",
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Secondary text color",
                    color = YamalTheme.colors.textSecondary,
                )

                Text(
                    text = "Accent color",
                    color = YamalTheme.colors.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TextTypographyPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Display Large",
                    style = YamalTheme.typography.displayLarge,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Display Medium",
                    style = YamalTheme.typography.displayMedium,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Display Small",
                    style = YamalTheme.typography.displaySmall,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Title",
                    style = YamalTheme.typography.title,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Title Small",
                    style = YamalTheme.typography.titleSmall,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Body text",
                    style = YamalTheme.typography.body,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Body Medium",
                    style = YamalTheme.typography.bodyMedium,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Small text",
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TextStyleOverridesPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Bold text",
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Italic text",
                    fontStyle = FontStyle.Italic,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Underlined text",
                    textDecoration = TextDecoration.Underline,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Strikethrough text",
                    textDecoration = TextDecoration.LineThrough,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "Center aligned text with a longer content to show alignment",
                    textAlign = TextAlign.Center,
                    color = YamalTheme.colors.text,
                )

                Text(
                    text = "This is a very long text that will be truncated with ellipsis because maxLines is set to 1",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = YamalTheme.colors.text,
                )
            }
        }
    }
}
