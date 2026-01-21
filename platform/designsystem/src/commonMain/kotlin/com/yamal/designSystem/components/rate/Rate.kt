package com.yamal.designSystem.components.rate

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Default values for Rate component following Ant Design Mobile specifications.
 *
 * ADM CSS values:
 * - Star size: 32dp (--star-size)
 * - Active color: #FFAF13 (warning color)
 * - Inactive color: #CCCCCC (light/border color)
 * - Spacing: 8dp between stars
 *
 * @see <a href="https://mobile.ant.design/components/rate">Ant Design Mobile Rate</a>
 */
object RateDefaults {
    /** Default star size: 32dp */
    val starSize = 32.dp

    /** Spacing between stars: 8dp */
    val starSpacing = 8.dp

    /** Animation duration */
    const val ANIMATION_DURATION_MS = 150

    /** Default number of stars */
    const val DEFAULT_COUNT = 5

    /** Active (filled) star color - warning color */
    val activeColor: Color
        @Composable get() = YamalTheme.colors.warning

    /** Inactive (empty) star color */
    val inactiveColor: Color
        @Composable get() = YamalTheme.colors.light

    /** Inactive color for half stars */
    val inactiveColorHalf: Color
        @Composable get() = YamalTheme.colors.border
}

/**
 * Rate component following Ant Design Mobile specifications.
 *
 * A star rating component that allows users to rate content or display existing ratings.
 *
 * Features:
 * - Customizable star count
 * - Half-star support
 * - Read-only mode for displaying ratings
 * - Interactive mode for user input
 * - Clear rating on tap when allowClear is true
 *
 * @param value Current rating value (0 to count)
 * @param onValueChange Callback when rating changes (null for read-only mode)
 * @param modifier Modifier for the rate component
 * @param count Number of stars to display (default 5)
 * @param allowHalf Whether to allow half-star ratings (default false)
 * @param allowClear Whether clicking the same rating clears it (default true)
 * @param readOnly Whether the component is in read-only mode (default false when onValueChange is null)
 * @param size Size of each star icon (default 32dp)
 * @param activeColor Color for filled stars (default warning color)
 * @param inactiveColor Color for empty stars (default light color)
 *
 * Example usage:
 * ```
 * // Basic interactive rating
 * var rating by remember { mutableStateOf(0f) }
 * Rate(
 *     value = rating,
 *     onValueChange = { rating = it }
 * )
 *
 * // Half-star ratings
 * Rate(
 *     value = 3.5f,
 *     onValueChange = { rating = it },
 *     allowHalf = true
 * )
 *
 * // Read-only display
 * Rate(
 *     value = 4.5f,
 *     onValueChange = null,
 *     allowHalf = true
 * )
 *
 * // Custom star count and size
 * Rate(
 *     value = rating,
 *     onValueChange = { rating = it },
 *     count = 10,
 *     size = 24.dp
 * )
 * ```
 */
