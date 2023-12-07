import com.yamal.featureManager.AppModules
import org.koin.core.context.startKoin

actual fun initKoin() = startKoin {
    modules(AppModules.exportModules())
}
