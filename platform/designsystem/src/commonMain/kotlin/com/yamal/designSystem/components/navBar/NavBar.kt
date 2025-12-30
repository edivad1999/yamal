package com.yamal.designSystem.components.navBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.ProvideTextStyle
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.foundation.LocalContentAlpha
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.preview.PlatformPreviewContextConfigurationEffect
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * NavBar provides navigation for pages following Ant Design Mobile guidelines.
 *
 * Layout uses three sections:
 * - Left section (flex: 1): back button + optional left content
 * - Title section (flex: auto): centered title
 * - Right section (flex: 1): right content
 *
 * @param title The title to be displayed in the center of the NavBar.
 * @param modifier The [Modifier] to be applied to this NavBar.
 * @param back The text content for the back button. If null, no back button is shown.
 * @param backIcon The icon for the back button. Defaults to Left arrow when [back] is provided.
 *   Set to null to hide the icon while still showing back text.
 * @param onBack Callback when the back button is clicked.
 * @param left Additional content to display in the left section after the back button.
 * @param right Content to display in the right section.
 * @param backgroundColor The background color for the NavBar.
 * @param contentColor The preferred content color provided by this NavBar to its children.
 * @param elevation The elevation of this NavBar.
 */
@Composable
fun YamalNavBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    back: String? = null,
    backIcon: @Composable (() -> Unit)? = {
        Icon(
            Icons.Outlined.Left,
            contentDescription = null,
            modifier = Modifier.size(NavBarDefaults.BackArrowSize),
        )
    },
    onBack: (() -> Unit)? = null,
    left: @Composable (() -> Unit)? = null,
    right: @Composable (() -> Unit)? = null,
    backgroundColor: Color = YamalTheme.colors.background,
    contentColor: Color = YamalTheme.colors.text,
    elevation: Dp = NavBarDefaults.Elevation,
    windowInsets: WindowInsets =
        WindowInsets.statusBars.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Top,
        ),
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth(),
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Row(
            modifier =
                Modifier
                    .windowInsetsPadding(windowInsets)
                    .fillMaxWidth()
                    .height(NavBarDefaults.Height)
                    .padding(horizontal = NavBarDefaults.HorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left section (flex: 1)
            Row(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                CompositionLocalProvider(LocalContentAlpha provides 1f) {
                    // Back button
                    if (back != null || onBack != null) {
                        Row(
                            modifier =
                                Modifier
                                    .clickable(
                                        enabled = onBack != null,
                                        onClick = { onBack?.invoke() },
                                    ).padding(vertical = NavBarDefaults.BackButtonVerticalPadding),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (backIcon != null) {
                                Box(modifier = Modifier.padding(end = NavBarDefaults.BackArrowEndPadding)) {
                                    backIcon()
                                }
                            }
                            back?.let { Text(text = it) }
                        }
                        if (left != null) {
                            Box(modifier = Modifier.padding(start = NavBarDefaults.BackButtonEndMargin)) {
                                left()
                            }
                        }
                    } else {
                        left?.invoke()
                    }
                }
            }

            // Title section (flex: auto - content-based width with padding)
            // ADM: font-size: var(--adm-font-size-10) = 18px
            Box(
                modifier = Modifier.padding(horizontal = NavBarDefaults.TitleHorizontalPadding),
                contentAlignment = Alignment.Center,
            ) {
                ProvideTextStyle(value = TextStyle(fontSize = 18.sp)) {
                    CompositionLocalProvider(LocalContentAlpha provides 1f) {
                        title()
                    }
                }
            }

            // Right section (flex: 1)
            Row(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                CompositionLocalProvider(LocalContentAlpha provides 1f) {
                    right?.invoke()
                }
            }
        }
    }
}

/**
 * Contains default values used for [YamalNavBar] following Ant Design Mobile specs.
 */
object NavBarDefaults {
    /** Default height: 45dp */
    val Height: Dp = 45.dp

    /** Default elevation: 0dp */
    val Elevation: Dp = 0.dp

    /** Horizontal padding: 12dp */
    val HorizontalPadding: Dp = 12.dp

    /** Back button vertical padding: 6dp */
    val BackButtonVerticalPadding: Dp = 6.dp

    /** Back button end margin: 16dp */
    val BackButtonEndMargin: Dp = 16.dp

    /** Back arrow end padding: 4dp */
    val BackArrowEndPadding: Dp = 4.dp

    /** Back arrow size: 24dp (ADM: font-size: 24px) */
    val BackArrowSize: Dp = 24.dp

    /** Title horizontal padding: 12dp */
    val TitleHorizontalPadding: Dp = 12.dp
}

// Previews

@Preview
@Composable
private fun YamalNavBarBasicPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = { Text("Title") },
            back = "",
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun YamalNavBarWithBackTextPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = { Text("Title") },
            back = "Back",
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun YamalNavBarWithRightPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = { Text("Title") },
            back = "",
            onBack = {},
            right = {
                Text(
                    text = "Done",
                    color = YamalTheme.colors.primary,
                )
            },
        )
    }
}

@Preview
@Composable
private fun YamalNavBarNoBackPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = { Text("Title") },
            right = {
                Icon(Icons.Outlined.Close, contentDescription = "Close")
            },
        )
    }
}

@Preview
@Composable
private fun YamalNavBarWithLeftContentPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = { Text("Title") },
            back = "",
            onBack = {},
            left = {
                Icon(Icons.Outlined.Search, contentDescription = "Search")
            },
            right = {
                Icon(Icons.Outlined.More, contentDescription = "More")
            },
        )
    }
}

@Preview
@Composable
private fun YamalNavBarLongTitlePreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = {
                Text(
                    text = "This is a very long title that should be truncated",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            back = "Back",
            onBack = {},
            right = {
                Text(
                    text = "Done",
                    color = YamalTheme.colors.primary,
                )
            },
        )
    }
}

@Preview
@Composable
private fun YamalNavBarCustomBackIconPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = { Text("Custom Icon") },
            back = "Close",
            backIcon = { Icon(Icons.Outlined.Close, contentDescription = null) },
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun YamalNavBarNoBackIconPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalNavBar(
            title = { Text("Text Only Back") },
            back = "Cancel",
            backIcon = null,
            onBack = {},
        )
    }
}
