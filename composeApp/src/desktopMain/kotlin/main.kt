@file:Suppress("ktlint:standard:filename")

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.koinInitializer

fun main() =
    application {
        koinInitializer { }
        Window(onCloseRequest = ::exitApplication) {
            App()
        }
    }

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}
