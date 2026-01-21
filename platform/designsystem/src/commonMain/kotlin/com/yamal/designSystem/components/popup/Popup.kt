package com.yamal.designSystem.components.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme

/**
 * Position of the popup on the screen.
 */
enum class PopupPosition {
    /**
     * Popup slides from the bottom of the screen (default for mobile bottom sheets).
     */
    Bottom,

    /**
     * Popup slides from the top of the screen.
     */
    Top,

    /**
     * Popup slides from the left edge of the screen.
     */
    Left,

    /**
     * Popup slides from the right edge of the screen.
     */
    Right,
}

/**
 * A versatile popup/bottom sheet component following Ant Design Mobile patterns.
 *
 * Features:
 * - Multiple positions (bottom, top, left, right)
 * - Animated entrance and exit
 * - Optional backdrop/scrim with click-outside-to-dismiss
 * - Optional close button
 * - Optional handle bar (for bottom sheet UX)
 * - Customizable styling
 *
 * Example usage:
 * ```kotlin
 * var showPopup by remember { mutableStateOf(false) }
 *
 * YamalPopup(
 *     visible = showPopup,
 *     onDismiss = { showPopup = false },
 *     position = PopupPosition.Bottom,
 *     showHandleBar = true,
 * ) {
 *     // Your content here
 *     Text("Hello from bottom sheet!")
 * }
 * ```
 *
 * @param visible Whether the popup is currently visible
 * @param onDismiss Callback when the popup should be dismissed
 * @param modifier Modifier for the popup container
 * @param position Position from which the popup appears
 * @param showMask Whether to show a backdrop/scrim behind the popup
 * @param closeOnMaskClick Whether clicking the backdrop dismisses the popup
 * @param showCloseButton Whether to show a close button in the top-right corner
 * @param showHandleBar Whether to show a handle bar at the top (bottom sheet style)
 * @param maskColor Color of the backdrop/scrim
 * @param backgroundColor Background color of the popup content
 * @param content The content to display in the popup
 */
