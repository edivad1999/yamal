package com.yamal.designSystem.components.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.theme.YamalTheme

/**
 * Yamal Design System Scaffold component.
 *
 * A custom scaffold implementation without Material dependency that provides
 * a basic layout structure with slots for top bar, bottom bar, and content.
 *
 * The scaffold measures the top and bottom bars and provides appropriate
 * padding values to the content to ensure proper layout without overlap.
 *
 * @param modifier Modifier for the scaffold
 * @param topBar Content to place at the top (e.g., YamalNavBar)
 * @param bottomBar Content to place at the bottom (e.g., YamalTabBar)
 * @param backgroundColor Background color of the scaffold
 * @param content Main content. Receives PaddingValues to offset for bars.
 *
 * Example:
 * ```
 * YamalScaffold(
 *     topBar = { YamalNavBar(title = { Text("Title") }) }
 * ) { padding ->
 *     LazyColumn(modifier = Modifier.padding(padding)) { ... }
 * }
 * ```
 */
@Composable
fun YamalScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    backgroundColor: Color = YamalTheme.colors.box,
    content: @Composable (PaddingValues) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = backgroundColor,
    ) {
        SubcomposeLayout { constraints ->
            val layoutWidth = constraints.maxWidth
            val layoutHeight = constraints.maxHeight
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            // Measure top bar
            val topBarPlaceables =
                subcompose(ScaffoldLayoutContent.TopBar, topBar).map {
                    it.measure(looseConstraints)
                }
            val topBarHeight = topBarPlaceables.maxOfOrNull { it.height } ?: 0

            // Measure bottom bar
            val bottomBarPlaceables =
                subcompose(ScaffoldLayoutContent.BottomBar, bottomBar).map {
                    it.measure(looseConstraints)
                }
            val bottomBarHeight = bottomBarPlaceables.maxOfOrNull { it.height } ?: 0

            // Calculate content padding
            val contentPadding =
                PaddingValues(
                    top = (topBarHeight / density).dp,
                    bottom = (bottomBarHeight / density).dp,
                )

            // Measure content with full available space (it will use padding internally)
            val contentPlaceables =
                subcompose(ScaffoldLayoutContent.MainContent) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        content(contentPadding)
                    }
                }.map { it.measure(looseConstraints.copy(maxHeight = layoutHeight)) }

            layout(layoutWidth, layoutHeight) {
                // Place content first (behind bars)
                contentPlaceables.forEach { it.placeRelative(0, 0) }

                // Place top bar
                topBarPlaceables.forEach { it.placeRelative(0, 0) }

                // Place bottom bar at the bottom
                bottomBarPlaceables.forEach {
                    it.placeRelative(0, layoutHeight - bottomBarHeight)
                }
            }
        }
    }
}

/**
 * Enum for scaffold layout content slots used in subcomposition.
 */
private enum class ScaffoldLayoutContent {
    TopBar,
    BottomBar,
    MainContent,
}
