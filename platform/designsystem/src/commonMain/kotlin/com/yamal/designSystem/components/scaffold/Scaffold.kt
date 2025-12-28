package com.yamal.designSystem.components.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yamal.designSystem.theme.YamalTheme

/**
 * Yamal Design System Scaffold component.
 *
 * Provides a basic layout structure with slots for top bar, bottom bar, and content.
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
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        backgroundColor = backgroundColor,
        content = content,
    )
}
