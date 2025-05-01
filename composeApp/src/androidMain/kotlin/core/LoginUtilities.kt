package core

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual object LoginUtilities : KoinComponent, AuthCodeHandler() {

    actual fun launchBrowser(url: String) {
        val context: Context = get()
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            runCatching {
                context.startActivity(this)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}
