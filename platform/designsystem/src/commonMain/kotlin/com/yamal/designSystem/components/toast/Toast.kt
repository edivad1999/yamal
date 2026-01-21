package com.yamal.designSystem.components.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Toast icon types following Ant Design Mobile specifications.
 */
@Immutable
enum class ToastIcon {
    /** Success state with checkmark icon */
    Success,

    /** Fail/Error state with X icon */
    Fail,

    /** Loading state with spinner */
    Loading,

    /** No icon, text only */
    None,
}

/**
 * Toast position following Ant Design Mobile specifications.
 */
@Immutable
enum class ToastPosition {
    /** 20% from top */
    Top,

    /** Centered (50%) */
    Center,

    /** 80% from top (20% from bottom) */
    Bottom,
}

/**
 * Default values for Toast component following Ant Design Mobile specifications.
 *
 * ADM CSS values:
 * - Background: rgba(0, 0, 0, 0.7)
 * - Border radius: 8dp
 * - Max width: 204dp
 * - Text padding: 12dp
 * - Icon padding: 35dp vertical, 12dp horizontal
 * - Icon size: 36dp
 * - Default duration: 2000ms
 *
 * @see <a href="https://mobile.ant.design/components/toast">Ant Design Mobile Toast</a>
 */
object ToastDefaults {
    /** Default toast duration in milliseconds */
    const val DURATION_MS = 2000L

    /** Animation duration */
    const val ANIMATION_DURATION_MS = 200

    /** Toast background color: rgba(0, 0, 0, 0.7) */
    val backgroundColor: Color = Color(0xB3000000)

    /** Toast text color */
    val contentColor: Color = Color.White

    /** Toast max width: 204dp */
    val maxWidth = 204.dp

    /** Toast min width for icon mode: 150dp */
    val minWidthWithIcon = 150.dp

    /** Border radius: 8dp */
    val borderRadius = 8.dp

    /** Text-only padding: 12dp */
    val textPadding = 12.dp

    /** Icon mode vertical padding: 35dp */
    val iconVerticalPadding = 35.dp

    /** Icon mode horizontal padding: 12dp */
    val iconHorizontalPadding = 12.dp

    /** Icon size: 36dp */
    val iconSize = 36.dp

    /** Loading spinner size: 48dp */
    val loadingSize = 48.dp

    /** Icon margin bottom: 8dp */
    val iconSpacing = 8.dp
}

/**
 * Toast component following Ant Design Mobile specifications.
 *
 * Toast shows a brief message that appears on top of the UI and automatically dismisses.
 *
 * @param visible Whether the toast is visible
 * @param content The message to display
 * @param icon The icon type to display (success, fail, loading, or none)
 * @param position The position on screen (top, center, bottom)
 * @param duration Duration in milliseconds before auto-dismiss. Set to 0 to disable auto-dismiss.
 * @param onDismiss Callback when toast should be dismissed
 * @param modifier Modifier for the toast overlay
 */
@Composable
fun Toast(
    visible: Boolean,
    content: String,
    modifier: Modifier = Modifier,
    icon: ToastIcon = ToastIcon.None,
    position: ToastPosition = ToastPosition.Center,
    duration: Long = ToastDefaults.DURATION_MS,
    onDismiss: () -> Unit = {},
) {
    // Auto-dismiss after duration (unless loading or duration is 0)
    LaunchedEffect(visible, duration, icon) {
        if (visible && duration > 0 && icon != ToastIcon.Loading) {
            delay(duration)
            onDismiss()
        }
    }

    val alignment =
        when (position) {
            ToastPosition.Top -> BiasAlignment(0f, -0.6f)

            // 20% from top
            ToastPosition.Center -> Alignment.Center

            ToastPosition.Bottom -> BiasAlignment(0f, 0.6f) // 80% from top
        }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = alignment,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter =
                fadeIn(tween(ToastDefaults.ANIMATION_DURATION_MS)) +
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(ToastDefaults.ANIMATION_DURATION_MS),
                    ),
            exit =
                fadeOut(tween(ToastDefaults.ANIMATION_DURATION_MS)) +
                    scaleOut(
                        targetScale = 0.8f,
                        animationSpec = tween(ToastDefaults.ANIMATION_DURATION_MS),
                    ),
        ) {
            ToastContent(
                content = content,
                icon = icon,
            )
        }
    }
}

