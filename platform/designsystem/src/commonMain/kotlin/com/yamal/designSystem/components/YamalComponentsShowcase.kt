package com.yamal.designSystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Comprehensive showcase of all Yamal Design System components.
 */
@Preview
@Composable
private fun YamalComponentsShowcasePreview() {
    YamalTheme(isDark = true) {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(Dimension.Spacing.md)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            ) {
                Text(
                    text = "Yamal Design System",
                    style = YamalTheme.typography.h3,
                    color = YamalTheme.colors.neutralColors.title,
                )

                ButtonsSection()
                YamalDivider()
                InputSection()
                YamalDivider()
                CheckboxSection()
                YamalDivider()
                SwitchSection()
                YamalDivider()
                RadioSection()
                YamalDivider()
                TagsSection()
                YamalDivider()
                BadgesSection()
                YamalDivider()
                AvatarsSection()
                YamalDivider()
                CardsSection()
                YamalDivider()
                ProgressSection()
                YamalDivider()
                SpinSection()
                YamalDivider()
                AlertSection()
                YamalDivider()
                EmptySection()
                YamalDivider()
                SkeletonSection()
                YamalDivider()
                SpaceSection()
                YamalDivider()
                DividerSection()
            }
        }
    }
}

@Preview
@Composable
private fun YamalComponentsShowcaseLightPreview() {
    YamalTheme(isDark = false) {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(Dimension.Spacing.md)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            ) {
                Text(
                    text = "Yamal Design System - Light",
                    style = YamalTheme.typography.h3,
                    color = YamalTheme.colors.neutralColors.title,
                )

                ButtonsSection()
                TagsSection()
                BadgesSection()
                AvatarsSection()
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = YamalTheme.typography.h5,
        color = YamalTheme.colors.neutralColors.title,
    )
}

@Composable
private fun ButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Buttons")

        // Button Types
        Text("Types", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
            modifier = Modifier.fillMaxWidth(),
        ) {
            YamalButton(text = "Primary", type = YamalButtonType.Primary, onClick = {})
            YamalButton(text = "Default", type = YamalButtonType.Default, onClick = {})
            YamalButton(text = "Dashed", type = YamalButtonType.Dashed, onClick = {})
            YamalButton(text = "Text", type = YamalButtonType.Text, onClick = {})
            YamalButton(text = "Link", type = YamalButtonType.Link, onClick = {})
        }

        // Button Sizes
        Text("Sizes", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalButton(text = "Large", size = YamalButtonSize.Large, onClick = {})
            YamalButton(text = "Middle", size = YamalButtonSize.Middle, onClick = {})
            YamalButton(text = "Small", size = YamalButtonSize.Small, onClick = {})
        }

        // Danger & Ghost
        Text("Danger & Ghost", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
            YamalButton(text = "Danger", danger = true, onClick = {})
            YamalButton(text = "Ghost", ghost = true, onClick = {})
            YamalButton(text = "Loading", loading = true, onClick = {})
        }

        // Shapes
        Text("Shapes", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
            YamalButton(text = "Default", shape = YamalButtonShape.Default, onClick = {})
            YamalButton(text = "Round", shape = YamalButtonShape.Round, onClick = {})
            YamalButton(shape = YamalButtonShape.Circle, onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun InputSection() {
    var text by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Input")

        // Variants
        Text("Variants", style = YamalTheme.typography.bodyMedium)
        YamalInput(
            value = text,
            onValueChange = { text = it },
            label = "Outlined",
            placeholder = "Enter text...",
            variant = YamalInputVariant.Outlined,
        )
        YamalInput(
            value = text,
            onValueChange = { text = it },
            label = "Filled",
            variant = YamalInputVariant.Filled,
        )
        YamalInput(
            value = text,
            onValueChange = { text = it },
            placeholder = "Borderless",
            variant = YamalInputVariant.Borderless,
        )

        // Status
        Text("Status", style = YamalTheme.typography.bodyMedium)
        YamalInput(
            value = "Error state",
            onValueChange = {},
            status = YamalInputStatus.Error,
            helperText = "This field has an error",
        )
        YamalInput(
            value = "Warning state",
            onValueChange = {},
            status = YamalInputStatus.Warning,
            helperText = "This is a warning",
        )

        // With clear button
        YamalInput(
            value = "Clear me",
            onValueChange = {},
            allowClear = true,
            placeholder = "With clear button",
        )
    }
}

@Composable
private fun CheckboxSection() {
    var checked1 by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(true) }
    var selectedItems by remember { mutableStateOf(setOf("apple")) }

    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Checkbox")

        // Basic
        Text("Basic", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
            YamalCheckbox(
                checked = checked1,
                onCheckedChange = { checked1 = it },
                label = "Unchecked",
            )
            YamalCheckbox(
                checked = checked2,
                onCheckedChange = { checked2 = it },
                label = "Checked",
            )
            YamalCheckbox(
                checked = false,
                onCheckedChange = {},
                indeterminate = true,
                label = "Indeterminate",
            )
        }

        // Group
        Text("Checkbox Group", style = YamalTheme.typography.bodyMedium)
        YamalCheckboxGroup(
            options =
                listOf(
                    YamalCheckboxOption("apple", "Apple"),
                    YamalCheckboxOption("pear", "Pear"),
                    YamalCheckboxOption("orange", "Orange"),
                ),
            selectedValues = selectedItems,
            onSelectionChange = { selectedItems = it },
        )
    }
}

