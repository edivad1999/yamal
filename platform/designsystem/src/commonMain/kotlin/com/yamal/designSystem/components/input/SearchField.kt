package com.yamal.designSystem.components.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme

/**
 * SearchField is a specialized input field for search operations.
 * It displays a search icon on the left and optionally a clear button on the right.
 *
 * @param value Current search query
 * @param onValueChange Callback when search query changes
 * @param modifier Modifier for the component
 * @param placeholder Placeholder text
 * @param onSearch Callback when search is submitted (Enter pressed)
 * @param enabled Whether the search field is enabled
 *
 * Example usage:
 * ```
 * var searchQuery by remember { mutableStateOf("") }
 * SearchField(
 *     value = searchQuery,
 *     onValueChange = { searchQuery = it },
 *     placeholder = "Search titles...",
 *     onSearch = { performSearch(searchQuery) }
 * )
 * ```
 */
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    onSearch: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.box,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon = Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = YamalTheme.colors.light,
            )
            Spacer(Modifier.width(8.dp))
            Input(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = placeholder,
                clearable = true,
                onlyShowClearWhenFocus = false,
                onEnterPress = onSearch,
                enabled = enabled,
                imeAction = ImeAction.Search,
            )
        }
    }
}
