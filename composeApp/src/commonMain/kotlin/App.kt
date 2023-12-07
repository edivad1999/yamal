import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.yamal.featureManager.AppModules
import com.yamal.presentation.home.presenter.CounterPresenter
import com.yamal.presentation.home.presenter.CounterScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.KoinApplication

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        KoinApplication(application = {
            modules(AppModules.exportModules())
        }) {
            var greetingText by remember { mutableStateOf("Hello World!") }
            var showImage by remember { mutableStateOf(false) }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = CenterHorizontally) {
                Button(onClick = {
                    greetingText = "Compose: ${Greeting().greet()}"
                    showImage = !showImage
                }) {
                    Text(greetingText)
                }
                AnimatedVisibility(showImage) {
                    Column {
                        Image(
                            painterResource("compose-multiplatform.xml"),
                            null,
                        )
                        Counter()
                    }
                }
            }
        }
    }
}

@Composable
fun Counter(state: CounterScreen.CounterState = koinInject<CounterPresenter>().present()) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier.align(CenterHorizontally),
                text = "Count: ${state.count}",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.align(CenterHorizontally),
                onClick = { state.event(CounterScreen.CounterEvent.Increment) },
            ) { Icon(rememberVectorPainter(Icons.Filled.Add), "Increment") }
            Button(
                modifier = Modifier.align(CenterHorizontally),
                onClick = { state.event(CounterScreen.CounterEvent.Decrement) },
            ) { Icon(rememberVectorPainter(Icons.Filled.Delete), "Decrement") }
        }
    }
}
