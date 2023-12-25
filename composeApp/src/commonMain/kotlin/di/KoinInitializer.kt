package di

import com.yamal.shared.AppModules
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun koinInitializer(block: KoinApplication.() -> Unit) = startKoin {
    modules(AppModules.exportModules())
    block()
}

fun defaultKoinInitializer() = koinInitializer { }
