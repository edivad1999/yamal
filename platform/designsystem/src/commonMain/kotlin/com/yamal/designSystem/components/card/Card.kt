package com.yamal.designSystem.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.ProvideTextStyle
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.preview.PlatformPreviewContextConfigurationEffect
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A universal card container following Ant Design Mobile guidelines.
 *
 * Cards can carry text, lists, pictures, paragraphs, etc., making it convenient
 * for users to browse content.
 *
 * Example usage:
 * ```
 * // Simple card with title
 * YamalCard(
 *     title = { Text("Card Title") },
 * ) {
 *     Text("Card content goes here")
 * }
 *
 * // Card with icon, title and extra content
 * YamalCard(
 *     icon = { Icon(Icons.Outlined.Info, null) },
 *     title = { Text("Title") },
 *     extra = { Text("More") },
 * ) {
 *     Text("Card content")
 * }
 *
 * // Clickable card
 * YamalCard(
 *     title = { Text("Clickable") },
 *     onClick = { /* handle click */ },
 * ) {
 *     Text("Click anywhere on this card")
 * }
 * ```
 *
 * @param modifier The [Modifier] to be applied to this card.
 * @param title Optional title content displayed in the header's left area.
 * @param icon Optional icon displayed before the title in the header.
 * @param extra Optional content displayed in the header's right area.
 * @param shape Defines the card's shape. Defaults to [CardDefaults.shape].
 * @param colors [CardColors] that will be used to resolve the colors for this card.
 *   See [CardDefaults.colors].
 * @param onClick Optional callback fired when the card is clicked.
 * @param onHeaderClick Optional callback fired when the header is clicked.
 * @param onBodyClick Optional callback fired when the body is clicked.
 * @param content The body content of the card.
 */
@Composable
fun YamalCard(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    extra: @Composable (() -> Unit)? = null,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.colors(),
    onClick: (() -> Unit)? = null,
    onHeaderClick: (() -> Unit)? = null,
    onBodyClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    val hasHeader = title != null || icon != null || extra != null
    val hasBody = content != null

    Surface(
        modifier =
            modifier.then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
            ),
        shape = shape,
        color = colors.containerColor,
        contentColor = colors.contentColor,
    ) {
        Column {
            // Header
            if (hasHeader) {
                CardHeader(
                    icon = icon,
                    title = title,
                    extra = extra,
                    colors = colors,
                    showBorder = hasBody,
                    onClick = onHeaderClick,
                )
            }

            // Body
            if (hasBody) {
                CardBody(
                    colors = colors,
                    onClick = onBodyClick,
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun CardHeader(
    icon: @Composable (() -> Unit)?,
    title: @Composable (() -> Unit)?,
    extra: @Composable (() -> Unit)?,
    colors: CardColors,
    showBorder: Boolean,
    onClick: (() -> Unit)?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = CardDefaults.PaddingInline,
                        vertical = CardDefaults.HeaderPaddingBlock,
                    ),
            horizontalArrangement = Arrangement.spacedBy(CardDefaults.HeaderGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon and Title
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(CardDefaults.HeaderGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                icon?.let {
                    CompositionLocalProvider(LocalContentColor provides colors.titleColor) {
                        it()
                    }
                }

                title?.let {
                    ProvideTextStyle(value = CardDefaults.TitleTextStyle) {
                        CompositionLocalProvider(LocalContentColor provides colors.titleColor) {
                            it()
                        }
                    }
                }
            }

            // Extra
            extra?.let {
                CompositionLocalProvider(LocalContentColor provides colors.extraColor) {
                    it()
                }
            }
        }

        // Header border
        if (showBorder) {
            Divider(
                color = colors.borderColor,
                thickness = CardDefaults.HeaderBorderWidth,
            )
        }
    }
}

@Composable
private fun CardBody(
    colors: CardColors,
    onClick: (() -> Unit)?,
    content: @Composable (() -> Unit)?,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(
                    horizontal = CardDefaults.PaddingInline,
                    vertical = CardDefaults.BodyPaddingBlock,
                ),
    ) {
        CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
            content?.invoke()
        }
    }
}

/**
 * Represents the colors of the card elements.
 *
 * @param containerColor The background color of the card.
 * @param contentColor The color for body content.
 * @param titleColor The color for the title text.
 * @param extraColor The color for extra content.
 * @param borderColor The color for the header border.
 */
