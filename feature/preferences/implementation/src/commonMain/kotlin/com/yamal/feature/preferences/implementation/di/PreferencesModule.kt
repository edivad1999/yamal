package com.yamal.feature.preferences.implementation.di

import com.yamal.feature.preferences.api.PreferencesDatasource
import com.yamal.feature.preferences.implementation.PreferencesDatasourceImpl
import com.yamal.feature.preferences.implementation.factory.SettingsFactory
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