@Composable
private fun ToastContent(
    content: String,
    icon: ToastIcon,
) {
    val hasIcon = icon != ToastIcon.None

    val padding =
        if (hasIcon) {
            Modifier.padding(
                horizontal = ToastDefaults.iconHorizontalPadding,
                vertical = ToastDefaults.iconVerticalPadding,
            )
        } else {
            Modifier.padding(ToastDefaults.textPadding)
        }

    val sizeModifier =
        if (hasIcon) {
            Modifier.widthIn(min = ToastDefaults.minWidthWithIcon, max = ToastDefaults.maxWidth)
        } else {
            Modifier.widthIn(max = ToastDefaults.maxWidth)
        }

    Box(
        modifier =
            sizeModifier
                .clip(RoundedCornerShape(ToastDefaults.borderRadius))
                .background(ToastDefaults.backgroundColor)
                .then(padding),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ToastDefaults.iconSpacing),
        ) {
            when (icon) {
                ToastIcon.Success -> {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        modifier = Modifier.size(ToastDefaults.iconSize),
                        tint = ToastDefaults.contentColor,
                    )
                }

                ToastIcon.Fail -> {
                    Icon(
                        Icons.Filled.CloseCircle,
                        contentDescription = "Error",
                        modifier = Modifier.size(ToastDefaults.iconSize),
                        tint = ToastDefaults.contentColor,
                    )
                }

                ToastIcon.Loading -> {
                    SpinLoadingIndicator(
                        modifier = Modifier.size(ToastDefaults.loadingSize),
                        color = ToastDefaults.contentColor,
                    )
                }

                ToastIcon.None -> {
                    // No icon
                }
            }

            Text(
                text = content,
                color = ToastDefaults.contentColor,
                style = YamalTheme.typography.body,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// Previews

@Preview
@Composable
private fun ToastTextOnlyPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                Toast(
                    visible = true,
                    content = "This is a toast message",
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToastSuccessPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                Toast(
                    visible = true,
                    content = "Added to list",
                    icon = ToastIcon.Success,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToastFailPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                Toast(
                    visible = true,
                    content = "Failed to save",
                    icon = ToastIcon.Fail,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToastLoadingPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                Toast(
                    visible = true,
                    content = "Loading...",
                    icon = ToastIcon.Loading,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToastPositionsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                Toast(
                    visible = true,
                    content = "Top toast",
                    position = ToastPosition.Top,
                )
            }
        }
    }
}

/**
 * Composable state holder for managing toast visibility.
 *
 * Usage:
 * ```
 * val toastState = rememberToastState()
 *
 * // Show toast
 * toastState.show("Message", icon = ToastIcon.Success)
 *
 * // In composable
 * ToastHost(state = toastState)
 * ```
 */
class ToastState {
    var visible by mutableStateOf(false)
        private set
    var content by mutableStateOf("")
        private set
    var icon by mutableStateOf(ToastIcon.None)
        private set
    var position by mutableStateOf(ToastPosition.Center)
        private set
    var duration by mutableStateOf(ToastDefaults.DURATION_MS)
        private set

    fun show(
        message: String,
        icon: ToastIcon = ToastIcon.None,
        position: ToastPosition = ToastPosition.Center,
        duration: Long = ToastDefaults.DURATION_MS,
    ) {
        this.content = message
        this.icon = icon
        this.position = position
        this.duration = duration
        this.visible = true
    }

    fun showSuccess(
        message: String,
        duration: Long = ToastDefaults.DURATION_MS,
    ) {
        show(message, ToastIcon.Success, duration = duration)
    }

    fun showError(
        message: String,
        duration: Long = ToastDefaults.DURATION_MS,
    ) {
        show(message, ToastIcon.Fail, duration = duration)
    }

    fun showLoading(message: String = "Loading...") {
        show(message, ToastIcon.Loading, duration = 0)
    }

    fun hide() {
        visible = false
    }
}

/**
 * Creates and remembers a [ToastState].
 */
@Composable
fun rememberToastState(): ToastState = remember { ToastState() }

/**
 * Host composable that displays toasts from a [ToastState].
 *
 * Place this at the root of your screen/scaffold.
 *
 * @param state The toast state to observe
 * @param modifier Modifier for the toast overlay
 */
@Composable
fun ToastHost(
    state: ToastState,
    modifier: Modifier = Modifier,
) {
    Toast(
        visible = state.visible,
        content = state.content,
        icon = state.icon,
        position = state.position,
        duration = state.duration,
        onDismiss = { state.hide() },
        modifier = modifier,
    )
}
