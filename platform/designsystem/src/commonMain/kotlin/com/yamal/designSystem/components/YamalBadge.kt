package com.yamal.designSystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Badge status following Ant Design guidelines.
 */
@Immutable
enum class YamalBadgeStatus {
    Success,
    Processing,
    Default,
    Error,
    Warning,
}

/**
 * Badge size following Ant Design guidelines.
 */
@Immutable
enum class YamalBadgeSize {
    Default,
    Small,
}

/**
 * A badge component following Ant Design guidelines.
 *
 * @param modifier Modifier for the badge container
 * @param count Number to display (shows overflow when > overflowCount)
 * @param dot Show dot instead of count
 * @param showZero Show badge when count is 0
 * @param overflowCount Max count to display before showing "+"
 * @param color Custom badge color
 * @param size Badge size
 * @param offset Position offset for the badge
 * @param content Content to wrap with badge
 */
@Composable
fun YamalBadge(
    modifier: Modifier = Modifier,
    count: Int? = null,
    dot: Boolean = false,
    showZero: Boolean = false,
    overflowCount: Int = 99,
    color: Color? = null,
    size: YamalBadgeSize = YamalBadgeSize.Default,
    offset: IntOffset = IntOffset(0, 0),
    content: @Composable () -> Unit,
) {
    val colors = YamalTheme.colors
    val badgeColor = color ?: colors.functionalColors.error

    val shouldShow =
        when {
            dot -> true
            count == null -> false
            count == 0 -> showZero
            else -> true
        }

    Box(modifier = modifier) {
        content()

        if (shouldShow) {
            val badgeModifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .offset { offset }

            if (dot) {
                // Dot badge
                val dotSize =
                    when (size) {
                        YamalBadgeSize.Default -> 8.dp
                        YamalBadgeSize.Small -> 6.dp
                    }
                Box(
                    modifier =
                        badgeModifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(badgeColor),
                )
            } else if (count != null) {
                // Count badge
                val displayText = if (count > overflowCount) "$overflowCount+" else count.toString()
                val minSize =
                    when (size) {
                        YamalBadgeSize.Default -> 20.dp
                        YamalBadgeSize.Small -> 16.dp
                    }
                val fontSize =
                    when (size) {
                        YamalBadgeSize.Default -> 12.sp
                        YamalBadgeSize.Small -> 10.sp
                    }

                Surface(
                    modifier = badgeModifier,
                    shape = RoundedCornerShape(minSize / 2),
                    color = badgeColor,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .defaultMinSize(minWidth = minSize, minHeight = minSize)
                                .padding(horizontal = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = displayText,
                            color = Color.White,
                            fontSize = fontSize,
                        )
                    }
                }
            }
        }
    }
}

/**
 * A status badge with text following Ant Design Badge with status.
 */
@Composable
fun YamalStatusBadge(
    status: YamalBadgeStatus,
    modifier: Modifier = Modifier,
    text: String? = null,
    color: Color? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val statusColor =
        color ?: when (status) {
            YamalBadgeStatus.Success -> colors.functionalColors.success
            YamalBadgeStatus.Processing -> colors.paletteColors.color6
            YamalBadgeStatus.Default -> colors.neutralColors.secondaryText
            YamalBadgeStatus.Error -> colors.functionalColors.error
            YamalBadgeStatus.Warning -> colors.functionalColors.warning
        }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
    ) {
        Box(
            modifier =
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(statusColor),
        )
        text?.let {
            Text(
                text = it,
                style = typography.body,
                color = colors.neutralColors.primaryText,
            )
        }
    }
}

@Composable
private fun Modifier.defaultMinSize(
    minWidth: Dp,
    minHeight: Dp,
): Modifier =
    this.then(
        Modifier.size(width = minWidth, height = minHeight),
    )

// Previews

@Preview
@Composable
private fun YamalBadgeCountPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                YamalBadge(count = 5) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.neutralColors.divider,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                YamalBadge(count = 100, overflowCount = 99) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.neutralColors.divider,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                YamalBadge(count = 0, showZero = true) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.neutralColors.divider,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalBadgeDotPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                YamalBadge(dot = true) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.neutralColors.divider,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }

                YamalBadge(dot = true, color = YamalTheme.colors.paletteColors.color6) {
                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    YamalTheme.colors.neutralColors.divider,
                                    RoundedCornerShape(4.dp),
                                ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun YamalStatusBadgePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YamalStatusBadge(status = YamalBadgeStatus.Success, text = "Success")
                YamalStatusBadge(status = YamalBadgeStatus.Processing, text = "Processing")
                YamalStatusBadge(status = YamalBadgeStatus.Default, text = "Default")
                YamalStatusBadge(status = YamalBadgeStatus.Error, text = "Error")
                YamalStatusBadge(status = YamalBadgeStatus.Warning, text = "Warning")
            }
        }
    }
}
