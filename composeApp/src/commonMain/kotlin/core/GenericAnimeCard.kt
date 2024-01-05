package core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import com.yamal.feature.anime.api.model.GenericAnime

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GenericAnimeCard(genericAnime: GenericAnime) {
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
        Divider()
    }
}