@Composable
fun Rate(
    value: Float,
    onValueChange: ((Float) -> Unit)?,
    modifier: Modifier = Modifier,
    count: Int = RateDefaults.DEFAULT_COUNT,
    allowHalf: Boolean = false,
    allowClear: Boolean = true,
    readOnly: Boolean = onValueChange == null,
    size: Dp = RateDefaults.starSize,
    activeColor: Color = RateDefaults.activeColor,
    inactiveColor: Color = RateDefaults.inactiveColor,
) {
    // Track container position for gesture calculations
    var containerWidth by remember { mutableStateOf(0f) }
    var containerX by remember { mutableStateOf(0f) }

    val starList = remember(count) { List(count) { it + 1 } }

    // Calculate value from x position
    fun calculateValueFromX(x: Float): Float {
        if (containerWidth <= 0f) return value

        val relativeX = (x - containerX).coerceIn(0f, containerWidth)
        val rawValue = (relativeX / containerWidth) * count

        return if (allowHalf) {
            (ceil(rawValue * 2f) / 2f).coerceIn(0f, count.toFloat())
        } else {
            ceil(rawValue).coerceIn(0f, count.toFloat())
        }
    }

    Row(
        modifier =
            modifier
                .onGloballyPositioned { coordinates ->
                    containerWidth = coordinates.size.width.toFloat()
                    containerX = coordinates.positionInParent().x
                }.then(
                    if (!readOnly && onValueChange != null) {
                        Modifier
                            .pointerInput(value, count, allowHalf, allowClear) {
                                detectTapGestures { offset ->
                                    val newValue = calculateValueFromX(offset.x)

                                    // Allow clearing if tapping the same value
                                    if (allowClear && newValue == value) {
                                        onValueChange(0f)
                                    } else {
                                        onValueChange(newValue)
                                    }
                                }
                            }.pointerInput(value, count, allowHalf) {
                                detectDragGestures { change, _ ->
                                    val newValue = calculateValueFromX(change.position.x)
                                    if (newValue != value) {
                                        onValueChange(newValue)
                                    }
                                }
                            }
                    } else {
                        Modifier
                    },
                ).semantics {
                    contentDescription = "Rating: $value out of $count stars"
                },
        horizontalArrangement = Arrangement.spacedBy(RateDefaults.starSpacing),
    ) {
        starList.forEach { starIndex ->
            Box {
                // If allowHalf, render both half and full overlays
                if (allowHalf) {
                    // Half star (left side)
                    StarIcon(
                        active = value >= starIndex - 0.5f,
                        fillRatio =
                            when {
                                value >= starIndex -> 0.5f

                                // Full value, show half for the "half" layer
                                value >= starIndex - 0.5f -> 0.5f

                                // Half value, show half
                                else -> 0f // No fill
                            },
                        isHalfLayer = true,
                        size = size,
                        activeColor = activeColor,
                        inactiveColor = inactiveColor,
                        readOnly = readOnly,
                    )
                }

                // Full star
                StarIcon(
                    active = value >= starIndex,
                    fillRatio =
                        when {
                            value >= starIndex -> 1f
                            allowHalf && value >= starIndex - 0.5f -> 0.5f
                            else -> 0f
                        },
                    isHalfLayer = false,
                    size = size,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    readOnly = readOnly,
                )
            }
        }
    }
}

/**
 * Individual star icon with partial fill support.
 */
