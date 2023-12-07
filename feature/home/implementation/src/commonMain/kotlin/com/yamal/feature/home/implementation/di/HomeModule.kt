package com.yamal.feature.home.implementation.di

import com.yamal.feature.home.api.HomeRepository
import com.yamal.feature.home.implementation.HomeRepositoryImpl
import org.koin.dsl.module

object HomeModule {

    operator fun invoke() = module {
        single<HomeRepository> {
            HomeRepositoryImpl()
        }
    }
}
