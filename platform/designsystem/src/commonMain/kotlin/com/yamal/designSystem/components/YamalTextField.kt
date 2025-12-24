package com.yamal.designSystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Input size following Ant Design guidelines.
 */
@Immutable
enum class YamalInputSize {
    Large,
    Middle,
    Small,
}

/**
 * Input variant following Ant Design guidelines (5.13+).
 */
@Immutable
enum class YamalInputVariant {
    Outlined,
    Borderless,
    Filled,
}

/**
 * Input status for validation states following Ant Design guidelines.
 */
@Immutable
enum class YamalInputStatus {
    Default,
    Error,
    Warning,
}

/**
 * A text input component following Ant Design guidelines.
 *
 * @param value Current input value
 * @param onValueChange Callback when value changes
 * @param modifier Modifier for the component
 * @param size Input size (Large, Middle, Small)
 * @param variant Input variant (Outlined, Borderless, Filled)
 * @param status Validation status (Default, Error, Warning)
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param helperText Optional helper text below input
 * @param prefix Optional leading content
 * @param suffix Optional trailing content
 * @param allowClear Whether to show clear button
 * @param disabled Whether the input is disabled
 * @param readOnly Whether the input is read-only
 * @param singleLine Whether to restrict to single line
 * @param maxLines Maximum lines for multiline input
 */
@Composable
fun YamalInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    size: YamalInputSize = YamalInputSize.Middle,
    variant: YamalInputVariant = YamalInputVariant.Outlined,
    status: YamalInputStatus = YamalInputStatus.Default,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    allowClear: Boolean = false,
    disabled: Boolean = false,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val height =
        when (size) {
            YamalInputSize.Small -> Dimension.ComponentSize.small
            YamalInputSize.Middle -> Dimension.ComponentSize.middle
            YamalInputSize.Large -> Dimension.ComponentSize.large
        }

    val shape: Shape = RoundedCornerShape(Dimension.BorderRadius.base)

    val statusColor =
        when (status) {
            YamalInputStatus.Default -> colors.paletteColors.color6
            YamalInputStatus.Error -> colors.functionalColors.error
            YamalInputStatus.Warning -> colors.functionalColors.warning
        }

    val trailingIcon: @Composable (() -> Unit)? =
        when {
            allowClear && value.isNotEmpty() && !disabled && !readOnly -> {
                {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            icon = Icons.Outlined.Close,
                            contentDescription = "Clear",
                            tint = colors.neutralColors.disableText,
                        )
                    }
                }
            }

            suffix != null -> {
                suffix
            }

            else -> {
                null
            }
        }

    Column(modifier = modifier) {
        when (variant) {
            YamalInputVariant.Outlined -> {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth().height(height + 24.dp),
                    enabled = !disabled,
                    readOnly = readOnly,
                    textStyle = typography.body,
                    label = label?.let { { Text(text = it, style = typography.footnote) } },
                    placeholder =
                        placeholder?.let {
                            {
                                Text(
                                    text = it,
                                    style = typography.body,
                                    color = colors.neutralColors.disableText,
                                )
                            }
                        },
                    leadingIcon = prefix,
                    trailingIcon = trailingIcon,
                    isError = status == YamalInputStatus.Error,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    interactionSource = interactionSource,
                    shape = shape,
                    colors =
                        TextFieldDefaults.outlinedTextFieldColors(
                            textColor = colors.neutralColors.primaryText,
                            disabledTextColor = colors.neutralColors.disableText,
                            backgroundColor = Color.Transparent,
                            cursorColor = statusColor,
                            errorCursorColor = colors.functionalColors.error,
                            focusedBorderColor = statusColor,
                            unfocusedBorderColor = colors.neutralColors.border,
                            disabledBorderColor = colors.neutralColors.divider,
                            errorBorderColor = colors.functionalColors.error,
                            focusedLabelColor = statusColor,
                            unfocusedLabelColor = colors.neutralColors.secondaryText,
                            disabledLabelColor = colors.neutralColors.disableText,
                            errorLabelColor = colors.functionalColors.error,
                            placeholderColor = colors.neutralColors.disableText,
                            disabledPlaceholderColor = colors.neutralColors.disableText,
                        ),
                )
            }

            YamalInputVariant.Filled -> {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth().height(height + 24.dp),
                    enabled = !disabled,
                    readOnly = readOnly,
                    textStyle = typography.body,
                    label = label?.let { { Text(text = it, style = typography.footnote) } },
                    placeholder =
                        placeholder?.let {
                            {
                                Text(
                                    text = it,
                                    style = typography.body,
                                    color = colors.neutralColors.disableText,
                                )
                            }
                        },
                    leadingIcon = prefix,
                    trailingIcon = trailingIcon,
                    isError = status == YamalInputStatus.Error,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    interactionSource = interactionSource,
                    shape =
                        RoundedCornerShape(
                            topStart = Dimension.BorderRadius.base,
                            topEnd = Dimension.BorderRadius.base,
                        ),
                    colors =
                        TextFieldDefaults.textFieldColors(
                            textColor = colors.neutralColors.primaryText,
                            disabledTextColor = colors.neutralColors.disableText,
                            backgroundColor = colors.neutralColors.tableHeader,
                            cursorColor = statusColor,
                            errorCursorColor = colors.functionalColors.error,
                            focusedIndicatorColor = statusColor,
                            unfocusedIndicatorColor = colors.neutralColors.border,
                            disabledIndicatorColor = colors.neutralColors.divider,
                            errorIndicatorColor = colors.functionalColors.error,
                            focusedLabelColor = statusColor,
                            unfocusedLabelColor = colors.neutralColors.secondaryText,
                            disabledLabelColor = colors.neutralColors.disableText,
                            errorLabelColor = colors.functionalColors.error,
                            placeholderColor = colors.neutralColors.disableText,
                            disabledPlaceholderColor = colors.neutralColors.disableText,
                        ),
                )
            }

            YamalInputVariant.Borderless -> {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth().height(height + 24.dp),
                    enabled = !disabled,
                    readOnly = readOnly,
                    textStyle = typography.body,
                    label = label?.let { { Text(text = it, style = typography.footnote) } },
                    placeholder =
                        placeholder?.let {
                            {
                                Text(
                                    text = it,
                                    style = typography.body,
                                    color = colors.neutralColors.disableText,
                                )
                            }
                        },
                    leadingIcon = prefix,
                    trailingIcon = trailingIcon,
                    isError = status == YamalInputStatus.Error,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    interactionSource = interactionSource,
                    shape = shape,
                    colors =
                        TextFieldDefaults.textFieldColors(
                            textColor = colors.neutralColors.primaryText,
                            disabledTextColor = colors.neutralColors.disableText,
                            backgroundColor = Color.Transparent,
                            cursorColor = statusColor,
                            errorCursorColor = colors.functionalColors.error,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            focusedLabelColor = statusColor,
                            unfocusedLabelColor = colors.neutralColors.secondaryText,
                            disabledLabelColor = colors.neutralColors.disableText,
                            errorLabelColor = colors.functionalColors.error,
                            placeholderColor = colors.neutralColors.disableText,
                            disabledPlaceholderColor = colors.neutralColors.disableText,
                        ),
                )
            }
        }

        if (helperText != null) {
            Text(
                text = helperText,
                style = typography.footnote,
                color =
                    when (status) {
                        YamalInputStatus.Default -> colors.neutralColors.secondaryText
                        YamalInputStatus.Error -> colors.functionalColors.error
                        YamalInputStatus.Warning -> colors.functionalColors.warning
                    },
                modifier = Modifier.padding(start = Dimension.Spacing.md, top = Dimension.Spacing.xxs),
            )
        }
    }
}

