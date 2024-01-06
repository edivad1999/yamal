package screen.userAnimeList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yamal.feature.anime.api.model.UserListStatus
import com.yamal.presentation.userAnimeList.presenter.UserAnimeListIntent
import com.yamal.presentation.userAnimeList.presenter.UserAnimeListPresenter
import com.yamal.presentation.userAnimeList.presenter.UserAnimeListUi
import core.GenericAnimeCard
import core.isAnyLoading

object UserAnimeList : Screen {
    @Composable
    override fun Content() {
        val presenter = getScreenModel<UserAnimeListPresenter>()
        val state by presenter.state.collectAsState()
        UserAnimeListScreen(state, onTabClicked = {
            presenter.processIntent(UserAnimeListIntent.OnSelectStatus(it))
        })
    }
}

@Composable fun UserAnimeListScreen(
    state: UserAnimeListUi,
    onTabClicked: (UserListStatus) -> Unit,
) = Column {
    if (state.isLoggedIn) {
        val selectedIndex =
            remember(state.userAnimeStatus, state.userAnimeList) {
                state.userAnimeList.map {
                    it.status
                }.indexOf(state.userAnimeStatus)
            }
        ScrollableTabRow(selectedTabIndex = selectedIndex) {
            state.userAnimeList.map { it.status }.forEach {
                Tab(state.userAnimeStatus == it, onClick = {
                    onTabClicked(it)
                }) {
                    Text(it.name)
                }
            }
        }

        state.userAnimeList.forEach {
            val current = it.list.collectAsLazyPagingItems()
            if (it.status == state.userAnimeStatus) {
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
    } else {
        Text("Not logged in")
    }
}
