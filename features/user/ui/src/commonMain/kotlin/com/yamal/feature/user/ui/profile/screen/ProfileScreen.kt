package com.yamal.feature.user.ui.profile.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.login.api.LoginRepository
import com.yamal.feature.user.api.model.UserAnimeStatisticsYamal
import com.yamal.feature.user.api.model.UserProfileYamal
import com.yamal.feature.user.ui.profile.presenter.ProfileIntent
import com.yamal.feature.user.ui.profile.presenter.ProfilePresenter
import com.yamal.platform.utils.formatOneDecimal
import com.yamal.platform.utils.formatTwoDecimals
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    presenter: ProfilePresenter = koinInject(),
    loginRepository: LoginRepository = koinInject(),
) {
    val state by presenter.state.collectAsState()
    val isLoggedIn by loginRepository.isUserAuthenticated().collectAsState(initial = false)

    YamalScaffold(
        topBar = {
            YamalNavBar(
                title = { Text("Profile") },
                right = {
                    Icon(
                        icon = Icons.Outlined.Setting,
                        contentDescription = "Settings",
                    )
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when {
                !isLoggedIn -> {
                    NotLoggedInContent()
                }

                state.isLoading && state.userProfile == null -> {
                    SpinLoadingIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                state.error != null && state.userProfile == null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = state.error ?: "An error occurred",
                            color = YamalTheme.colors.danger,
                        )
                        YamalButton(
                            text = "Retry",
                            onClick = { presenter.processIntent(ProfileIntent.Refresh) },
                            color = ButtonColor.Primary,
                        )
                    }
                }

                state.userProfile != null -> {
                    ProfileContent(
                        userProfile = state.userProfile!!,
                        onNavigateToSettings = onNavigateToSettings,
                    )
                }
            }
        }
    }
}

@Composable
private fun NotLoggedInContent() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(Dimension.Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            icon = Icons.Outlined.User,
            contentDescription = null,
            tint = YamalTheme.colors.weak,
            modifier = Modifier.size(64.dp),
        )

        Spacer(modifier = Modifier.height(Dimension.Spacing.md))

        Text(
            text = "Not Signed In",
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.text,
        )

        Spacer(modifier = Modifier.height(Dimension.Spacing.sm))

        Text(
            text = "Sign in to view your profile and statistics",
            style = YamalTheme.typography.body,
            color = YamalTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun ProfileContent(
    userProfile: UserProfileYamal,
    onNavigateToSettings: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState())
                .padding(Dimension.Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.lg),
    ) {
        // User info header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
        ) {
            // Avatar
            if (userProfile.picture != null) {
                AsyncImage(
                    model = userProfile.picture,
                    contentDescription = "Profile picture",
                    modifier =
                        Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = YamalTheme.colors.primary,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = userProfile.name.firstOrNull()?.uppercase() ?: "?",
                            style = YamalTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = YamalTheme.colors.textLightSolid,
                        )
                    }
                }
            }

            Text(
                text = userProfile.name,
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )

            userProfile.joinedAt?.let { joinedAt ->
                Text(
                    text = "Member since ${joinedAt.take(10)}",
                    style = YamalTheme.typography.small,
                    color = YamalTheme.colors.textSecondary,
                )
            }
        }

        // Anime statistics
        userProfile.animeStatistics?.let { stats ->
            AnimeStatisticsSection(stats = stats)
        }

        Spacer(modifier = Modifier.height(Dimension.Spacing.md))
    }
}

@Composable
private fun AnimeStatisticsSection(stats: UserAnimeStatisticsYamal) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = YamalTheme.colors.fillContent,
    ) {
        Column(
            modifier = Modifier.padding(Dimension.Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.md),
        ) {
            Text(
                text = "Anime Statistics",
                style = YamalTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = YamalTheme.colors.text,
            )

            // List status counts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatItem(label = "Watching", value = stats.numItemsWatching.toString())
                StatItem(label = "Completed", value = stats.numItemsCompleted.toString())
                StatItem(label = "On Hold", value = stats.numItemsOnHold.toString())
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatItem(label = "Dropped", value = stats.numItemsDropped.toString())
                StatItem(label = "Plan to Watch", value = stats.numItemsPlanToWatch.toString())
                StatItem(label = "Total", value = stats.numItems.toString())
            }

            // Additional stats
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.sm),
            ) {
                StatRow(label = "Days Watched", value = stats.numDays.formatOneDecimal())
                StatRow(label = "Episodes Watched", value = stats.numEpisodes.toString())
                StatRow(label = "Mean Score", value = stats.meanScore.formatTwoDecimals())
                StatRow(label = "Times Rewatched", value = stats.numTimesRewatched.toString())
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            style = YamalTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = YamalTheme.colors.primary,
        )
        Text(
            text = label,
            style = YamalTheme.typography.small,
            color = YamalTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = YamalTheme.typography.body,
            color = YamalTheme.colors.textSecondary,
        )
        Text(
            text = value,
            style = YamalTheme.typography.body,
            fontWeight = FontWeight.Medium,
            color = YamalTheme.colors.text,
        )
    }
}
