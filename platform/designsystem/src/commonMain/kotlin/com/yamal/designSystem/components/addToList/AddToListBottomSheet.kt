package com.yamal.designSystem.components.addToList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.popup.YamalBottomSheet
import com.yamal.designSystem.components.slider.YamalSlider
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.tag.Tag
import com.yamal.designSystem.components.tag.TagColor
import com.yamal.designSystem.components.tag.TagFill
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme

/**
 * Status options for anime list
 */
@Immutable
enum class AnimeListStatus(
    val displayName: String,
    val serialName: String,
) {
    Watching("Watching", "watching"),
    Completed("Completed", "completed"),
    OnHold("On Hold", "on_hold"),
    Dropped("Dropped", "dropped"),
    PlanToWatch("Plan to Watch", "plan_to_watch"),
}

/**
 * State for the AddToListBottomSheet
 */
@Immutable
data class AddToListState(
    val status: AnimeListStatus? = null,
    val score: Int = 0,
    val episodesWatched: Int = 0,
    val totalEpisodes: Int = 0,
    val isInList: Boolean = false,
)

/**
 * A bottom sheet component for adding or updating anime in a user's list.
 *
 * Features:
 * - Status selection chips (Watching, Completed, On Hold, Dropped, Plan to Watch)
 * - Score slider (0-10)
 * - Episode progress stepper
 * - Save and Delete buttons
 * - Login prompt for unauthenticated users
 *
 * This version uses the custom YamalBottomSheet without Material dependency.
 */
