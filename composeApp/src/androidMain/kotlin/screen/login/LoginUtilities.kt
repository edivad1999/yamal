package screen.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual object LoginUtilities : KoinComponent, AuthCodeHandler() {

    actual suspend fun launchBrowser(url: String) {
        val context: Context = get()
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            runCatching {
                context.startActivity(this)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}