@Composable
private fun StarIcon(
    active: Boolean,
    fillRatio: Float,
    isHalfLayer: Boolean,
    size: Dp,
    activeColor: Color,
    inactiveColor: Color,
    readOnly: Boolean,
) {
    val animatedColor by animateColorAsState(
        targetValue = if (active) activeColor else inactiveColor,
        animationSpec = tween(RateDefaults.ANIMATION_DURATION_MS),
    )

    Box(
        modifier =
            Modifier
                .size(size)
                .then(
                    if (fillRatio > 0f && fillRatio < 1f) {
                        // Apply clipping for partial fill
                        Modifier.drawWithContent {
                            clipRect(
                                left = if (isHalfLayer) 0f else 0f,
                                top = 0f,
                                right = size.toPx() * fillRatio,
                                bottom = size.toPx(),
                                clipOp = ClipOp.Intersect,
                            ) {
                                this@drawWithContent.drawContent()
                            }
                        }
                    } else if (fillRatio == 0f) {
                        // Don't render if no fill
                        Modifier.size(0.dp)
                    } else {
                        Modifier
                    },
                ).semantics {
                    contentDescription = if (readOnly) "Star" else "Rate star"
                },
    ) {
        Icon(
            icon = Icons.Filled.Star,
            contentDescription = null,
            tint = animatedColor,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

// Previews

@Preview
@Composable
private fun RateBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Basic Interactive", style = YamalTheme.typography.bodyMedium)

                var rating1 by remember { mutableStateOf(0f) }
                Rate(
                    value = rating1,
                    onValueChange = { rating1 = it },
                )

                Text("Value: $rating1", style = YamalTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
private fun RateHalfStarPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Half Star Support", style = YamalTheme.typography.bodyMedium)

                var rating by remember { mutableStateOf(2.5f) }
                Rate(
                    value = rating,
                    onValueChange = { rating = it },
                    allowHalf = true,
                )

                Text("Value: $rating", style = YamalTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
private fun RateReadOnlyPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Read-only Display", style = YamalTheme.typography.bodyMedium)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                ) {
                    Rate(
                        value = 4.5f,
                        onValueChange = null,
                        allowHalf = true,
                    )
                    Text("4.5", style = YamalTheme.typography.caption)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
                ) {
                    Rate(
                        value = 3f,
                        onValueChange = null,
                    )
                    Text("3.0", style = YamalTheme.typography.caption)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RateVariantsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Different Values", style = YamalTheme.typography.bodyMedium)

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(value = 0f, onValueChange = null)
                    Text(" 0 stars", style = YamalTheme.typography.caption)
                }

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(value = 1f, onValueChange = null)
                    Text(" 1 star", style = YamalTheme.typography.caption)
                }

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(value = 2.5f, onValueChange = null, allowHalf = true)
                    Text(" 2.5 stars", style = YamalTheme.typography.caption)
                }

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(value = 4f, onValueChange = null)
                    Text(" 4 stars", style = YamalTheme.typography.caption)
                }

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(value = 5f, onValueChange = null)
                    Text(" 5 stars", style = YamalTheme.typography.caption)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RateCustomSizePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Custom Sizes", style = YamalTheme.typography.bodyMedium)

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(
                        value = 4f,
                        onValueChange = null,
                        size = 20.dp,
                    )
                    Text(" Small (20dp)", style = YamalTheme.typography.caption)
                }

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(
                        value = 4f,
                        onValueChange = null,
                        size = 32.dp,
                    )
                    Text(" Default (32dp)", style = YamalTheme.typography.caption)
                }

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Rate(
                        value = 4f,
                        onValueChange = null,
                        size = 48.dp,
                    )
                    Text(" Large (48dp)", style = YamalTheme.typography.caption)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RateCustomCountPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Custom Star Count", style = YamalTheme.typography.bodyMedium)

                var rating3 by remember { mutableStateOf(1f) }
                Rate(
                    value = rating3,
                    onValueChange = { rating3 = it },
                    count = 3,
                )
                Text("3 stars - Value: $rating3", style = YamalTheme.typography.caption)

                var rating10 by remember { mutableStateOf(7f) }
                Rate(
                    value = rating10,
                    onValueChange = { rating10 = it },
                    count = 10,
                    size = 24.dp,
                )
                Text("10 stars - Value: $rating10", style = YamalTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
private fun RateAllowClearPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Allow Clear (tap same value to clear)", style = YamalTheme.typography.bodyMedium)

                var rating by remember { mutableStateOf(3f) }
                Rate(
                    value = rating,
                    onValueChange = { rating = it },
                    allowClear = true,
                )

                Text("Value: $rating (tap 3 stars to clear)", style = YamalTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
private fun RateCustomColorsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(Dimension.Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            ) {
                Text("Custom Colors", style = YamalTheme.typography.bodyMedium)

                Rate(
                    value = 4f,
                    onValueChange = null,
                    activeColor = YamalTheme.colors.danger,
                    inactiveColor = YamalTheme.colors.border,
                )
                Text("Red (danger) stars", style = YamalTheme.typography.caption)

                Rate(
                    value = 3f,
                    onValueChange = null,
                    activeColor = YamalTheme.colors.success,
                    inactiveColor = YamalTheme.colors.border,
                )
                Text("Green (success) stars", style = YamalTheme.typography.caption)

                Rate(
                    value = 5f,
                    onValueChange = null,
                    activeColor = YamalTheme.colors.primary,
                    inactiveColor = YamalTheme.colors.border,
                )
                Text("Blue (primary) stars", style = YamalTheme.typography.caption)
            }
        }
    }
}