@Composable
private fun SwitchSection() {
    var switch1 by remember { mutableStateOf(false) }
    var switch2 by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Switch")

        // Basic
        Text("Basic", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalSwitch(checked = switch1, onCheckedChange = { switch1 = it })
            YamalSwitch(checked = switch2, onCheckedChange = { switch2 = it })
        }

        // Sizes
        Text("Sizes", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalSwitch(checked = true, onCheckedChange = {}, size = YamalSwitchSize.Default)
            YamalSwitch(checked = true, onCheckedChange = {}, size = YamalSwitchSize.Small)
        }

        // States
        Text("Disabled & Loading", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalSwitch(checked = false, onCheckedChange = {}, enabled = false)
            YamalSwitch(checked = true, onCheckedChange = {}, enabled = false)
            YamalSwitch(checked = false, onCheckedChange = {}, loading = true)
        }
    }
}

@Composable
private fun RadioSection() {
    var selected by remember { mutableStateOf("a") }
    var buttonSelected by remember { mutableStateOf("hz") }

    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Radio")

        // Basic
        Text("Basic", style = YamalTheme.typography.bodyMedium)
        YamalRadioGroup(
            options =
                listOf(
                    YamalRadioOption("a", "Option A"),
                    YamalRadioOption("b", "Option B"),
                    YamalRadioOption("c", "Option C"),
                ),
            selectedValue = selected,
            onSelectionChange = { selected = it },
        )

        // Button Style
        Text("Button Style", style = YamalTheme.typography.bodyMedium)
        YamalRadioGroup(
            options =
                listOf(
                    YamalRadioOption("hz", "Hangzhou"),
                    YamalRadioOption("sh", "Shanghai"),
                    YamalRadioOption("bj", "Beijing"),
                ),
            selectedValue = buttonSelected,
            onSelectionChange = { buttonSelected = it },
            optionType = YamalRadioOptionType.Button,
            buttonStyle = YamalRadioButtonStyle.Solid,
        )
    }
}

