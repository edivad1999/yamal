package core

import org.koin.core.component.KoinComponent

actual object LoginUtilities : KoinComponent, AuthCodeHandler() {
    actual fun launchBrowser(url: String) {
    }
}
