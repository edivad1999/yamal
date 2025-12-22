package com.yamal.platform.storage.implementation.factory

import com.russhwolf.settings.Settings

interface SettingsFactory {
    fun createSettings(): Settings
}
