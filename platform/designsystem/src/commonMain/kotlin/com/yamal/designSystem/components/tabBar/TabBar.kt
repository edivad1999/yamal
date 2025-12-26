package com.yamal.designSystem.components.tabBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.YamalBadge
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.preview.PlatformPreviewContextConfigurationEffect
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * TabBar provides navigation for mobile apps following Ant Design Mobile guidelines.
 *
 * A tab bar typically appears at the bottom of the screen and allows users to
 * switch between different primary views.
 *
 * [YamalTabBar] should contain three to five [YamalTabBarItem]s, each representing a singular
 * destination.
 *
 * A simple example looks like:
 * ```
 * var selectedTab by remember { mutableStateOf(0) }
 *
 * YamalTabBar {
 *     YamalTabBarItem(
 *         selected = selectedTab == 0,
 *         onClick = { selectedTab = 0 },
 *         icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
 *         label = { Text("Home") },
 *     )
 *     YamalTabBarItem(
 *         selected = selectedTab == 1,
 *         onClick = { selectedTab = 1 },
 *         icon = { Icon(Icons.Outlined.User, contentDescription = "Profile") },
 *         label = { Text("Profile") },
 *     )
 * }
 * ```
 *
 * See [YamalTabBarItem] for configuration specific to each item.
 *
 * @param modifier The [Modifier] to be applied to this TabBar.
 * @param containerColor The color used for the background of this tab bar.
 * @param contentColor The preferred content color provided by this tab bar to its children.
 * @param content The content of this tab bar, typically 3-5 [YamalTabBarItem]s.
 */
@Composable
fun YamalTabBar(
    modifier: Modifier = Modifier,
    containerColor: Color = TabBarDefaults.containerColor,
    contentColor: Color = TabBarDefaults.contentColor,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
        elevation = TabBarDefaults.Elevation,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(TabBarDefaults.Height)
                    .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * A tab bar item for use in [YamalTabBar].
 *
 * The recommended configuration for a [YamalTabBarItem] depends on how many items there are
 * inside a [YamalTabBar]:
 * - Three to four destinations: Display icons and text labels for all destinations.
 * - Five destinations: Active destinations display an icon and text label. Inactive destinations
 *   use icons, and use text labels if space permits.
 *
 * @param selected Whether this item is selected.
 * @param onClick Called when this item is clicked.
 * @param icon Icon for this item, typically an [Icon].
 * @param modifier The [Modifier] to be applied to this item.
 * @param enabled Controls the enabled state of this item. When `false`, this component will not
 *   respond to user input, and it will appear visually disabled.
 * @param label Optional text label for this item.
 * @param badge Optional badge to display on the icon. Can be [TabBarBadge.Dot], [TabBarBadge.Count],
 *   or [TabBarBadge.Custom].
 * @param colors [TabBarItemColors] that will be used to resolve the colors used for this
 *   item in different states. See [TabBarItemDefaults.colors].
 */
@Composable
fun RowScope.YamalTabBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    badge: TabBarBadge? = null,
    colors: TabBarItemColors = TabBarItemDefaults.colors(),
) {
    val contentColor = colors.contentColor(selected = selected, enabled = enabled)

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier =
                modifier
                    .weight(1f)
                    .clickable(
                        enabled = enabled,
                        onClick = onClick,
                    ).semantics { role = Role.Tab }
                    .padding(
                        horizontal = TabBarDefaults.ItemHorizontalPadding,
                        vertical = TabBarDefaults.ItemVerticalPadding,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Icon with optional badge
            val iconComposable: @Composable () -> Unit = {
                Box(
                    modifier =
                        Modifier
                            .height(TabBarDefaults.IconHeight)
                            .size(TabBarDefaults.IconHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    icon()
                }
            }

            when (badge) {
                is TabBarBadge.Dot -> {
                    YamalBadge(
                        dot = true,
                        offset = TabBarDefaults.IconBadgeOffset,
                        content = iconComposable,
                    )
                }

                is TabBarBadge.Count -> {
                    YamalBadge(
                        count = badge.count,
                        offset = TabBarDefaults.IconBadgeOffset,
                        content = iconComposable,
                    )
                }

                is TabBarBadge.Custom -> {
                    Box {
                        iconComposable()
                        Box(modifier = Modifier.align(Alignment.TopEnd)) {
                            badge.content()
                        }
                    }
                }

                null -> {
                    iconComposable()
                }
            }

            // Label
            label?.let {
                Box(modifier = Modifier.padding(top = TabBarDefaults.TitleTopMargin)) {
                    it()
                }
            }
        }
    }
}

