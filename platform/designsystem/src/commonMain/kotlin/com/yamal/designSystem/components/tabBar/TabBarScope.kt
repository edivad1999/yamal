package com.yamal.designSystem.components.tabBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.preview.PlatformPreviewContextConfigurationEffect
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Scope for building tab bar items using a DSL-style builder pattern.
 *
 * This provides an alternative to the composable [RowScope.YamalTabBarItem] approach,
 * allowing items to be defined declaratively using the [item] function.
 *
 * Example usage:
 * ```
 * var selectedTab by remember { mutableStateOf(0) }
 *
 * YamalTabBar(
 *     items = {
 *         item(
 *             selected = selectedTab == 0,
 *             onClick = { selectedTab = 0 },
 *             icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
 *             label = { Text("Home") },
 *         )
 *         item(
 *             selected = selectedTab == 1,
 *             onClick = { selectedTab = 1 },
 *             icon = { Icon(Icons.Outlined.User, contentDescription = "Profile") },
 *             label = { Text("Profile") },
 *         )
 *     }
 * )
 * ```
 */
sealed interface TabBarScope {
    /**
     * Adds a tab bar item to the tab bar.
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
     *   item in different states. If null, [TabBarItemDefaults.colors] will be used.
     */
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        label: @Composable (() -> Unit)? = null,
        badge: TabBarBadge? = null,
        colors: TabBarItemColors? = null,
    )
}

/**
 * Internal data class representing a tab bar item's configuration.
 */
internal class TabBarItem(
    val selected: Boolean,
    val onClick: () -> Unit,
    val icon: @Composable () -> Unit,
    val modifier: Modifier,
    val enabled: Boolean,
    val label: @Composable (() -> Unit)?,
    val badge: TabBarBadge?,
    val colors: TabBarItemColors?,
)

/**
 * Provider interface for accessing tab bar items.
 */
internal interface TabBarItemProvider {
    val itemsCount: Int
    val itemList: List<TabBarItem>
}

/**
 * Implementation of [TabBarScope] that collects items into a list.
 */
private class TabBarScopeImpl :
    TabBarScope,
    TabBarItemProvider {
    private val _itemList = mutableListOf<TabBarItem>()

    override fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        label: @Composable (() -> Unit)?,
        badge: TabBarBadge?,
        colors: TabBarItemColors?,
    ) {
        _itemList.add(
            TabBarItem(
                selected = selected,
                onClick = onClick,
                icon = icon,
                modifier = modifier,
                enabled = enabled,
                label = label,
                badge = badge,
                colors = colors,
            ),
        )
    }

    override val itemList: List<TabBarItem>
        get() = _itemList

    override val itemsCount: Int
        get() = _itemList.size
}

/**
 * Remembers the state of tab bar items built using [TabBarScope].
 *
 * @param content The content builder using [TabBarScope].
 * @return A [State] containing the [TabBarItemProvider] with all defined items.
 */
@Composable
internal fun rememberStateOfItems(content: TabBarScope.() -> Unit): State<TabBarItemProvider> {
    val latestContent = rememberUpdatedState(content)
    return remember {
        derivedStateOf { TabBarScopeImpl().apply(latestContent.value) }
    }
}

/**
 * TabBar with DSL-style builder pattern for defining items.
 *
 * This overload provides an alternative API using [TabBarScope] instead of composable content.
 * Use this when you prefer a declarative builder pattern over composable lambdas.
 *
 * Example:
 * ```
 * var selectedTab by remember { mutableStateOf(0) }
 *
 * YamalTabBar(items = {
 *     item(
 *         selected = selectedTab == 0,
 *         onClick = { selectedTab = 0 },
 *         icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
 *         label = { Text("Home") },
 *     )
 *     item(
 *         selected = selectedTab == 1,
 *         onClick = { selectedTab = 1 },
 *         icon = { Icon(Icons.Outlined.User, contentDescription = "Profile") },
 *         label = { Text("Profile") },
 *     )
 * })
 * ```
 *
 * @param items The builder for defining tab bar items using [TabBarScope].
 * @param modifier The [Modifier] to be applied to this TabBar.
 * @param containerColor The color used for the background of this tab bar.
 * @param contentColor The preferred content color provided by this tab bar to its children.
 */
@Composable
fun YamalTabBar(
    items: TabBarScope.() -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = TabBarDefaults.containerColor,
    contentColor: Color = TabBarDefaults.contentColor,
) {
    val itemProvider by rememberStateOfItems(items)

    YamalTabBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        itemProvider.itemList.forEach { item ->
            YamalTabBarItem(
                selected = item.selected,
                onClick = item.onClick,
                icon = item.icon,
                modifier = item.modifier,
                enabled = item.enabled,
                label = item.label,
                badge = item.badge,
                colors = item.colors ?: TabBarItemDefaults.colors(),
            )
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalTabBarBuilderPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        var selectedTab by remember { mutableStateOf(0) }

        YamalTabBar(
            items = {
                item(
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
                item(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            icon =
                                if (selectedTab == 1) {
                                    Icons.Filled.CheckCircle
                                } else {
                                    Icons.Outlined.CheckCircle
                                },
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
                item(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            icon =
                                if (selectedTab == 2) {
                                    Icons.Filled.Message
                                } else {
                                    Icons.Outlined.Message
                                },
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
                    badge = TabBarBadge.Count(5),
                )
                item(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            icon =
                                if (selectedTab == 3) {
                                    Icons.Filled.Profile
                                } else {
                                    Icons.Outlined.User
                                },
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
            },
        )
    }
}

@Preview
@Composable
private fun YamalTabBarBuilderWithBadgesPreview() {
    PlatformPreviewContextConfigurationEffect()
    YamalTheme {
        YamalTabBar(
            items = {
                item(
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
                item(
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
                item(
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
                    badge = TabBarBadge.Count(99),
                )
            },
        )
    }
}
