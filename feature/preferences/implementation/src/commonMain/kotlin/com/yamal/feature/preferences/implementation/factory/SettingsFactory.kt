package com.yamal.feature.preferences.implementation.factory

import com.russhwolf.settings.Settings

interface SettingsFactory {

    fun createSettings(): Settings
}

