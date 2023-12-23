package com.yamal.feature.preferences.implementation.factory

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings

class SettingsFactoryDesktop : SettingsFactory {

    override fun createSettings(): Settings {
        val settings: Settings = PreferencesSettings.Factory().create("prefs")
        return settings
    }
}
