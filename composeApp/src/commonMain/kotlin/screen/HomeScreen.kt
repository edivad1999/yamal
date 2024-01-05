package screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.fetch.NetworkFetcher
import coil3.request.ImageRequest
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.presentation.home.presenter.HomePresenter
import com.yamal.presentation.home.presenter.HomeState
import kotlinx.coroutines.launch

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val presenter: HomePresenter = getScreenModel()
        val state by presenter.state.collectAsState()
        HomeScreen(state)
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(state: HomeState) =
    Column {
        val items = state.genericAnime.collectAsLazyPagingItems()
        var seasonalSelected: Boolean by remember { mutableStateOf(false) }
        Text(
            "Change",
            modifier =
                Modifier.clickable {
                    seasonalSelected = !seasonalSelected
                },
        )
        if (!seasonalSelected) {
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
        } else {
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
    }

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GenericAnimeCard(genericAnime: GenericAnime) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            AsyncImage(
                model =
                    ImageRequest.Builder(LocalPlatformContext.current)
                        .fetcherFactory(NetworkFetcher.Factory())
                        .data(genericAnime.mainPicture?.large ?: genericAnime.mainPicture?.medium)
                        .build(),
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

val CombinedLoadStates.isAnyLoading: Boolean
    get() = append is LoadState.Loading || refresh is LoadState.Loading || prepend is LoadState.Loading
