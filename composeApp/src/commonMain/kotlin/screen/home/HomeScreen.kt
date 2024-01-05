package screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import coil3.annotation.ExperimentalCoilApi
import com.yamal.presentation.home.presenter.HomePresenter
import com.yamal.presentation.home.presenter.HomeState

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
        Text("WIP")
    }