@Composable
private fun TagsSection() {
    var checked by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Tags")

        // Preset colors
        Text("Preset Colors", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
            YamalTag(text = "Default")
            YamalTag(text = "Success", preset = YamalTagPreset.Success)
            YamalTag(text = "Processing", preset = YamalTagPreset.Processing)
            YamalTag(text = "Error", preset = YamalTagPreset.Error)
            YamalTag(text = "Warning", preset = YamalTagPreset.Warning)
        }

        // Bordered & Closable
        Text("Bordered & Closable", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
            YamalTag(text = "Bordered", bordered = true)
            YamalTag(text = "Borderless", bordered = false)
            YamalTag(text = "Closable", closable = true, onClose = {})
        }

        // Checkable
        Text("Checkable", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)) {
            YamalCheckableTag(text = "Unchecked", checked = false, onCheckedChange = {})
            YamalCheckableTag(text = "Checked", checked = true, onCheckedChange = {})
            YamalCheckableTag(text = "Toggle", checked = checked, onCheckedChange = { checked = it })
        }
    }
}

@Composable
private fun BadgesSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Badges")

        // Count badges
        Text("Count Badges", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalBadge(count = 5) {
                YamalAvatar(text = "U")
            }
            YamalBadge(count = 25) {
                YamalAvatar(text = "U")
            }
            YamalBadge(count = 100, overflowCount = 99) {
                YamalAvatar(text = "U")
            }
            YamalBadge(dot = true) {
                Icon(Icons.Default.Favorite, contentDescription = null)
            }
        }

        // Status badges
        Text("Status Badges", style = YamalTheme.typography.bodyMedium)
        Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.xxs)) {
            YamalStatusBadge(status = YamalBadgeStatus.Success, text = "Success")
            YamalStatusBadge(status = YamalBadgeStatus.Processing, text = "Processing")
            YamalStatusBadge(status = YamalBadgeStatus.Error, text = "Error")
            YamalStatusBadge(status = YamalBadgeStatus.Warning, text = "Warning")
            YamalStatusBadge(status = YamalBadgeStatus.Default, text = "Default")
        }
    }
}

@Composable
private fun AvatarsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Avatars")

        // Sizes
        Text("Sizes", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalAvatar(size = YamalAvatarSize.Small, text = "S")
            YamalAvatar(size = YamalAvatarSize.Default, text = "M")
            YamalAvatar(size = YamalAvatarSize.Large, text = "L")
        }

        // Shapes
        Text("Shapes", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
            YamalAvatar(shape = YamalAvatarShape.Circle, text = "C")
            YamalAvatar(shape = YamalAvatarShape.Square, text = "S")
        }

        // With icon
        Text("With Icon", style = YamalTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
            YamalAvatar(
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
            )
            YamalAvatar(
                icon = { Icon(Icons.Default.Star, contentDescription = null) },
                shape = YamalAvatarShape.Square,
            )
        }

        // Avatar Group
        Text("Avatar Group", style = YamalTheme.typography.bodyMedium)
        YamalAvatarGroup(
            avatars =
                listOf(
                    { YamalAvatar(text = "A") },
                    { YamalAvatar(text = "B") },
                    { YamalAvatar(text = "C") },
                    { YamalAvatar(text = "D") },
                    { YamalAvatar(text = "E") },
                ),
            maxCount = 3,
        )
    }
}

@Composable
private fun CardsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Cards")

        // Basic card
        YamalCard(
            title = "Card Title",
            extra = { Text("More", color = YamalTheme.colors.functionalColors.link) },
        ) {
            Text("Card content goes here")
        }

        // Small card
        YamalCard(
            size = YamalCardSize.Small,
            title = "Small Card",
        ) {
            Text("Small card content")
        }

        // Card with meta
        YamalCard(
            title = "Card with Meta",
            actions =
                listOf(
                    { Text("Edit") },
                    { Text("Delete") },
                ),
        ) {
            YamalCardMeta(
                avatar = { YamalAvatar(text = "U") },
                title = "User Name",
                description = "This is the description",
            )
        }

        // Loading card
        YamalCard(
            loading = true,
            title = "Loading Card",
        ) {
            Text("This content won't show")
        }
    }
}

