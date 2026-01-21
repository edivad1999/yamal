package com.yamal.designSystem.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Size variants for Floating Action Button
 */
enum class FabSize {
    /** Normal size: 56dp */
    Normal,

    /** Small size: 40dp */
    Small,

    /** Large size: 96dp (extended FAB) */
    Large,
}

/**
 * Floating Action Button (FAB) following Material Design guidelines.
 *
 * FABs are used for the primary action in an application. They float above content
 * and are typically circular with an icon.
 *
 * @param onClick Callback when FAB is clicked
 * @param modifier Modifier for the FAB
 * @param backgroundColor Background color of the FAB
 * @param contentColor Color of the icon/content
 * @param size Size variant of the FAB
 * @param elevation Elevation of the FAB
 * @param content Icon or content to display
 *
 * Example usage:
 * ```
 * YamalFloatingActionButton(
 *     onClick = { /* Add new item */ }
 * ) {
 *     Icon(Icons.Outlined.Plus, contentDescription = "Add")
 * }
 * ```
 */
@Composable
fun YamalFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = YamalTheme.colors.primary,
    contentColor: Color = YamalTheme.colors.textLightSolid,
    size: FabSize = FabSize.Normal,
    elevation: Dp = 6.dp,
    content: @Composable () -> Unit,
) {
    val fabSize =
        when (size) {
            FabSize.Small -> 40.dp
            FabSize.Normal -> 56.dp
            FabSize.Large -> 96.dp
        }

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(fabSize),
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation =
            FloatingActionButtonDefaults.elevation(
                defaultElevation = elevation,
                pressedElevation = elevation + 2.dp,
            ),
        shape = CircleShape,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

// Previews

@Preview
@Composable
private fun FloatingActionButtonPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalFloatingActionButton(onClick = {}) {
                    Icon(
                        icon = Icons.Outlined.Plus,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp),
                    )
                }

                YamalFloatingActionButton(
                    onClick = {},
                    size = FabSize.Small,
                ) {
                    Icon(
                        icon = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}
