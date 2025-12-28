package com.yamal.designSystem.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default values for [TextArea] component following Ant Design Mobile specifications.
 */
object TextAreaDefaults {
    /** Font size: 17sp (--adm-font-size-9) */
    val FontSize = 17.sp

    /** Default number of rows */
    const val DEFAULT_ROWS = 2

    /** Line height multiplier */
    const val LINE_HEIGHT_MULTIPLIER = 1.5f

    /** Count padding top: 8dp */
    val CountPaddingTop: Dp = 8.dp

    /** Disabled opacity: 0.4 */
    const val DISABLED_ALPHA = 0.4f

    /** Text color (--adm-color-text) */
    val textColor: Color
        @Composable get() = YamalTheme.colors.text

    /** Placeholder color (--adm-color-light) */
    val placeholderColor: Color
        @Composable get() = YamalTheme.colors.border

    /** Disabled text color (--adm-color-weak) */
    val disabledColor: Color
        @Composable get() = YamalTheme.colors.textSecondary

    /** Count text color */
    val countColor: Color
        @Composable get() = YamalTheme.colors.textSecondary

    /** Cursor color */
    val cursorColor: Color
        @Composable get() = YamalTheme.colors.primary
}

/**
 * Configuration for auto-sizing behavior.
 *
 * @property minRows Minimum number of rows to display
 * @property maxRows Maximum number of rows to display (null = unlimited)
 */
data class AutoSizeConfig(
    val minRows: Int? = null,
    val maxRows: Int? = null,
)

/**
 * TextArea component following Ant Design Mobile specifications.
 *
 * A multi-line text input for long text that requires wrapping.
 * Designed for keyboard-based content entry.
 *
 * @param value Current input value
 * @param onValueChange Callback when value changes
 * @param modifier Modifier for the component
 * @param placeholder Placeholder text shown when input is empty
 * @param rows Number of visible text lines (default 2)
 * @param maxLength Maximum character count (null = unlimited)
 * @param showCount Whether to display character count. When maxLength is set, shows "current/max"
 * @param showCountContent Custom count display composable. Receives (currentLength, maxLength?) as parameters
 * @param autoSize Whether to auto-size the height based on content. Can be boolean or AutoSizeConfig
 * @param enabled Whether the textarea is enabled (default: true). When false, the textarea is disabled.
 * @param readOnly Whether the textarea is read-only
 * @param onEnterPress Callback when Enter key is pressed (not typically used for multiline)
 * @param keyboardActions Keyboard actions for handling IME events
 *
 * Example usage:
 * ```
 * // Basic textarea
 * var text by remember { mutableStateOf("") }
 * TextArea(
 *     value = text,
 *     onValueChange = { text = it },
 *     placeholder = "Enter description"
 * )
 *
 * // With character count
 * TextArea(
 *     value = text,
 *     onValueChange = { text = it },
 *     maxLength = 200,
 *     showCount = true
 * )
 *
 * // Auto-sizing with row limits
 * TextArea(
 *     value = text,
 *     onValueChange = { text = it },
 *     autoSize = AutoSizeConfig(minRows = 2, maxRows = 5)
 * )
 *
 * // Disabled textarea
 * TextArea(
 *     value = "Disabled",
 *     onValueChange = {},
 *     enabled = false
 * )
 * ```
 */
