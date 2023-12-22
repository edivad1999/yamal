package screen.login

import org.koin.core.component.KoinComponent
import platform.Foundation.NSURL.Companion.URLWithString
import platform.UIKit.UIApplication

actual object LoginUtilities : KoinComponent, AuthCodeHandler() {

    actual suspend fun launchBrowser(url: String) {
        UIApplication.sharedApplication.openURL(URLWithString(url)!!)
    }
}
