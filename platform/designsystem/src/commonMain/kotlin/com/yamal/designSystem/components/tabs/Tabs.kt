package com.yamal.designSystem.components.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton

/**
 * TODO: Implement proper Tabs component following Ant Design Mobile specifications.
 *
 * Ant Design Mobile Tabs: https://mobile.ant.design/components/tabs
 *
 * Required features:
 * - Horizontal scrollable tabs
 * - Active tab indicator animation
 * - Support for swipe gestures between tab contents
 * - Badge support on tabs
 * - Custom tab bar rendering
 *
 * Current implementation is a temporary placeholder using buttons in a LazyRow.
 */
@Composable
fun Tabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(tabs) { index, tab ->
            YamalButton(
                text = tab,
                onClick = { onTabSelected(index) },
                color = if (selectedIndex == index) ButtonColor.Primary else ButtonColor.Default,
                fill = if (selectedIndex == index) ButtonFill.Solid else ButtonFill.Outline,
                size = ButtonSize.Small,
            )
        }
    }
}
