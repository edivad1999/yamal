package core

import org.koin.core.component.KoinComponent
import platform.Foundation.NSURLMeta
import platform.UIKit.UIApplication

actual object LoginUtilities : KoinComponent, AuthCodeHandler() {

    actual fun launchBrowser(url: String) {
        UIApplication.Companion.sharedApplication.openURL(NSURLMeta.URLWithString(url)!!)
    }
}
