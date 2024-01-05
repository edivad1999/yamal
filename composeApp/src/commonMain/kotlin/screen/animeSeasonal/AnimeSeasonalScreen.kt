package screen.animeSeasonal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yamal.presentation.animeSeasonal.presenter.AnimeSeasonalPresenter
import com.yamal.presentation.animeSeasonal.presenter.AnimeSeasonalUi
import core.GenericAnimeCard
import core.isAnyLoading
import kotlinx.coroutines.launch

object AnimeSeasonalScreen : Screen {
    @Composable
    override fun Content() {
        val presenter = getScreenModel<AnimeSeasonalPresenter>()
        val state by presenter.state.collectAsState()

        AnimeSeasonalScreen(state)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeSeasonalScreen(state: AnimeSeasonalUi) {
    val c = rememberCoroutineScope()
    val pager =
        rememberPagerState(state.animeSeason.size - 1) {
            state.animeSeason.size
        }
    LazyRow {
        itemsIndexed(state.animeSeason) { index, item ->
            Column(
                modifier =
                    Modifier.clickable {
                        c.launch {
                            pager.scrollToPage(index)
                        }
                    },
            ) {
                Text(item.first.year)
                Text(item.first.season.toString())
            }
        }
    }

    HorizontalPager(pager, beyondBoundsPageCount = 2) {
        val current = state.animeSeason[it].second.collectAsLazyPagingItems()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(current.itemCount) {
                val element = current[it]
                element?.let {
                    GenericAnimeCard(it)
                }
            }
            if (current.loadState.isAnyLoading) {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
