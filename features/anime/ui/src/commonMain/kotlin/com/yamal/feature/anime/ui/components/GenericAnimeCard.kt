package com.yamal.feature.anime.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamal.designSystem.components.divider.YamalDivider
import com.yamal.designSystem.components.text.Text
import com.yamal.feature.anime.api.model.AnimeForListYamal

@Composable
fun GenericAnimeCard(genericAnime: AnimeForListYamal) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            AsyncImage(
                model = genericAnime.mainPicture?.large ?: genericAnime.mainPicture?.medium,
                contentDescription = "animeImage",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit,
            )
            Text(
                modifier = Modifier,
                text = genericAnime.toString(),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        YamalDivider(modifier = Modifier.fillMaxWidth())
    }
}