@Immutable
class CardColors(
    val containerColor: Color,
    val contentColor: Color,
    val titleColor: Color,
    val extraColor: Color,
    val borderColor: Color,
) {
    /**
     * Returns a copy of this CardColors, optionally overriding some of the values.
     */
    fun copy(
        containerColor: Color = this.containerColor,
        contentColor: Color = this.contentColor,
        titleColor: Color = this.titleColor,
        extraColor: Color = this.extraColor,
        borderColor: Color = this.borderColor,
    ) = CardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        titleColor = titleColor,
        extraColor = extraColor,
        borderColor = borderColor,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is CardColors) return false

        if (containerColor != other.containerColor) return false
        if (contentColor != other.contentColor) return false
        if (titleColor != other.titleColor) return false
        if (extraColor != other.extraColor) return false
        if (borderColor != other.borderColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + titleColor.hashCode()
        result = 31 * result + extraColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        return result
    }
}

/**
 * Contains default values used for [YamalCard] following Ant Design Mobile specs.
 */
object CardDefaults {
    /** Default border radius: 8dp */
    val BorderRadius: Dp = 8.dp

    /** Default shape with border radius */
    val shape: Shape = RoundedCornerShape(BorderRadius)

    /** Horizontal padding: 12dp */
    val PaddingInline: Dp = 12.dp

    /** Header vertical padding: 12dp */
    val HeaderPaddingBlock: Dp = 12.dp

    /** Body vertical padding: 12dp */
    val BodyPaddingBlock: Dp = 12.dp

    /** Header gap between elements: 8dp */
    val HeaderGap: Dp = 8.dp

    /** Header border width: 0.5dp */
    val HeaderBorderWidth: Dp = 0.5.dp

    /** Title text style: font-size-7 (15sp), bold, line height 1.4 */
    val TitleTextStyle: TextStyle =
        TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = (15 * 1.4).sp,
        )

    /**
     * Creates a [CardColors] with the default colors.
     */
    @Composable
    fun colors(): CardColors =
        CardColors(
            containerColor = YamalTheme.colors.background,
            contentColor = YamalTheme.colors.text,
            titleColor = YamalTheme.colors.text,
            extraColor = YamalTheme.colors.textSecondary,
            borderColor = YamalTheme.colors.border,
        )

    /**
     * Creates a [CardColors] with custom colors.
     *
     * @param containerColor The background color of the card.
     * @param contentColor The color for body content.
     * @param titleColor The color for the title text.
     * @param extraColor The color for extra content.
     * @param borderColor The color for the header border.
     */
    @Composable
    fun colors(
        containerColor: Color = YamalTheme.colors.background,
        contentColor: Color = YamalTheme.colors.text,
        titleColor: Color = YamalTheme.colors.text,
        extraColor: Color = YamalTheme.colors.textSecondary,
        borderColor: Color = YamalTheme.colors.border,
    ): CardColors =
        CardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            titleColor = titleColor,
            extraColor = extraColor,
            borderColor = borderColor,
        )
}

// Previews

@Preview
@Composable
private fun YamalCardBasicPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.box) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalCard(
                    title = { Text("Card Title") },
                ) {
                    Text("Card content goes here. This is a simple card with just a title and body content.")
                }

                YamalCard(
                    title = { Text("With Extra") },
                    extra = { Text("More", color = YamalTheme.colors.primary) },
                ) {
                    Text("Card content with extra action in the header.")
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCardHeaderOnlyPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.box) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalCard(
                    title = { Text("Header Only Card") },
                )

                YamalCard(
                    title = { Text("Title") },
                    extra = { Text("Action") },
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalCardBodyOnlyPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.box) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalCard {
                    Text("This card has only body content, no header.")
                }

                YamalCard {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Multiple lines of content")
                        Text("Second line")
                        Text("Third line")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCardClickablePreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.box) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalCard(
                    title = { Text("Clickable Card") },
                    onClick = { /* handle click */ },
                ) {
                    Text("Click anywhere on this card")
                }

                YamalCard(
                    title = { Text("Clickable Header") },
                    onHeaderClick = { /* handle header click */ },
                ) {
                    Text("Only the header is clickable")
                }

                YamalCard(
                    title = { Text("Clickable Body") },
                    onBodyClick = { /* handle body click */ },
                ) {
                    Text("Only the body is clickable")
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCardCustomColorsPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        Surface(color = YamalTheme.colors.box) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val customColors =
                    CardDefaults.colors(
                        containerColor = YamalTheme.colors.wathet,
                        titleColor = YamalTheme.colors.primary,
                    )

                YamalCard(
                    title = { Text("Custom Colors") },
                    colors = customColors,
                ) {
                    Text("Card with custom background and title colors")
                }
            }
        }
    }
}