@Composable
private fun ProgressSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Progress")

        // Line progress
        Text("Line", style = YamalTheme.typography.bodyMedium)
        YamalProgress(percent = 30f, modifier = Modifier.fillMaxWidth())
        YamalProgress(percent = 70f, status = YamalProgressStatus.Active, modifier = Modifier.fillMaxWidth())
        YamalProgress(percent = 100f, modifier = Modifier.fillMaxWidth())
        YamalProgress(percent = 50f, status = YamalProgressStatus.Exception, modifier = Modifier.fillMaxWidth())

        // Circle progress
        Text("Circle & Dashboard", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalProgress(percent = 75f, type = YamalProgressType.Circle, size = YamalProgressSize.Small)
            YamalProgress(percent = 75f, type = YamalProgressType.Dashboard, size = YamalProgressSize.Small)
        }
    }
}

@Composable
private fun SpinSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Spin")

        // Sizes
        Text("Sizes", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YamalSpin(size = YamalSpinSize.Small)
            YamalSpin(size = YamalSpinSize.Default)
            YamalSpin(size = YamalSpinSize.Large)
        }

        // With tip
        Text("With Tip", style = YamalTheme.typography.bodyMedium)
        YamalSpin(tip = "Loading...")

        // Container
        Text("Container", style = YamalTheme.typography.bodyMedium)
        YamalSpinContainer(spinning = true) {
            YamalCard {
                Text("Content behind spinner")
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun AlertSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Alert")

        YamalAlert(message = "Success Tips", type = YamalAlertType.Success, showIcon = true)
        YamalAlert(message = "Informational Notes", type = YamalAlertType.Info, showIcon = true)
        YamalAlert(message = "Warning", type = YamalAlertType.Warning, showIcon = true)
        YamalAlert(
            message = "Error with description",
            description = "This is a detailed error description.",
            type = YamalAlertType.Error,
            showIcon = true,
            closable = true,
        )
    }
}

@Composable
private fun EmptySection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Empty")

        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md)) {
            YamalEmpty(description = "No Data")
            YamalEmpty(image = YamalEmptyImage.Simple, description = "Empty")
        }
    }
}

@Composable
private fun SkeletonSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Skeleton")

        YamalSkeleton(
            avatar = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
            YamalSkeletonAvatar()
            YamalSkeletonButton()
        }
    }
}

@Composable
private fun SpaceSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Space")

        // Horizontal
        Text("Horizontal", style = YamalTheme.typography.bodyMedium)
        YamalSpace {
            YamalButton(text = "Button 1", onClick = {})
            YamalButton(text = "Button 2", onClick = {})
            YamalButton(text = "Button 3", onClick = {})
        }

        // Vertical
        Text("Vertical", style = YamalTheme.typography.bodyMedium)
        YamalSpace(direction = YamalSpaceDirection.Vertical) {
            YamalTag(text = "Tag 1")
            YamalTag(text = "Tag 2")
            YamalTag(text = "Tag 3")
        }

        // Compact
        Text("Compact", style = YamalTheme.typography.bodyMedium)
        YamalSpaceCompact {
            YamalButton(text = "Left", onClick = {})
            YamalButton(text = "Middle", onClick = {})
            YamalButton(text = "Right", onClick = {})
        }
    }
}

@Composable
private fun DividerSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm)) {
        SectionTitle("Divider")

        // Horizontal
        Text("Horizontal", style = YamalTheme.typography.bodyMedium)
        YamalDivider()
        YamalDivider(dashed = true)

        // With text
        Text("With Text", style = YamalTheme.typography.bodyMedium)
        YamalDivider(text = "Center")
        YamalDivider(text = "Left", orientation = YamalDividerOrientation.Start)
        YamalDivider(text = "Right", orientation = YamalDividerOrientation.End)

        // Vertical
        Text("Vertical", style = YamalTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(24.dp),
        ) {
            Text("Item 1")
            YamalDivider(type = YamalDividerType.Vertical)
            Text("Item 2")
            YamalDivider(type = YamalDividerType.Vertical)
            Text("Item 3")
        }
    }
}
