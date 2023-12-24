package screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.yamal.presentation.home.presenter.CounterPresenter
import com.yamal.presentation.home.presenter.CounterScreen
import org.koin.compose.koinInject

object HomeScreen : Screen {

    @Composable
    override fun Content() = HomeScreen()
}

@Composable
private fun HomeScreen(state: CounterScreen.CounterState = koinInject<CounterPresenter>().present()) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Count: ${state.count}",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { state.event(CounterScreen.CounterIntent.Increment) },
            ) { Icon(rememberVectorPainter(Icons.Filled.Add), "Increment") }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { state.event(CounterScreen.CounterIntent.Decrement) },
            ) { Icon(rememberVectorPainter(Icons.Filled.Delete), "Decrement") }
        }
    }
}
