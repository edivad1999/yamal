package screen.login

import org.koin.core.component.KoinComponent

actual object LoginUtilities : KoinComponent {

    actual suspend fun launchBrowser(url: String) {
    }

    actual fun parseUrlResult(urlResult: String) {
    }
}