/**
 * Sealed interface for badge content on tab bar items.
 */
sealed interface TabBarBadge {
    /**
     * A dot indicator badge.
     */
    data object Dot : TabBarBadge

    /**
     * A count badge showing a number.
     */
    data class Count(
        val count: Int,
    ) : TabBarBadge

    /**
     * Custom badge content.
     */
    data class Custom(
        val content: @Composable () -> Unit,
    ) : TabBarBadge
}

/**
 * Represents the colors of the various elements of a tab bar item.
 *
 * @param selectedContentColor The color to use for the icon and label when the item is selected.
 * @param unselectedContentColor The color to use for the icon and label when the item is unselected.
 * @param disabledContentColor The color to use for the icon and label when the item is disabled.
 */
@Immutable
class TabBarItemColors(
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color,
) {
    /**
     * Returns the content color for this item based on [selected] and [enabled] states.
     */
    @Stable
    fun contentColor(
        selected: Boolean,
        enabled: Boolean,
    ): Color =
        when {
            !enabled -> disabledContentColor
            selected -> selectedContentColor
            else -> unselectedContentColor
        }

    /**
     * Returns a copy of this TabBarItemColors, optionally overriding some of the values.
     */
    fun copy(
        selectedContentColor: Color = this.selectedContentColor,
        unselectedContentColor: Color = this.unselectedContentColor,
        disabledContentColor: Color = this.disabledContentColor,
    ) = TabBarItemColors(
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        disabledContentColor = disabledContentColor,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is TabBarItemColors) return false

        if (selectedContentColor != other.selectedContentColor) return false
        if (unselectedContentColor != other.unselectedContentColor) return false
        if (disabledContentColor != other.disabledContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = selectedContentColor.hashCode()
        result = 31 * result + unselectedContentColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        return result
    }
}

/**
 * Contains default values used for [YamalTabBar] following Ant Design Mobile specs.
 */
object TabBarDefaults {
    /** Default height: 50dp */
    val Height: Dp = 50.dp

    /** Default elevation: 0dp */
    val Elevation: Dp = 0.dp

    /** Item horizontal padding: 8dp */
    val ItemHorizontalPadding: Dp = 8.dp

    /** Item vertical padding: 4dp */
    val ItemVerticalPadding: Dp = 4.dp

    /** Icon height: 24dp */
    val IconHeight: Dp = 24.dp

    /** Title top margin (when icon present): 2dp */
    val TitleTopMargin: Dp = 2.dp

    /** Title font size: 12sp (Ant Design font-size-2) */
    val TitleFontSize = 12.sp

    /** Title line height: 15sp */
    val TitleLineHeight = 15.sp

    /** Badge offset for icons */
    val IconBadgeOffset: IntOffset = IntOffset(6, -6)

    /** Default container color for the tab bar. */
    val containerColor: Color
        @Composable get() = YamalTheme.colors.neutralColors.background

    /** Default content color for the tab bar. */
    val contentColor: Color
        @Composable get() = YamalTheme.colors.neutralColors.primaryText
}

/**
 * Contains default values used for [YamalTabBarItem].
 */
object TabBarItemDefaults {
    /**
     * Creates a [TabBarItemColors] with the default colors.
     */
    @Composable
    fun colors(): TabBarItemColors =
        TabBarItemColors(
            selectedContentColor = YamalTheme.colors.paletteColors.color6,
            unselectedContentColor = YamalTheme.colors.neutralColors.secondaryText,
            disabledContentColor =
                YamalTheme.colors.neutralColors.secondaryText
                    .copy(alpha = 0.38f),
        )

