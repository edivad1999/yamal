package com.yamal.designSystem.components.empty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.ProvideTextStyle
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [Empty] component following Ant Design Mobile specifications.
 */
object EmptyDefaults {
    /** Image width: 64dp */
    val ImageWidth: Dp = 64.dp

    /** Vertical padding: 24dp */
    val VerticalPadding: Dp = 24.dp

    /** Description margin top: 8dp */
    val DescriptionMarginTop: Dp = 8.dp

    /** Description font size: 14sp (adm-font-size-6) */
    val DescriptionFontSize = 14.sp

    /** Description text color - --adm-color-light */
    val descriptionColor: Color
        @Composable get() = YamalTheme.colors.light

    /** Default content padding */
    val contentPadding: PaddingValues
        @Composable get() = PaddingValues(vertical = VerticalPadding)
}

/**
 * Empty state component following Ant Design Mobile specifications.
 *
 * Used to display a placeholder when no data is available.
 *
 * @param modifier Modifier for the empty component
 * @param imageContent Optional composable slot for custom image content.
 *   When null, displays the default empty illustration.
 * @param description Optional composable slot for description content below the image.
 *
 * Example usage:
 * ```
 * // Basic empty state with default image
 * Empty()
 *
 * // With description
 * Empty(
 *     description = { Text("No data available") }
 * )
 *
 * // With custom image
 * Empty(
 *     imageContent = {
 *         Icon(
 *             icon = Icons.Outlined.Search,
 *             modifier = Modifier.size(64.dp)
 *         )
 *     },
 *     description = { Text("No search results") }
 * )
 *
 * // Full customization
 * Empty(
 *     imageContent = { MyCustomIllustration() },
 *     description = {
 *         Column {
 *             Text("Nothing here yet")
 *             Button(onClick = { }) { Text("Add item") }
 *         }
 *     }
 * )
 * ```
 */
@Composable
fun Empty(
    modifier: Modifier = Modifier,
    imageContent: @Composable (() -> Unit)? = null,
    description: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(EmptyDefaults.contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Image container
        Box(
            modifier = Modifier.width(EmptyDefaults.ImageWidth).aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (imageContent != null) {
                imageContent()
            } else {
                EmptyDefaultImage()
            }
        }

        // Description
        if (description != null) {
            Spacer(Modifier.height(EmptyDefaults.DescriptionMarginTop))
            ProvideTextStyle(
                value =
                    androidx.compose.ui.text.TextStyle(
                        fontSize = EmptyDefaults.DescriptionFontSize,
                        color = EmptyDefaults.descriptionColor,
                    ),
            ) {
                description()
            }
        }
    }
}

/**
 * Convenience overload with string description.
 *
 * @param description Description text to display below the image
 * @param modifier Modifier for the empty component
 * @param imageContent Optional composable slot for custom image content
 */
@Composable
fun Empty(
    description: String,
    modifier: Modifier = Modifier,
    imageContent: @Composable (() -> Unit)? = null,
) {
    Empty(
        modifier = modifier,
        imageContent = imageContent,
        description = { Text(description) },
    )
}

/**
 * Default empty illustration following Ant Design Mobile style.
 */
@Composable
private fun EmptyDefaultImage() {
    val colors = YamalTheme.colors
    val borderColor = colors.border
    Icon(
        Icons.Outlined.Inbox,
        null,
        modifier = Modifier.fillMaxSize(),
        tint = borderColor,
    )
}

// Previews

@Preview
@Composable
private fun EmptyDefaultPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Empty(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
private fun EmptyWithDescriptionPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Empty(
                modifier = Modifier.fillMaxSize(),
                description = "No data available",
            )
        }
    }
}

@Preview
@Composable
private fun EmptyWithCustomImagePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Empty(
                modifier = Modifier.fillMaxSize(),
                imageContent = {
                    Icon(
                        Icons.Outlined.QuestionCircle,
                        null,
                        modifier = Modifier.fillMaxSize(),
                    )
                },
                description = { Text("Custom illustration") },
            )
        }
    }
}
