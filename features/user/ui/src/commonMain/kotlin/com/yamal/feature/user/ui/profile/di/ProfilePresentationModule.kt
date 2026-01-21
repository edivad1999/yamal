package com.yamal.feature.user.ui.profile.di

import com.yamal.feature.user.ui.profile.presenter.ProfilePresenter
import org.koin.dsl.module

object ProfilePresentationModule {
    operator fun invoke() =
        module {
            single {
                ProfilePresenter(userRepository = get())
            }
        }
}