@Composable
fun YamalPopup(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    position: PopupPosition = PopupPosition.Bottom,
    showMask: Boolean = true,
    closeOnMaskClick: Boolean = true,
    showCloseButton: Boolean = false,
    showHandleBar: Boolean = false,
    maskColor: Color = Color.Black.copy(alpha = 0.55f),
    backgroundColor: Color = YamalTheme.colors.background,
    content: @Composable () -> Unit,
) {
    // Track animation state independently from visibility to prevent premature content removal
    val animationState = remember { MutableTransitionState(false) }

    LaunchedEffect(visible) {
        animationState.targetState = visible
    }

    // Only show dialog when animation is active or about to become active
    if (animationState.currentState || animationState.targetState) {
        Dialog(
            onDismissRequest = {
                if (closeOnMaskClick) {
                    onDismiss()
                }
            },
            properties =
                DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = closeOnMaskClick,
                    usePlatformDefaultWidth = false,
                ),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment =
                    when (position) {
                        PopupPosition.Bottom -> Alignment.BottomCenter
                        PopupPosition.Top -> Alignment.TopCenter
                        PopupPosition.Left -> Alignment.CenterStart
                        PopupPosition.Right -> Alignment.CenterEnd
                    },
            ) {
                // Backdrop/Scrim
                if (showMask) {
                    AnimatedVisibility(
                        visibleState = animationState,
                        enter = fadeIn(animationSpec = tween(durationMillis = 200)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 200)),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(maskColor)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            if (closeOnMaskClick) {
                                                onDismiss()
                                            }
                                        },
                                    ),
                        )
                    }
                }

                // Popup content with slide animation
                AnimatedVisibility(
                    visibleState = animationState,
                    enter =
                        when (position) {
                            PopupPosition.Bottom -> {
                                slideInVertically(
                                    animationSpec =
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium,
                                            visibilityThreshold = IntOffset.VisibilityThreshold,
                                        ),
                                    initialOffsetY = { it },
                                )
                            }

                            PopupPosition.Top -> {
                                slideInVertically(
                                    animationSpec =
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium,
                                            visibilityThreshold = IntOffset.VisibilityThreshold,
                                        ),
                                    initialOffsetY = { -it },
                                )
                            }

                            PopupPosition.Left -> {
                                slideInHorizontally(
                                    animationSpec =
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium,
                                            visibilityThreshold = IntOffset.VisibilityThreshold,
                                        ),
                                    initialOffsetX = { -it },
                                )
                            }

                            PopupPosition.Right -> {
                                slideInHorizontally(
                                    animationSpec =
                                        spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium,
                                            visibilityThreshold = IntOffset.VisibilityThreshold,
                                        ),
                                    initialOffsetX = { it },
                                )
                            }
                        },
                    exit =
                        when (position) {
                            PopupPosition.Bottom -> {
                                slideOutVertically(
                                    animationSpec = tween(durationMillis = 250),
                                    targetOffsetY = { it },
                                )
                            }

                            PopupPosition.Top -> {
                                slideOutVertically(
                                    animationSpec = tween(durationMillis = 250),
                                    targetOffsetY = { -it },
                                )
                            }

                            PopupPosition.Left -> {
                                slideOutHorizontally(
                                    animationSpec = tween(durationMillis = 250),
                                    targetOffsetX = { -it },
                                )
                            }

                            PopupPosition.Right -> {
                                slideOutHorizontally(
                                    animationSpec = tween(durationMillis = 250),
                                    targetOffsetX = { it },
                                )
                            }
                        },
                ) {
                    Box(
                        modifier =
                            modifier
                                .then(
                                    when (position) {
                                        PopupPosition.Bottom, PopupPosition.Top -> Modifier.fillMaxWidth()
                                        PopupPosition.Left, PopupPosition.Right -> Modifier.fillMaxHeight()
                                    },
                                ).clip(
                                    when (position) {
                                        PopupPosition.Bottom -> {
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                            )
                                        }

                                        PopupPosition.Top -> {
                                            RoundedCornerShape(
                                                bottomStart = 16.dp,
                                                bottomEnd = 16.dp,
                                            )
                                        }

                                        PopupPosition.Left -> {
                                            RoundedCornerShape(
                                                topEnd = 16.dp,
                                                bottomEnd = 16.dp,
                                            )
                                        }

                                        PopupPosition.Right -> {
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                bottomStart = 16.dp,
                                            )
                                        }
                                    },
                                ).background(backgroundColor),
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Handle bar (typically for bottom sheets)
                            if (showHandleBar && (position == PopupPosition.Bottom || position == PopupPosition.Top)) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = Dimension.Spacing.sm),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(40.dp)
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(YamalTheme.colors.weak),
                                    )
                                }
                            }

                            // Main content
                            Box {
                                content()

                                // Close button (top-right corner)
                                if (showCloseButton) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .align(
                                                    when (position) {
                                                        PopupPosition.Bottom, PopupPosition.Top -> Alignment.TopEnd
                                                        PopupPosition.Left -> Alignment.TopEnd
                                                        PopupPosition.Right -> Alignment.TopStart
                                                    },
                                                ).padding(Dimension.Spacing.md)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null,
                                                    onClick = onDismiss,
                                                ),
                                    ) {
                                        Icon(
                                            icon = Icons.Outlined.Close,
                                            contentDescription = "Close",
                                            tint = YamalTheme.colors.weak,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * A simpler bottom sheet variant with opinionated defaults.
 *
 * This is a convenience wrapper around [YamalPopup] with bottom sheet specific defaults.
 *
 * @param visible Whether the bottom sheet is currently visible
 * @param onDismiss Callback when the bottom sheet should be dismissed
 * @param modifier Modifier for the bottom sheet container
 * @param showHandleBar Whether to show a handle bar at the top (default: true)
 * @param closeOnMaskClick Whether clicking the backdrop dismisses the sheet (default: true)
 * @param showCloseButton Whether to show a close button (default: false)
 * @param content The content to display in the bottom sheet
 */
@Composable
fun YamalBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showHandleBar: Boolean = true,
    closeOnMaskClick: Boolean = true,
    showCloseButton: Boolean = false,
    content: @Composable () -> Unit,
) {
    YamalPopup(
        visible = visible,
        onDismiss = onDismiss,
        modifier = modifier,
        position = PopupPosition.Bottom,
        showMask = true,
        closeOnMaskClick = closeOnMaskClick,
        showCloseButton = showCloseButton,
        showHandleBar = showHandleBar,
        content = content,
    )
}

/**
 * Preview for Bottom position popup with handle bar.
 */
@Composable
private fun PreviewBottomPopup() {
    var visible by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(YamalTheme.colors.box)) {
        YamalPopup(
            visible = visible,
            onDismiss = { visible = false },
            position = PopupPosition.Bottom,
            showHandleBar = true,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimension.Spacing.lg),
            ) {
                com.yamal.designSystem.components.text.Text(
                    text = "Bottom Sheet Example",
                    style = YamalTheme.typography.titleLarge,
                    color = YamalTheme.colors.text,
                )
                com.yamal.designSystem.components.text.Text(
                    text = "This is a bottom sheet with a handle bar. Swipe down or tap outside to dismiss.",
                    style = YamalTheme.typography.body,
                    color = YamalTheme.colors.textSecondary,
                    modifier = Modifier.padding(top = Dimension.Spacing.sm),
                )
            }
        }
    }
}

