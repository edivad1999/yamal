package com.yamal.feature.anime.ui.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.card.YamalCard
import com.yamal.designSystem.components.loadingIndicator.DotLoadingIndicator
import com.yamal.designSystem.components.loadingIndicator.SpinLoadingIndicator
import com.yamal.designSystem.components.navBar.YamalNavBar
import com.yamal.designSystem.components.scaffold.YamalScaffold
import com.yamal.designSystem.components.skeleton.SkeletonTitle
import com.yamal.designSystem.components.tabBar.YamalTabBar
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.IconPainter
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme

@Composable
fun HomeScreen(
    onNavigateToSeasonal: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToUserList: () -> Unit,
) = Column {
    YamalScaffold(
        bottomBar = {
            YamalTabBar(
                items = {
                    item(
                        icon = { Icon(Icons.Outlined.Home, null) },
                        label = { Text("Home") },
                        selected = true,
                        onClick = {},
                    )
                    item(
                        icon = { Icon(Icons.Outlined.Star, null) },
                        label = { Text("stars") },
                        selected = true,
                        onClick = {},
                    )
                    item(
                        icon = { Icon(Icons.Outlined.Setting, null) },
                        label = { Text("Settings") },
                        selected = true,
                        onClick = {},
                    )
                },
            )
        },
        topBar = {
            YamalNavBar(
                title = { Text("YAMAL") },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SpinLoadingIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            DotLoadingIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            SkeletonTitle()

            Text(
                text = "Discover Anime",
                style = YamalTheme.typography.title,
                color = YamalTheme.colors.text,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                HomeCard(
                    modifier = Modifier.weight(1f),
                    title = "Seasonal",
                    subtitle = "Current season anime",
                    icon = Icons.Outlined.Calendar,
                    onClick = onNavigateToSeasonal,
                )

                HomeCard(
                    modifier = Modifier.weight(1f),
                    title = "Ranking",
                    subtitle = "Top rated anime",
                    icon = Icons.Outlined.Star,
                    onClick = onNavigateToRanking,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Library",
                style = YamalTheme.typography.title,
                color = YamalTheme.colors.text,
            )

            HomeCard(
                modifier = Modifier.fillMaxWidth(),
                title = "My Anime List",
                subtitle = "Your watched and planned anime",
                icon = Icons.Outlined.UnorderedList,
                onClick = onNavigateToUserList,
            )
        }
    }
}

@Composable private fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: IconPainter,
    onClick: () -> Unit,
) {
    YamalCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Icon(
                icon = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = YamalTheme.colors.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = YamalTheme.typography.titleSmall,
                color = YamalTheme.colors.text,
            )

            Text(
                text = subtitle,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.textSecondary,
            )
        }
    }
}
