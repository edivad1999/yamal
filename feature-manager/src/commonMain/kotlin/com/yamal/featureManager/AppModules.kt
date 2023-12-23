package com.yamal.featureManager

import com.yamal.feature.home.implementation.di.HomeModule
import com.yamal.feature.login.implementation.di.LoginModule
import com.yamal.feature.network.implementation.di.NetworkModule
import com.yamal.feature.preferences.implementation.di.PreferencesModule
import com.yamal.presentation.home.di.HomePresentationModule
import com.yamal.presentation.login.di.LoginPresentationModule

object AppModules {

    fun exportModules() = listOf(
        HomeModule(),
        HomePresentationModule(),
        LoginModule(),
        LoginPresentationModule(),
        NetworkModule(),
        PreferencesModule()
    )
}
