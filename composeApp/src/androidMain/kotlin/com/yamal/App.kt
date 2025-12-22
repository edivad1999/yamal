package com.yamal

import android.app.Application
import di.koinInitializer
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())

        koinInitializer {
            androidContext(this@App)
        }
    }
}
