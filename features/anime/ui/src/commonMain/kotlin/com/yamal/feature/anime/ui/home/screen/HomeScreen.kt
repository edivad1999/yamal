package com.yamal.feature.anime.ui.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.YamalCard
import com.yamal.designSystem.theme.YamalTheme

@Composable fun HomeScreen(
    onNavigateToSeasonal: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToUserList: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = { Text("YAMAL") },
                backgroundColor = YamalTheme.colors.paletteColors.color6,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Discover Anime",
                style = YamalTheme.typography.h4,
                color = YamalTheme.colors.neutralColors.title,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                HomeCard(
                    modifier = Modifier.weight(1f),
                    title = "Seasonal",
                    subtitle = "Current season anime",
                    icon = Icons.Default.DateRange,
                    onClick = onNavigateToSeasonal,
                )

                HomeCard(
                    modifier = Modifier.weight(1f),
                    title = "Ranking",
                    subtitle = "Top rated anime",
                    icon = Icons.Default.Star,
                    onClick = onNavigateToRanking,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Library",
                style = YamalTheme.typography.h4,
                color = YamalTheme.colors.neutralColors.title,
            )

            HomeCard(
                modifier = Modifier.fillMaxWidth(),
                title = "My Anime List",
                subtitle = "Your watched and planned anime",
                icon = Icons.Default.List,
                onClick = onNavigateToUserList,
            )
        }
    }
}

@Composable private fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    YamalCard(
        modifier = modifier,
        onClick = onClick,
        hoverable = true,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = YamalTheme.colors.paletteColors.color6,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = YamalTheme.typography.h5,
                color = YamalTheme.colors.neutralColors.title,
            )

            Text(
                text = subtitle,
                style = YamalTheme.typography.body,
                color = YamalTheme.colors.neutralColors.secondaryText,
            )
        }
    }
}
