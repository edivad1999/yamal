package com.yamal.feature.preferences.implementation.di

import com.yamal.feature.preferences.implementation.factory.SettingsFactory
import com.yamal.feature.preferences.implementation.factory.SettingsFactoryIOS
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

actual fun Module.provideSettingsFactory(): KoinDefinition<SettingsFactory> = single {
    SettingsFactoryIOS()
}
