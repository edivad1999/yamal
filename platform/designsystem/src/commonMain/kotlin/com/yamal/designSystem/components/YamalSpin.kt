package com.yamal.designSystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Spin size following Ant Design guidelines.
 */
@Immutable
enum class YamalSpinSize {
    Small,
    Default,
    Large,
}

/**
 * A spin/loading component following Ant Design guidelines.
 *
 * @param modifier Modifier for the spin
 * @param size Spin size (Small, Default, Large)
 * @param spinning Whether the spinner is visible
 * @param delay Delay in milliseconds before showing (prevents flashing)
 * @param tip Optional description text below spinner
 */
@Composable
fun YamalSpin(
    modifier: Modifier = Modifier,
    size: YamalSpinSize = YamalSpinSize.Default,
    spinning: Boolean = true,
    delay: Long = 0,
    tip: String? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    var showSpinner by remember { mutableStateOf(delay == 0L) }

    LaunchedEffect(spinning, delay) {
        if (spinning && delay > 0) {
            delay(delay)
            showSpinner = true
        } else if (!spinning) {
            showSpinner = false
        } else {
            showSpinner = true
        }
    }

    val spinnerSize: Dp =
        when (size) {
            YamalSpinSize.Small -> 14.dp
            YamalSpinSize.Default -> 24.dp
            YamalSpinSize.Large -> 40.dp
        }

    val strokeWidth: Dp =
        when (size) {
            YamalSpinSize.Small -> 2.dp
            YamalSpinSize.Default -> 3.dp
            YamalSpinSize.Large -> 4.dp
        }

    AnimatedVisibility(
        visible = spinning && showSpinner,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(spinnerSize),
                color = colors.paletteColors.color6,
                strokeWidth = strokeWidth,
            )

            tip?.let {
                Text(
                    text = it,
                    style = typography.body,
                    color = colors.paletteColors.color6,
                )
            }
        }
    }
}

/**
 * A spin wrapper that shows a loading overlay over content.
 *
 * @param spinning Whether the spinner is visible
 * @param modifier Modifier for the container
 * @param size Spin size
 * @param delay Delay in milliseconds before showing
 * @param tip Optional description text
 * @param content Content to wrap
 */
@Composable
fun YamalSpinContainer(
    spinning: Boolean,
    modifier: Modifier = Modifier,
    size: YamalSpinSize = YamalSpinSize.Default,
    delay: Long = 0,
    tip: String? = null,
    content: @Composable () -> Unit,
) {
    val colors = YamalTheme.colors

    Box(modifier = modifier) {
        content()

        if (spinning) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(colors.neutralColors.background.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center,
            ) {
                YamalSpin(
                    size = size,
                    spinning = true,
                    delay = delay,
                    tip = tip,
                )
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalSpinSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                YamalSpin(size = YamalSpinSize.Small)
                YamalSpin(size = YamalSpinSize.Default)
                YamalSpin(size = YamalSpinSize.Large)
            }
        }
    }
}

@Preview
@Composable
private fun YamalSpinWithTipPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            YamalSpin(
                modifier = Modifier.padding(16.dp),
                tip = "Loading...",
            )
        }
    }
}

@Preview
@Composable
private fun YamalSpinContainerPreview() {
    YamalTheme {
        YamalSpinContainer(
            spinning = true,
            modifier = Modifier.padding(16.dp),
            tip = "Loading content...",
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("This is some content")
                Text("That is being loaded")
                Text("With a spinner overlay")
            }
        }
    }
}
