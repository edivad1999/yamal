package screen.animeRanking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yamal.presentation.animeRanking.presenter.AnimeRankingPresenter
import com.yamal.presentation.animeRanking.presenter.AnimeRankingUi
import core.GenericAnimeCard
import core.isAnyLoading

object AnimeRankingScreen : Screen {
    @Composable
    override fun Content() {
        val presenter = getScreenModel<AnimeRankingPresenter>()
        val state by presenter.state.collectAsState()

        AnimeRankingScreen(state)
    }
}

@Composable
fun AnimeRankingScreen(state: AnimeRankingUi) =
    Column {
        val items = state.ranking.collectAsLazyPagingItems()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items.itemCount) {
                items[it]?.let {
                    GenericAnimeCard(it)
                }
            }
            if (items.loadState.isAnyLoading) {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