    /**
     * Creates a [TabBarItemColors] with custom colors.
     *
     * @param selectedContentColor The color to use for the icon and label when the item is selected.
     * @param unselectedContentColor The color to use for the icon and label when the item is unselected.
     * @param disabledContentColor The color to use for the icon and label when the item is disabled.
     */
    @Composable
    fun colors(
        selectedContentColor: Color = YamalTheme.colors.paletteColors.color6,
        unselectedContentColor: Color = YamalTheme.colors.neutralColors.secondaryText,
        disabledContentColor: Color =
            YamalTheme.colors.neutralColors.secondaryText
                .copy(alpha = 0.38f),
    ): TabBarItemColors =
        TabBarItemColors(
            selectedContentColor = selectedContentColor,
            unselectedContentColor = unselectedContentColor,
            disabledContentColor = disabledContentColor,
        )
}

// Previews

@Preview
@Composable
private fun YamalTabBarBasicPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        var selectedTab by remember { mutableStateOf(0) }

        YamalTabBar {
            YamalTabBarItem(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = {
                    Icon(
                        icon = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
            )
            YamalTabBarItem(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                icon = {
                    Icon(
                        icon = if (selectedTab == 1) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                        contentDescription = "To Do",
                    )
                },
                label = {
                    Text(
                        text = "To Do",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
            )
            YamalTabBarItem(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                icon = {
                    Icon(
                        icon = if (selectedTab == 2) Icons.Filled.Message else Icons.Outlined.Message,
                        contentDescription = "Message",
                    )
                },
                label = {
                    Text(
                        text = "Message",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
                badge = TabBarBadge.Count(99),
            )
            YamalTabBarItem(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                icon = {
                    Icon(
                        icon = if (selectedTab == 3) Icons.Filled.Profile else Icons.Outlined.User,
                        contentDescription = "Me",
                    )
                },
                label = {
                    Text(
                        text = "Me",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun YamalTabBarWithBadgesPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalTabBar {
            YamalTabBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.Home, contentDescription = "Home") },
                label = {
                    Text(
                        text = "Home",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
            )
            YamalTabBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.CheckCircle, contentDescription = "To Do") },
                label = {
                    Text(
                        text = "To Do",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
                badge = TabBarBadge.Dot,
            )
            YamalTabBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.Message, contentDescription = "Message") },
                label = {
                    Text(
                        text = "Message",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
                badge = TabBarBadge.Count(5),
            )
            YamalTabBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.User, contentDescription = "Me") },
                label = {
                    Text(
                        text = "Me",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
                badge = TabBarBadge.Count(100),
            )
        }
    }
}

@Preview
@Composable
private fun YamalTabBarIconOnlyPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        var selectedTab by remember { mutableStateOf(0) }

        YamalTabBar {
            YamalTabBarItem(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = {
                    Icon(
                        icon = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                    )
                },
            )
            YamalTabBarItem(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                icon = {
                    Icon(
                        icon = Icons.Outlined.Search,
                        contentDescription = "Search",
                    )
                },
            )
            YamalTabBarItem(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                icon = {
                    Icon(
                        icon = if (selectedTab == 2) Icons.Filled.Setting else Icons.Outlined.Setting,
                        contentDescription = "Settings",
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun YamalTabBarDisabledPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalTabBar {
            YamalTabBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.Home, contentDescription = "Home") },
                label = {
                    Text(
                        text = "Home",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
            )
            YamalTabBarItem(
                selected = false,
                onClick = {},
                enabled = false,
                icon = { Icon(icon = Icons.Outlined.Lock, contentDescription = "Locked") },
                label = {
                    Text(
                        text = "Locked",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
            )
            YamalTabBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.Setting, contentDescription = "Settings") },
                label = {
                    Text(
                        text = "Settings",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun YamalTabBarCustomColorsPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        val customColors =
            TabBarItemDefaults.colors(
                selectedContentColor = YamalTheme.colors.functionalColors.success,
                unselectedContentColor = YamalTheme.colors.neutralColors.secondaryText,
            )

        YamalTabBar {
            YamalTabBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.Home, contentDescription = "Home") },
                label = {
                    Text(
                        text = "Home",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
                colors = customColors,
            )
            YamalTabBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(icon = Icons.Outlined.User, contentDescription = "Profile") },
                label = {
                    Text(
                        text = "Profile",
                        fontSize = TabBarDefaults.TitleFontSize,
                        lineHeight = TabBarDefaults.TitleLineHeight,
                    )
                },
                colors = customColors,
            )
        }
    }
}
