package com.yamal.feature.preferences.implementation.factory

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings

class SettingsFactoryIOS : SettingsFactory {

    override fun createSettings(): Settings = NSUserDefaultsSettings.Factory().create("prefs")
}
