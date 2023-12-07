package com.yamal.featureManager

import com.yamal.feature.home.implementation.di.HomeModule
import com.yamal.presentation.home.di.HomePresentationModule

object AppModules {

    fun exportModules() = listOf(
        HomeModule(),
        HomePresentationModule(),
    )
}
