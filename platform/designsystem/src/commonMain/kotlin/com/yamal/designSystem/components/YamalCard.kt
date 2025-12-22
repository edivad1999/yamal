package com.yamal.designSystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Card size following Ant Design guidelines.
 */
@Immutable
enum class YamalCardSize {
    Default,
    Small,
}

/**
 * Card variant following Ant Design guidelines (5.24+).
 */
@Immutable
enum class YamalCardVariant {
    Outlined,
    Borderless,
}

/**
 * A card component following Ant Design guidelines.
 *
 * @param modifier Modifier for the card
 * @param size Card size (Default or Small)
 * @param variant Card variant (Outlined or Borderless)
 * @param hoverable Whether the card shows hover effect
 * @param loading Whether to show loading skeleton
 * @param title Optional card title
 * @param extra Optional content in top-right corner
 * @param cover Optional cover content (image, etc.)
 * @param actions Optional action items at bottom
 * @param onClick Optional click handler
 * @param content Card body content
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun YamalCard(
    modifier: Modifier = Modifier,
    size: YamalCardSize = YamalCardSize.Default,
    variant: YamalCardVariant = YamalCardVariant.Outlined,
    hoverable: Boolean = false,
    loading: Boolean = false,
    title: String? = null,
    extra: (@Composable RowScope.() -> Unit)? = null,
    cover: (@Composable () -> Unit)? = null,
    actions: List<@Composable () -> Unit>? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = YamalTheme.colors

    val padding =
        when (size) {
            YamalCardSize.Default -> Dimension.Spacing.lg
            YamalCardSize.Small -> Dimension.Spacing.sm
        }

    val shape: Shape = RoundedCornerShape(Dimension.BorderRadius.lg)
    val border =
        when (variant) {
            YamalCardVariant.Outlined -> BorderStroke(1.dp, colors.neutralColors.border)
            YamalCardVariant.Borderless -> null
        }

    val baseElevation = if (variant == YamalCardVariant.Borderless) 0f else 1f
    val elevation by animateFloatAsState(
        targetValue = if (hoverable) 4f else baseElevation,
    )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            backgroundColor = colors.neutralColors.background,
            contentColor = colors.neutralColors.primaryText,
            border = border,
            elevation = elevation.dp,
        ) {
            CardContent(
                loading = loading,
                title = title,
                extra = extra,
                cover = cover,
                actions = actions,
                padding = padding,
                content = content,
            )
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            backgroundColor = colors.neutralColors.background,
            contentColor = colors.neutralColors.primaryText,
            border = border,
            elevation = elevation.dp,
        ) {
            CardContent(
                loading = loading,
                title = title,
                extra = extra,
                cover = cover,
                actions = actions,
                padding = padding,
                content = content,
            )
        }
    }
}

@Composable
private fun CardContent(
    loading: Boolean,
    title: String?,
    extra: (@Composable RowScope.() -> Unit)?,
    cover: (@Composable () -> Unit)?,
    actions: List<@Composable () -> Unit>?,
    padding: Dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    Column {
        // Cover
        cover?.let {
            Box(modifier = Modifier.fillMaxWidth()) {
                it()
            }
        }

        // Header with title and extra
        if (title != null || extra != null) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = padding, vertical = Dimension.Spacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                title?.let {
                    Text(
                        text = it,
                        style = typography.h5,
                        color = colors.neutralColors.title,
                    )
                }
                extra?.let {
                    Row(content = it)
                }
            }
            Divider(color = colors.neutralColors.divider)
        }

        // Body content
        if (loading) {
            // Simple loading skeleton
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding),
            ) {
                repeat(3) { index ->
                    Surface(
                        modifier =
                            Modifier
                                .fillMaxWidth(if (index == 2) 0.6f else 1f)
                                .padding(vertical = Dimension.Spacing.xxs),
                        color = colors.neutralColors.divider,
                        shape = RoundedCornerShape(Dimension.BorderRadius.xs),
                    ) {
                        Box(modifier = Modifier.padding(vertical = Dimension.Spacing.xs))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(padding),
                content = content,
            )
        }

        // Actions
        actions?.let { actionList ->
            if (actionList.isNotEmpty()) {
                Divider(color = colors.neutralColors.divider)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    actionList.forEachIndexed { index, action ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            action()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card.Meta component for displaying avatar, title, and description.
 */
@Composable
fun YamalCardMeta(
    modifier: Modifier = Modifier,
    avatar: (@Composable () -> Unit)? = null,
    title: String? = null,
    description: String? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
    ) {
        avatar?.invoke()
        Column {
            title?.let {
                Text(
                    text = it,
                    style = typography.bodyMedium,
                    color = colors.neutralColors.title,
                )
            }
            description?.let {
                Text(
                    text = it,
                    style = typography.body,
                    color = colors.neutralColors.secondaryText,
                )
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalCardDefaultPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalCard(
                    title = "Card Title",
                    extra = { Text("More", color = YamalTheme.colors.paletteColors.color6) },
                ) {
                    Text("Card content goes here")
                }

                YamalCard(
                    size = YamalCardSize.Small,
                    title = "Small Card",
                ) {
                    Text("Small card content")
                }

                YamalCard(
                    loading = true,
                    title = "Loading Card",
                ) {
                    Text("This won't show")
                }

                YamalCard(
                    variant = YamalCardVariant.Borderless,
                ) {
                    Text("Borderless card")
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalCardWithActionsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            YamalCard(
                modifier = Modifier.padding(16.dp),
                title = "Card with Actions",
                actions =
                    listOf(
                        { Text("Edit") },
                        { Text("Delete") },
                        { Text("More") },
                    ),
            ) {
                YamalCardMeta(
                    title = "Card Meta Title",
                    description = "This is the description",
                )
            }
        }
    }
}