@Composable
fun AddToListBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    initialState: AddToListState,
    isLoggedIn: Boolean,
    onSave: (status: AnimeListStatus?, score: Int, episodesWatched: Int) -> Unit,
    onDelete: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YamalBottomSheet(
        visible = visible,
        onDismiss = onDismiss,
        showHandleBar = true,
        modifier = modifier,
    ) {
        if (isLoggedIn) {
            AddToListContent(
                initialState = initialState,
                onSave = { status, score, episodes ->
                    onSave(status, score, episodes)
                    onDismiss()
                },
                onDelete = {
                    onDelete()
                    onDismiss()
                },
            )
        } else {
            LoginPromptContent(
                onLoginClick = {
                    onDismiss()
                    onLoginClick()
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddToListContent(
    initialState: AddToListState,
    onSave: (status: AnimeListStatus?, score: Int, episodesWatched: Int) -> Unit,
    onDelete: () -> Unit,
) {
    var selectedStatus by remember { mutableStateOf(initialState.status) }
    var score by remember { mutableFloatStateOf(initialState.score.toFloat()) }
    var episodesWatched by remember { mutableIntStateOf(initialState.episodesWatched) }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(Dimension.Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
    ) {
        // Title
        Text(
            text = if (initialState.isInList) "Edit List Entry" else "Add to List",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        // Status selection
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
        ) {
            Text(
                text = "Status",
                style = YamalTheme.typography.body,
                fontWeight = FontWeight.Medium,
                color = YamalTheme.colors.text,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs),
            ) {
                AnimeListStatus.entries.forEach { status ->
                    StatusChip(
                        status = status,
                        isSelected = selectedStatus == status,
                        onClick = {
                            selectedStatus = if (selectedStatus == status) null else status
                        },
                    )
                }
            }
        }

        // Score slider
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Score",
                    style = YamalTheme.typography.body,
                    fontWeight = FontWeight.Medium,
                    color = YamalTheme.colors.text,
                )
                Text(
                    text = if (score.toInt() == 0) "-" else score.toInt().toString(),
                    style = YamalTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = YamalTheme.colors.primary,
                )
            }

            YamalSlider(
                value = score,
                onValueChange = { score = it },
                valueRange = 0f..10f,
                steps = 9,
                thumbColor = YamalTheme.colors.primary,
                activeTrackColor = YamalTheme.colors.primary,
                inactiveTrackColor = YamalTheme.colors.border,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "0",
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.weak,
                )
                Text(
                    text = "10",
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.weak,
                )
            }
        }

        // Episodes progress
        if (initialState.totalEpisodes > 0) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                Text(
                    text = "Episodes Watched",
                    style = YamalTheme.typography.body,
                    fontWeight = FontWeight.Medium,
                    color = YamalTheme.colors.text,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Decrement button
                    Surface(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .clickable(
                                    enabled = episodesWatched > 0,
                                    onClick = { episodesWatched-- },
                                ),
                        shape = RoundedCornerShape(8.dp),
                        color =
                            if (episodesWatched > 0) {
                                YamalTheme.colors.fillContent
                            } else {
                                YamalTheme.colors.border
                            },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                icon = Icons.Outlined.Minus,
                                contentDescription = "Decrease",
                                tint =
                                    if (episodesWatched > 0) {
                                        YamalTheme.colors.text
                                    } else {
                                        YamalTheme.colors.weak
                                    },
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }

                    // Episode count
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = episodesWatched.toString(),
                            style = YamalTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = YamalTheme.colors.text,
                        )
                        Text(
                            text = "/ ${initialState.totalEpisodes}",
                            style = YamalTheme.typography.small,
                            color = YamalTheme.colors.weak,
                        )
                    }

                    // Increment button
                    Surface(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .clickable(
                                    enabled = episodesWatched < initialState.totalEpisodes,
                                    onClick = { episodesWatched++ },
                                ),
                        shape = RoundedCornerShape(8.dp),
                        color =
                            if (episodesWatched < initialState.totalEpisodes) {
                                YamalTheme.colors.fillContent
                            } else {
                                YamalTheme.colors.border
                            },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                icon = Icons.Outlined.Plus,
                                contentDescription = "Increase",
                                tint =
                                    if (episodesWatched < initialState.totalEpisodes) {
                                        YamalTheme.colors.text
                                    } else {
                                        YamalTheme.colors.weak
                                    },
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Quick fill button
                    YamalButton(
                        text = "All",
                        onClick = { episodesWatched = initialState.totalEpisodes },
                        color = ButtonColor.Primary,
                        fill = ButtonFill.Outline,
                        size = ButtonSize.Small,
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
        ) {
            if (initialState.isInList) {
                YamalButton(
                    text = "Delete",
                    onClick = onDelete,
                    color = ButtonColor.Danger,
                    fill = ButtonFill.Outline,
                    modifier = Modifier.weight(1f),
                    block = true,
                )
            }

            YamalButton(
                text = "Save",
                onClick = { onSave(selectedStatus, score.toInt(), episodesWatched) },
                color = ButtonColor.Primary,
                fill = ButtonFill.Solid,
                modifier = Modifier.weight(1f),
                block = true,
            )
        }

        Spacer(modifier = Modifier.height(Dimension.Spacing.md))
    }
}

@Composable
private fun StatusChip(
    status: AnimeListStatus,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Tag(
        text = status.displayName,
        color = if (isSelected) TagColor.Primary else TagColor.Default,
        fill = if (isSelected) TagFill.Solid else TagFill.Outline,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun LoginPromptContent(onLoginClick: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(Dimension.Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
    ) {
        Spacer(modifier = Modifier.height(Dimension.Spacing.sm))

        Icon(
            icon = Icons.Outlined.Lock,
            contentDescription = null,
            tint = YamalTheme.colors.weak,
            modifier = Modifier.size(48.dp),
        )

        Text(
            text = "Sign in Required",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        Text(
            text = "You need to sign in to your MyAnimeList account to add anime to your list.",
            style = YamalTheme.typography.body,
            color = YamalTheme.colors.textSecondary,
        )

        Spacer(modifier = Modifier.height(Dimension.Spacing.sm))

        YamalButton(
            text = "Sign In",
            onClick = onLoginClick,
            color = ButtonColor.Primary,
            fill = ButtonFill.Solid,
            block = true,
        )

        Spacer(modifier = Modifier.height(Dimension.Spacing.lg))
    }
}
