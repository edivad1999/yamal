package screen.userAnimeList

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yamal.presentation.userAnimeList.presenter.UserAnimeListPresenter

object UserAnimeList : Screen {
    @Composable
    override fun Content() {
        val presenter = getScreenModel<UserAnimeListPresenter>()
        val state by presenter.state.collectAsState()
        Text("WIP")
    }
}
