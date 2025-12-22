package com.yamal.platform.storage.implementation.di

import com.yamal.platform.storage.api.PreferencesDatasource
import com.yamal.platform.storage.implementation.PreferencesDatasourceImpl
import com.yamal.platform.storage.implementation.factory.SettingsFactory
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.module

object PreferencesModule {

    operator fun invoke() = module {
        provideSettingsFactory()

        single<PreferencesDatasource> {
            PreferencesDatasourceImpl(get<SettingsFactory>().createSettings())
        }
    }
}

expect fun Module.provideSettingsFactory(): KoinDefinition<SettingsFactory>
