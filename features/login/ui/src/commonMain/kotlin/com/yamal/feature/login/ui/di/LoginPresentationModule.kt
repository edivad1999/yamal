package com.yamal.feature.login.ui.di

import com.yamal.feature.login.ui.presenter.LoginPresenter
import org.koin.dsl.module

object LoginPresentationModule {
    operator fun invoke() =
        module {
            single {
                LoginPresenter(loginRepository = get())
            }
        }
}
