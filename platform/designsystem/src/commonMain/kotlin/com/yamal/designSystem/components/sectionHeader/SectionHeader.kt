package com.yamal.designSystem.components.sectionHeader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Section header component with title and optional action button.
 *
 * Following common mobile app patterns for content sections.
 *
 * Example usage:
 * ```
 * SectionHeader(
 *     title = "Trending Anime",
 *     actionText = "See All",
 *     onActionClick = { /* navigate */ }
 * )
 * ```
 *
 * @param title The section title text
 * @param modifier Modifier for the header
 * @param actionText Optional action button text (e.g., "See All")
 * @param onActionClick Optional callback when action is clicked
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = YamalTheme.typography.titleLarge,
            color = YamalTheme.colors.text,
        )

        if (actionText != null && onActionClick != null) {
            YamalButton(
                text = actionText,
                onClick = onActionClick,
                color = ButtonColor.Primary,
                fill = ButtonFill.None,
                size = ButtonSize.Small,
            )
        }
    }
}

// Previews

@Preview
@Composable
private fun SectionHeaderPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            SectionHeader(
                title = "Trending Anime",
                actionText = "See All",
                onActionClick = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun SectionHeaderNoActionPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            SectionHeader(
                title = "My Collection",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