// Backward compatibility alias
@Composable
fun YamalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    YamalInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        variant = YamalInputVariant.Outlined,
        status = if (isError) YamalInputStatus.Error else YamalInputStatus.Default,
        label = label,
        placeholder = placeholder,
        helperText = errorText ?: helperText,
        prefix = leadingIcon,
        suffix = trailingIcon,
        disabled = !enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
    )
}

// Previews

@Preview
@Composable
private fun YamalInputVariantsPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalInput(
                    value = "Outlined Input",
                    onValueChange = {},
                    label = "Outlined",
                    variant = YamalInputVariant.Outlined,
                )
                YamalInput(
                    value = "Filled Input",
                    onValueChange = {},
                    label = "Filled",
                    variant = YamalInputVariant.Filled,
                )
                YamalInput(
                    value = "Borderless Input",
                    onValueChange = {},
                    label = "Borderless",
                    variant = YamalInputVariant.Borderless,
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalInputSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalInput(
                    value = "",
                    onValueChange = {},
                    placeholder = "Large input",
                    size = YamalInputSize.Large,
                )
                YamalInput(
                    value = "",
                    onValueChange = {},
                    placeholder = "Middle input",
                    size = YamalInputSize.Middle,
                )
                YamalInput(
                    value = "",
                    onValueChange = {},
                    placeholder = "Small input",
                    size = YamalInputSize.Small,
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalInputStatusPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalInput(
                    value = "Normal",
                    onValueChange = {},
                    status = YamalInputStatus.Default,
                    helperText = "This is helper text",
                )
                YamalInput(
                    value = "Error",
                    onValueChange = {},
                    status = YamalInputStatus.Error,
                    helperText = "This field has an error",
                )
                YamalInput(
                    value = "Warning",
                    onValueChange = {},
                    status = YamalInputStatus.Warning,
                    helperText = "This is a warning",
                )
            }
        }
    }
}

@Preview
@Composable
private fun YamalInputWithClearPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            YamalInput(
                value = "Clear me",
                onValueChange = {},
                modifier = Modifier.padding(16.dp),
                allowClear = true,
                placeholder = "Type something...",
            )
        }
    }
}