/**
 * Preview for Top position popup.
 */
@Composable
private fun PreviewTopPopup() {
    var visible by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(YamalTheme.colors.box)) {
        YamalPopup(
            visible = visible,
            onDismiss = { visible = false },
            position = PopupPosition.Top,
            showCloseButton = true,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimension.Spacing.lg),
            ) {
                com.yamal.designSystem.components.text.Text(
                    text = "Top Notification",
                    style = YamalTheme.typography.title,
                    color = YamalTheme.colors.text,
                )
                com.yamal.designSystem.components.text.Text(
                    text = "Important message from the top!",
                    style = YamalTheme.typography.body,
                    color = YamalTheme.colors.textSecondary,
                    modifier = Modifier.padding(top = Dimension.Spacing.xs),
                )
            }
        }
    }
}

/**
 * Preview for Left side popup (drawer style).
 */
@Composable
private fun PreviewLeftPopup() {
    var visible by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(YamalTheme.colors.box)) {
        YamalPopup(
            visible = visible,
            onDismiss = { visible = false },
            position = PopupPosition.Left,
            showCloseButton = true,
            modifier = Modifier.width(280.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(Dimension.Spacing.lg),
            ) {
                com.yamal.designSystem.components.text.Text(
                    text = "Side Menu",
                    style = YamalTheme.typography.titleLarge,
                    color = YamalTheme.colors.text,
                )
                com.yamal.designSystem.components.text.Text(
                    text = "Navigation drawer from the left",
                    style = YamalTheme.typography.body,
                    color = YamalTheme.colors.textSecondary,
                    modifier = Modifier.padding(top = Dimension.Spacing.sm),
                )
            }
        }
    }
}

/**
 * Preview for Right side popup (drawer style).
 */
@Composable
private fun PreviewRightPopup() {
    var visible by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(YamalTheme.colors.box)) {
        YamalPopup(
            visible = visible,
            onDismiss = { visible = false },
            position = PopupPosition.Right,
            showCloseButton = true,
            modifier = Modifier.width(280.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(Dimension.Spacing.lg),
            ) {
                com.yamal.designSystem.components.text.Text(
                    text = "Filters",
                    style = YamalTheme.typography.titleLarge,
                    color = YamalTheme.colors.text,
                )
                com.yamal.designSystem.components.text.Text(
                    text = "Filter panel from the right",
                    style = YamalTheme.typography.body,
                    color = YamalTheme.colors.textSecondary,
                    modifier = Modifier.padding(top = Dimension.Spacing.sm),
                )
            }
        }
    }
}

/**
 * Preview for YamalBottomSheet convenience wrapper.
 */
@Composable
private fun PreviewBottomSheet() {
    var visible by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(YamalTheme.colors.box)) {
        YamalBottomSheet(
            visible = visible,
            onDismiss = { visible = false },
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimension.Spacing.lg),
            ) {
                com.yamal.designSystem.components.text.Text(
                    text = "YamalBottomSheet",
                    style = YamalTheme.typography.titleLarge,
                    color = YamalTheme.colors.text,
                )
                com.yamal.designSystem.components.text.Text(
                    text = "Convenience wrapper with opinionated defaults for bottom sheets.",
                    style = YamalTheme.typography.body,
                    color = YamalTheme.colors.textSecondary,
                    modifier = Modifier.padding(top = Dimension.Spacing.sm),
                )
            }
        }
    }
}
