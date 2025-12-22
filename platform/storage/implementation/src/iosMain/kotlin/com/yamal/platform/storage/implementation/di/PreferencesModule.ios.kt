package com.yamal.platform.storage.implementation.di

import com.yamal.platform.storage.implementation.factory.SettingsFactory
import com.yamal.platform.storage.implementation.factory.SettingsFactoryIOS
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

actual fun Module.provideSettingsFactory(): KoinDefinition<SettingsFactory> = single {
    SettingsFactoryIOS()
}
