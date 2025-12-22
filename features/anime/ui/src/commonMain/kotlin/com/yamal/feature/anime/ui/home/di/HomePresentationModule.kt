package com.yamal.feature.anime.ui.home.di

import com.yamal.feature.anime.ui.home.presenter.HomePresenter
import org.koin.dsl.module

object HomePresentationModule {

    operator fun invoke() = module {
        single {
            HomePresenter(get())
        }
    }
}
