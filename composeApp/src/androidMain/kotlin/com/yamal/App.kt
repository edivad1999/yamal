package com.yamal

import android.app.Application
import di.koinInitializer
import org.koin.android.ext.koin.androidContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        koinInitializer {
            androidContext(this@App)
        }
    }
}
