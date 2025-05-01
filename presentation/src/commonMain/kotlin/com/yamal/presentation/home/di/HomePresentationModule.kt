package com.yamal.presentation.home.di

import com.yamal.presentation.home.presenter.HomePresenter
import org.koin.dsl.module

object HomePresentationModule {

    operator fun invoke() = module {
        single {
            HomePresenter(get())
        }
    }
}
