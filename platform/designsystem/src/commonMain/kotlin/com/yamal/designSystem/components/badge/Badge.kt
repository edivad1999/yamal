package com.yamal.designSystem.components.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.ProvideTextStyle
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [Badge] and [BadgedBox] following Ant Design Mobile specifications.
 */
object BadgeDefaults {
    /** Default badge background color - uses theme's highlight/error color */
    val containerColor: Color
        @Composable get() = YamalTheme.colors.danger

    /** Default badge content color - --adm-color-text-light-solid */
    val contentColor: Color
        @Composable get() = YamalTheme.colors.textLightSolid

    /** Default border color when bordered is true - --adm-color-text-light-solid */
    val borderColor: Color
        @Composable get() = YamalTheme.colors.textLightSolid

    /** Dot badge radius: 5dp (10x10dp size) */
    val DotRadius: Dp = 5.dp

    /** Badge with content radius: 100dp (pill shape) */
    val BadgeWithContentRadius: Dp = 100.dp

    /** Content badge horizontal padding */
    val BadgeWithContentHorizontalPadding: Dp = 4.dp

    /** Content badge vertical padding */
    val BadgeWithContentVerticalPadding: Dp = 1.dp

    /** Badge content font size */
    val BadgeContentFontSize = 11.sp

    /** Badge content line height */
    val BadgeContentLineHeight = 12.sp

    /** Border width when bordered is true */
    val BorderWidth: Dp = 1.dp

    /** Horizontal offset for badge with content */
    val BadgeWithContentHorizontalOffset: Dp = (-6).dp

    /** Horizontal offset for dot badge */
    val BadgeHorizontalOffset: Dp = (-4).dp
}

/**
 * A BadgedBox is used to decorate [content] with a [badge] that can contain dynamic information,
 * such as the presence of a new notification or a number of pending requests.
 *
 * A common use case is to display a badge with bottom navigation items.
 *
 * Example usage:
 * ```
 * BadgedBox(badge = { Badge { Text("5") } }) {
 *     Icon(Icons.Mail, contentDescription = null)
 * }
 *
 * // Dot badge
 * BadgedBox(badge = { Badge() }) {
 *     Icon(Icons.Notification, contentDescription = null)
 * }
 * ```
 *
 * @param badge the badge to be displayed - typically a [Badge]
 * @param modifier optional [Modifier] for this item
 * @param content the anchor to which this badge will be positioned
 */
@Composable
fun BadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Layout(
        content = {
            Box(
                modifier = Modifier.layoutId("anchor"),
                contentAlignment = Alignment.Center,
                content = content,
            )
            Box(
                modifier = Modifier.layoutId("badge"),
                content = badge,
            )
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val badgePlaceable =
            measurables
                .first { it.layoutId == "badge" }
                .measure(constraints.copy(minHeight = 0))

        val anchorPlaceable =
            measurables
                .first { it.layoutId == "anchor" }
                .measure(constraints)

        val firstBaseline = anchorPlaceable[FirstBaseline]
        val lastBaseline = anchorPlaceable[LastBaseline]
        val totalWidth = anchorPlaceable.width
        val totalHeight = anchorPlaceable.height

        layout(
            totalWidth,
            totalHeight,
            mapOf(FirstBaseline to firstBaseline, LastBaseline to lastBaseline),
        ) {
            val hasContent = badgePlaceable.width > (2 * BadgeDefaults.DotRadius.roundToPx())
            val badgeHorizontalOffset =
                if (hasContent) {
                    BadgeDefaults.BadgeWithContentHorizontalOffset
                } else {
                    BadgeDefaults.BadgeHorizontalOffset
                }

            anchorPlaceable.placeRelative(0, 0)
            val badgeX = anchorPlaceable.width + badgeHorizontalOffset.roundToPx()
            val badgeY = -badgePlaceable.height / 2
            badgePlaceable.placeRelative(badgeX, badgeY)
        }
    }
}

/**
 * Badge is a component that can contain dynamic information, such as the presence of a new
 * notification or a number of pending requests. Badges can be icon only (dot) or contain short text.
 *
 * See [BadgedBox] for a layout that will properly place the badge relative to content
 * such as text or an icon.
 *
 * A simple dot badge (no content):
 * ```
 * Badge()
 * ```
 *
 * Badge with content:
 * ```
 * Badge { Text("5") }
 * Badge { Text("99+") }
 * Badge { Text("NEW") }
 * ```
 *
 * @param modifier optional [Modifier] for this item
 * @param containerColor the background color for the badge
 * @param contentColor the color of label text rendered in the badge
 * @param bordered whether to show a border around the badge
 * @param content optional content to be rendered inside the badge
 */
@Composable
fun Badge(
    modifier: Modifier = Modifier,
    containerColor: Color = BadgeDefaults.containerColor,
    contentColor: Color = BadgeDefaults.contentColor,
    bordered: Boolean = false,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    val borderColor = BadgeDefaults.borderColor

    if (content != null) {
        // Badge with content - pill shape
        val shape = RoundedCornerShape(BadgeDefaults.BadgeWithContentRadius)
        Row(
            modifier =
                modifier
                    .defaultMinSize(minWidth = 8.dp, minHeight = 14.dp)
                    .background(color = containerColor, shape = shape)
                    .clip(shape)
                    .then(
                        if (bordered) {
                            Modifier.border(
                                width = BadgeDefaults.BorderWidth,
                                color = borderColor,
                                shape = shape,
                            )
                        } else {
                            Modifier
                        },
                    ).padding(
                        horizontal = BadgeDefaults.BadgeWithContentHorizontalPadding,
                        vertical = BadgeDefaults.BadgeWithContentVerticalPadding,
                    ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                val style =
                    TextStyle(
                        fontSize = BadgeDefaults.BadgeContentFontSize,
                        lineHeight = BadgeDefaults.BadgeContentLineHeight,
                        color = contentColor,
                    )
                ProvideTextStyle(value = style) {
                    content()
                }
            }
        }
    } else {
        // Dot badge - small circle
        val dotSize = BadgeDefaults.DotRadius * 2
        val shape = RoundedCornerShape(BadgeDefaults.DotRadius)
        Box(
            modifier =
                modifier
                    .size(dotSize)
                    .background(color = containerColor, shape = shape)
                    .clip(shape)
                    .then(
                        if (bordered) {
                            Modifier.border(
                                width = BadgeDefaults.BorderWidth,
                                color = borderColor,
                                shape = shape,
                            )
                        } else {
                            Modifier
                        },
                    ),
        )
    }
}

// Previews

@Preview
@Composable
private fun BadgeDotPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                BadgedBox(badge = { Badge() }) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                BadgedBox(
                    badge = { Badge(containerColor = YamalTheme.colors.primary) },
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                BadgedBox(badge = { Badge(bordered = true) }) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BadgeContentPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                BadgedBox(badge = { Badge { Text("5") } }) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                BadgedBox(badge = { Badge { Text("99+") } }) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                BadgedBox(badge = { Badge(bordered = true) { Text("NEW") } }) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BadgeColorsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                BadgedBox(badge = { Badge { Text("5") } }) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                BadgedBox(
                    badge = {
                        Badge(containerColor = YamalTheme.colors.success) {
                            Text("5")
                        }
                    },
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                BadgedBox(
                    badge = {
                        Badge(containerColor = YamalTheme.colors.primary) {
                            Text("5")
                        }
                    },
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.border,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BadgeStandalonePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Badge()
                Badge { Text("1") }
                Badge { Text("99+") }
                Badge { Text("NEW") }
                Badge(bordered = true) { Text("Hot") }
            }
        }
    }
}
