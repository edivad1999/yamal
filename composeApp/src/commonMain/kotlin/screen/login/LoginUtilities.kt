package screen.login

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

abstract class AuthCodeHandler {

    val authCode: MutableStateFlow<String?> = MutableStateFlow(null)
    fun parseUrlResult(urlResult: String) {
        authCode.tryEmit(urlResult)
    }
}

expect object LoginUtilities : KoinComponent, AuthCodeHandler {

    fun launchBrowser(url: String)
}
