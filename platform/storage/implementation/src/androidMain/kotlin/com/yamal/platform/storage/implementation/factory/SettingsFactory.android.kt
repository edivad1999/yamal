package com.yamal.platform.storage.implementation.factory

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

class SettingsFactoryAndroid(private val context: Context) : SettingsFactory {

    override fun createSettings(): Settings = SharedPreferencesSettings.Factory(context).create("prefs")
}
