package screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import com.yamal.presentation.home.presenter.HomePresenter
import com.yamal.presentation.home.presenter.HomeScreen
import org.koin.compose.koinInject

object HomeScreen : Screen {

    @Composable
    override fun Content() = HomeScreen()
}

@Composable
private fun HomeScreen(state: HomeScreen.HomeState = koinInject<HomePresenter>().present()) {
    val items = state.animeRanking.collectAsLazyPagingItems()

    Box(Modifier.fillMaxSize()) {
        LazyColumn() {
            items(items.itemCount) {
                items[it]?.let {
                    Text(
                        modifier = Modifier,
                        text = it.toString(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            if (items.loadState.isAnyLoading) {
                item {
                    CircularProgressIndicator()
                }
            }
        }

    }
}

val CombinedLoadStates.isAnyLoading: Boolean
    get() = append is LoadState.Loading || refresh is LoadState.Loading || prepend is LoadState.Loading
