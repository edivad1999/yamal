package com.yamal.designSystem.components.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.foundation.LocalContentColor
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [Input] component following Ant Design Mobile specifications.
 */
object InputDefaults {
    /** Font size: 17sp (--adm-font-size-9) */
    val FontSize = 17.sp

    /** Minimum height: 24dp */
    val MinHeight: Dp = 24.dp

    /** Clear button padding: 4dp */
    val ClearButtonPadding: Dp = 4.dp

    /** Clear button margin left: 8dp */
    val ClearButtonMarginLeft: Dp = 8.dp

    /** Clear icon size: 18dp */
    val ClearIconSize: Dp = 18.dp

    /** Line height multiplier */
    const val LINE_HEIGHT_MULTIPLIER = 1.5f

    /** Disabled opacity: 0.4 */
    const val DISABLED_ALPHA = 0.4f

    /** Text color (--adm-color-text) */
    val textColor: Color
        @Composable get() = YamalTheme.colors.text

    /** Placeholder color (--adm-color-light) */
    val placeholderColor: Color
        @Composable get() = YamalTheme.colors.light

    /** Clear button color (--adm-color-light) */
    val clearColor: Color
        @Composable get() = YamalTheme.colors.light

    /** Clear button active color (--adm-color-weak) */
    val clearActiveColor: Color
        @Composable get() = YamalTheme.colors.weak

    /** Cursor color */
    val cursorColor: Color
        @Composable get() = YamalTheme.colors.primary
}

/**
 * Input component following Ant Design Mobile specifications.
 *
 * A basic text input field for keyboard-based text entry in forms.
 * It can work independently or be combined with List or Form components for enhanced layouts.
 *
 * @param value Current input value
 * @param onValueChange Callback when value changes
 * @param modifier Modifier for the component
 * @param placeholder Placeholder text shown when input is empty
 * @param clearable Whether to show a clear button when there is input
 * @param clearIcon Custom clear icon composable. When null, uses default CloseCircleFill icon
 * @param onlyShowClearWhenFocus Whether to only show clear button when focused (default: true)
 * @param onClear Callback when clear button is clicked
 * @param onEnterPress Callback when Enter key is pressed
 * @param enabled Whether the input is enabled (default: true). When false, the input is disabled.
 * @param readOnly Whether the input is read-only
 * @param maxLength Maximum character length (for text types only)
 * @param keyboardType Type of keyboard to show (text, number, email, etc.)
 * @param imeAction IME action button type
 * @param keyboardActions Keyboard actions for handling IME events
 * @param visualTransformation Visual transformation for the input (e.g., password mask)
 *
 * Example usage:
 * ```
 * // Basic input
 * var text by remember { mutableStateOf("") }
 * Input(
 *     value = text,
 *     onValueChange = { text = it },
 *     placeholder = "Enter text"
 * )
 *
 * // With clear button
 * Input(
 *     value = text,
 *     onValueChange = { text = it },
 *     clearable = true,
 *     onClear = { text = "" }
 * )
 *
 * // Password input
 * Input(
 *     value = password,
 *     onValueChange = { password = it },
 *     placeholder = "Enter password",
 *     visualTransformation = PasswordVisualTransformation()
 * )
 *
 * // Disabled input
 * Input(
 *     value = "Disabled",
 *     onValueChange = {},
 *     enabled = false
 * )
 * ```
 */
@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    clearable: Boolean = false,
    clearIcon: @Composable (() -> Unit)? = null,
    onlyShowClearWhenFocus: Boolean = true,
    onClear: (() -> Unit)? = null,
    onEnterPress: (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    maxLength: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    var isFocused by remember { mutableStateOf(false) }

    val textStyle =
        TextStyle(
            fontSize = InputDefaults.FontSize,
            color = InputDefaults.textColor,
            lineHeight = InputDefaults.FontSize * InputDefaults.LINE_HEIGHT_MULTIPLIER,
        )

    val effectiveKeyboardActions =
        remember(keyboardActions, onEnterPress) {
            if (onEnterPress != null) {
                KeyboardActions(
                    onDone = { onEnterPress() },
                    onGo = { onEnterPress() },
                    onNext = keyboardActions.onNext,
                    onPrevious = keyboardActions.onPrevious,
                    onSearch = { onEnterPress() },
                    onSend = { onEnterPress() },
                )
            } else {
                keyboardActions
            }
        }

    val showClearButton =
        clearable &&
            value.isNotEmpty() &&
            enabled &&
            !readOnly &&
            (!onlyShowClearWhenFocus || isFocused)

    Row(
        modifier =
            modifier
                .heightIn(min = InputDefaults.MinHeight)
                .alpha(if (enabled) 1f else InputDefaults.DISABLED_ALPHA),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val effectiveValue =
                    if (maxLength != null) {
                        newValue.take(maxLength)
                    } else {
                        newValue
                    }
                onValueChange(effectiveValue)
            },
            modifier =
                Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction,
                ),
            keyboardActions = effectiveKeyboardActions,
            singleLine = true,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(InputDefaults.cursorColor),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = InputDefaults.placeholderColor),
                        )
                    }
                    innerTextField()
                }
            },
        )

        // Clear button
        AnimatedVisibility(
            visible = showClearButton,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Box(
                modifier =
                    Modifier
                        .padding(start = InputDefaults.ClearButtonMarginLeft)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onValueChange("")
                                onClear?.invoke()
                            },
                        ).padding(InputDefaults.ClearButtonPadding),
            ) {
                if (clearIcon != null) {
                    CompositionLocalProvider(LocalContentColor provides InputDefaults.clearActiveColor) {
                        clearIcon()
                    }
                } else {
                    Icon(
                        icon = Icons.Filled.CloseCircle,
                        contentDescription = "Clear",
                        modifier = Modifier.size(InputDefaults.ClearIconSize),
                        tint = InputDefaults.clearActiveColor,
                    )
                }
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun InputBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text1 by remember { mutableStateOf("") }
                var text2 by remember { mutableStateOf("Hello World") }

                Text(
                    "Basic Input",
                    style = YamalTheme.typography.bodyMedium,
                )
                Input(
                    value = text1,
                    onValueChange = { text1 = it },
                    placeholder = "Enter text here",
                    modifier = Modifier.fillMaxWidth(),
                )
                Input(
                    value = text2,
                    onValueChange = { text2 = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun InputClearablePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text by remember { mutableStateOf("Clear me") }

                Text(
                    "Clearable Input",
                    style = YamalTheme.typography.bodyMedium,
                )
                Input(
                    value = text,
                    onValueChange = { text = it },
                    clearable = true,
                    onlyShowClearWhenFocus = false,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(YamalTheme.colors.box)
                            .padding(12.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun InputDisabledPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Disabled & ReadOnly",
                    style = YamalTheme.typography.bodyMedium,
                )
                Input(
                    value = "Disabled input",
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                Input(
                    value = "Read-only input",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun InputMaxLengthPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text by remember { mutableStateOf("") }

                Text(
                    "Max Length (10 chars)",
                    style = YamalTheme.typography.bodyMedium,
                )
                Input(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = "Max 10 characters",
                    maxLength = 10,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    "${text.length}/10",
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.textSecondary,
                )
            }
        }
    }
}