@Composable
fun TextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    rows: Int = TextAreaDefaults.DEFAULT_ROWS,
    maxLength: Int? = null,
    showCount: Boolean = false,
    showCountContent: @Composable ((currentLength: Int, maxLength: Int?) -> Unit)? = null,
    autoSize: AutoSizeConfig? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onEnterPress: (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val textStyle =
        TextStyle(
            fontSize = TextAreaDefaults.FontSize,
            color = if (enabled) TextAreaDefaults.textColor else TextAreaDefaults.disabledColor,
            lineHeight = TextAreaDefaults.FontSize * TextAreaDefaults.LINE_HEIGHT_MULTIPLIER,
        )

    // Calculate min/max height based on rows and autoSize config
    val lineHeightDp = (TextAreaDefaults.FontSize.value * TextAreaDefaults.LINE_HEIGHT_MULTIPLIER).dp
    val effectiveMinRows = autoSize?.minRows ?: rows
    val effectiveMaxRows = autoSize?.maxRows

    val minHeight = lineHeightDp * effectiveMinRows
    val maxHeight = effectiveMaxRows?.let { lineHeightDp * it }

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

    Column(
        modifier = modifier.alpha(if (enabled) 1f else TextAreaDefaults.DISABLED_ALPHA),
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
                    .fillMaxWidth()
                    .heightIn(min = minHeight, max = maxHeight ?: Dp.Unspecified),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Default, // Multi-line typically uses default (newline)
                ),
            keyboardActions = effectiveKeyboardActions,
            singleLine = false,
            cursorBrush = SolidColor(TextAreaDefaults.cursorColor),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = TextAreaDefaults.placeholderColor),
                        )
                    }
                    innerTextField()
                }
            },
        )

        // Character count
        if (showCount || showCountContent != null) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = TextAreaDefaults.CountPaddingTop),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (showCountContent != null) {
                    showCountContent(value.length, maxLength)
                } else {
                    val countText =
                        if (maxLength != null) {
                            "${value.length}/$maxLength"
                        } else {
                            "${value.length}"
                        }
                    Text(
                        text = countText,
                        style =
                            TextStyle(
                                fontSize = 14.sp,
                                color = TextAreaDefaults.countColor,
                                textAlign = TextAlign.End,
                            ),
                    )
                }
            }
        }
    }
}

/**
 * Overload for simple autoSize boolean.
 * When true, enables auto-sizing without row constraints.
 */
@Composable
fun TextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    rows: Int = TextAreaDefaults.DEFAULT_ROWS,
    maxLength: Int? = null,
    showCount: Boolean = false,
    showCountContent: @Composable ((currentLength: Int, maxLength: Int?) -> Unit)? = null,
    autoSize: Boolean,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onEnterPress: (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    TextArea(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        rows = rows,
        maxLength = maxLength,
        showCount = showCount,
        showCountContent = showCountContent,
        autoSize = if (autoSize) AutoSizeConfig() else null,
        enabled = enabled,
        readOnly = readOnly,
        onEnterPress = onEnterPress,
        keyboardActions = keyboardActions,
    )
}

// Previews

@Preview
@Composable
private fun TextAreaBasicPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text by remember { mutableStateOf("") }

                Text(
                    "Basic TextArea",
                    style = YamalTheme.typography.bodyMedium,
                )
                TextArea(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = "Enter description here...",
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
private fun TextAreaWithCountPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text by remember { mutableStateOf("Hello, this is a sample text.") }

                Text(
                    "With Character Count",
                    style = YamalTheme.typography.bodyMedium,
                )
                TextArea(
                    value = text,
                    onValueChange = { text = it },
                    maxLength = 200,
                    showCount = true,
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
private fun TextAreaRowsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text by remember { mutableStateOf("") }

                Text(
                    "4 Rows TextArea",
                    style = YamalTheme.typography.bodyMedium,
                )
                TextArea(
                    value = text,
                    onValueChange = { text = it },
                    rows = 4,
                    placeholder = "This textarea has 4 visible rows",
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
private fun TextAreaAutoSizePreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text by remember {
                    mutableStateOf(
                        "This textarea auto-sizes.\n" +
                            "Try adding more lines.\n" +
                            "It will grow with content.",
                    )
                }

                Text(
                    "Auto-size (2-5 rows)",
                    style = YamalTheme.typography.bodyMedium,
                )
                TextArea(
                    value = text,
                    onValueChange = { text = it },
                    autoSize = AutoSizeConfig(minRows = 2, maxRows = 5),
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
private fun TextAreaDisabledPreview() {
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
                TextArea(
                    value = "This textarea is disabled",
                    onValueChange = {},
                    enabled = false,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(YamalTheme.colors.box)
                            .padding(12.dp),
                )
                TextArea(
                    value = "This textarea is read-only",
                    onValueChange = {},
                    readOnly = true,
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
private fun TextAreaCustomCountPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                var text by remember { mutableStateOf("Custom count display") }

                Text(
                    "Custom Count Display",
                    style = YamalTheme.typography.bodyMedium,
                )
                TextArea(
                    value = text,
                    onValueChange = { text = it },
                    maxLength = 100,
                    showCountContent = { current, max ->
                        val remaining = (max ?: 0) - current
                        val color =
                            if (remaining < 10) {
                                YamalTheme.colors.danger
                            } else {
                                TextAreaDefaults.countColor
                            }
                        Text(
                            text = "$remaining characters remaining",
                            style =
                                TextStyle(
                                    fontSize = 12.sp,
                                    color = color,
                                ),
                        )
                    },
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
