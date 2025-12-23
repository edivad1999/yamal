package core

import org.koin.core.component.KoinComponent
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual object LoginUtilities : KoinComponent, AuthCodeHandler() {
    actual fun launchBrowser(url: String) {
        NSURL.URLWithString(url)?.let { nsUrl ->
            UIApplication.sharedApplication.openURL(nsUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
        }
    }
}
