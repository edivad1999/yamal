package com.yamal.shared

import com.yamal.feature.anime.implementation.di.AnimeModule
import com.yamal.feature.login.implementation.di.LoginModule
import com.yamal.feature.network.implementation.di.NetworkModule
import com.yamal.feature.preferences.implementation.di.PreferencesModule
import com.yamal.presentation.home.di.HomePresentationModule
import com.yamal.presentation.login.di.LoginPresentationModule

object AppModules {

    fun exportModules() = listOf(
        HomePresentationModule(),
        LoginModule(),
        AnimeModule(),
        LoginPresentationModule(),
        NetworkModule(),
        PreferencesModule()
    )
}
