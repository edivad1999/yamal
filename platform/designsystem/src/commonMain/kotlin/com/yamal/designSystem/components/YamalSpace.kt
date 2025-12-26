package com.yamal.designSystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Space size following Ant Design guidelines.
 */
@Immutable
enum class YamalSpaceSize {
    Small,
    Middle,
    Large,
}

/**
 * Space direction following Ant Design guidelines.
 */
@Immutable
enum class YamalSpaceDirection {
    Horizontal,
    Vertical,
}

/**
 * Space alignment following Ant Design guidelines.
 */
@Immutable
enum class YamalSpaceAlign {
    Start,
    End,
    Center,
    Baseline,
}

/**
 * A space component following Ant Design guidelines.
 * Provides consistent spacing between inline elements.
 *
 * @param modifier Modifier for the space container
 * @param size Spacing size (Small: 8dp, Middle: 16dp, Large: 24dp)
 * @param customSize Custom spacing in Dp (overrides size)
 * @param direction Layout direction (Horizontal or Vertical)
 * @param align Alignment of items
 * @param wrap Whether to wrap items (horizontal only)
 * @param content Content items
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YamalSpace(
    modifier: Modifier = Modifier,
    size: YamalSpaceSize = YamalSpaceSize.Small,
    customSize: Dp? = null,
    direction: YamalSpaceDirection = YamalSpaceDirection.Horizontal,
    align: YamalSpaceAlign = YamalSpaceAlign.Center,
    wrap: Boolean = false,
    content: @Composable () -> Unit,
) {
    val spacing =
        customSize ?: when (size) {
            YamalSpaceSize.Small -> Dimension.Spacing.xs
            YamalSpaceSize.Middle -> Dimension.Spacing.md
            YamalSpaceSize.Large -> Dimension.Spacing.lg
        }

    val horizontalAlignment =
        when (align) {
            YamalSpaceAlign.Start -> Alignment.Start
            YamalSpaceAlign.End -> Alignment.End
            YamalSpaceAlign.Center -> Alignment.CenterHorizontally
            YamalSpaceAlign.Baseline -> Alignment.Start
        }

    val verticalAlignment =
        when (align) {
            YamalSpaceAlign.Start -> Alignment.Top
            YamalSpaceAlign.End -> Alignment.Bottom
            YamalSpaceAlign.Center -> Alignment.CenterVertically
            YamalSpaceAlign.Baseline -> Alignment.CenterVertically
        }

    when {
        direction == YamalSpaceDirection.Horizontal && wrap -> {
            FlowRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) {
                content()
            }
        }

        direction == YamalSpaceDirection.Horizontal -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = verticalAlignment,
            ) {
                content()
            }
        }

        else -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalAlignment = horizontalAlignment,
            ) {
                content()
            }
        }
    }
}

/**
 * Compact space for form components following Ant Design Space.Compact.
 * Removes spacing between items and merges borders.
 */
@Composable
fun YamalSpaceCompact(
    modifier: Modifier = Modifier,
    direction: YamalSpaceDirection = YamalSpaceDirection.Horizontal,
    content: @Composable () -> Unit,
) {
    when (direction) {
        YamalSpaceDirection.Horizontal -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy((-1).dp),
            ) {
                content()
            }
        }

        YamalSpaceDirection.Vertical -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy((-1).dp),
            ) {
                content()
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalSpaceHorizontalPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Small spacing:")
                YamalSpace(size = YamalSpaceSize.Small) {
                    YamalButton(text = "Button 1", onClick = {}, color = ButtonColor.Primary)
                    YamalButton(text = "Button 2", onClick = {}, color = ButtonColor.Default)
                    YamalButton(text = "Button 3", onClick = {}, color = ButtonColor.Default)
                }

                Text("Middle spacing:")
                YamalSpace(size = YamalSpaceSize.Middle) {
                    YamalButton(text = "Button 1", onClick = {}, color = ButtonColor.Primary)
                    YamalButton(text = "Button 2", onClick = {}, color = ButtonColor.Default)
                }

                Text("Large spacing:")
                YamalSpace(size = YamalSpaceSize.Large) {
                    YamalButton(text = "Button 1", onClick = {}, color = ButtonColor.Primary)
                    YamalButton(text = "Button 2", onClick = {}, color = ButtonColor.Default)
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalSpaceVerticalPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            YamalSpace(
                modifier = Modifier.padding(16.dp),
                direction = YamalSpaceDirection.Vertical,
                size = YamalSpaceSize.Middle,
            ) {
                YamalButton(text = "Button 1", onClick = {}, color = ButtonColor.Primary)
                YamalButton(text = "Button 2", onClick = {}, color = ButtonColor.Default)
                YamalButton(text = "Button 3", onClick = {}, color = ButtonColor.Default)
            }
        }
    }
}

@Preview
@Composable
private fun YamalSpaceWrapPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            YamalSpace(
                modifier = Modifier.padding(16.dp),
                wrap = true,
                size = YamalSpaceSize.Small,
            ) {
                repeat(10) {
                    YamalTag(text = "Tag $it")
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalSpaceAlignPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Align Start:")
                YamalSpace(align = YamalSpaceAlign.Start) {
                    Text("Text", style = YamalTheme.typography.h3)
                    YamalButton(text = "Button", onClick = {}, size = ButtonSize.Small)
                }

                Text("Align Center:")
                YamalSpace(align = YamalSpaceAlign.Center) {
                    Text("Text", style = YamalTheme.typography.h3)
                    YamalButton(text = "Button", onClick = {}, size = ButtonSize.Small)
                }

                Text("Align End:")
                YamalSpace(align = YamalSpaceAlign.End) {
                    Text("Text", style = YamalTheme.typography.h3)
                    YamalButton(text = "Button", onClick = {}, size = ButtonSize.Small)
                }
            }
        }
    }
}
