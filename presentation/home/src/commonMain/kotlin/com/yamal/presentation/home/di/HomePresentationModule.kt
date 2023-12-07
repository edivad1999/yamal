package com.yamal.presentation.home.di

import com.yamal.presentation.home.presenter.CounterPresenter
import org.koin.dsl.module

object HomePresentationModule {

    operator fun invoke() = module {
        single {
            CounterPresenter(get())
        }
    }
}
