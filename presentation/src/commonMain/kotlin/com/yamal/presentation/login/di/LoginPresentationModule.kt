package com.yamal.presentation.login.di

import com.yamal.presentation.login.presenter.LoginPresenter
import org.koin.dsl.module

object LoginPresentationModule {

    operator fun invoke() = module {
        single {
            LoginPresenter(loginRepository = get())
        }
    }
}
