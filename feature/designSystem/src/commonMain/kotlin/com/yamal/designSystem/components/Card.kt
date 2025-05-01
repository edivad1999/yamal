package com.yamal.designSystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable fun YamalCard(
    title: String,
    imageUrl: String,
    score: Float? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    ), elevation = CardDefaults.cardElevation(
        defaultElevation = 4.dp
    )
    ) {
        Column {
            Box {
                AsyncImage(
                    model = imageUrl, contentDescription = title, modifier = Modifier.fillMaxWidth().height(200.dp), contentScale = ContentScale.Crop
                )
                score?.let {
                    Box(
                        modifier = Modifier.padding(8.dp).align(Alignment.TopEnd)
                    ) {
                        YamalBadge(
                            text = score.toString().take(3), backgroundColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                content()
            }
        }
    }
}

@Preview
@Composable private fun YamalCardPreview() {
    YamalTheme {
        YamalCard(
            title = "Attack on Titan",
            imageUrl = "https://cdn.myanimelist.net/images/anime/10/47347.jpg",
            score = 9.0f,
            modifier = Modifier.width(200.dp)
        ) {
            Text(
                text = "Action, Drama, Fantasy", style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
