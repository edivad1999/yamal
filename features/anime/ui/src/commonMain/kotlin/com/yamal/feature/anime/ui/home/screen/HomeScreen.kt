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
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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

@Composable
fun HomeScreen(
    onNavigateToSeasonal: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToUserList: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("YAMAL") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Discover Anime",
                style = MaterialTheme.typography.h5
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HomeCard(
                    modifier = Modifier.weight(1f),
                    title = "Seasonal",
                    subtitle = "Current season anime",
                    icon = Icons.Default.DateRange,
                    onClick = onNavigateToSeasonal
                )

                HomeCard(
                    modifier = Modifier.weight(1f),
                    title = "Ranking",
                    subtitle = "Top rated anime",
                    icon = Icons.Default.Star,
                    onClick = onNavigateToRanking
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Library",
                style = MaterialTheme.typography.h5
            )

            HomeCard(
                modifier = Modifier.fillMaxWidth(),
                title = "My Anime List",
                subtitle = "Your watched and planned anime",
                icon = Icons.Default.List,
                onClick = onNavigateToUserList
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = 4.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.h6
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
