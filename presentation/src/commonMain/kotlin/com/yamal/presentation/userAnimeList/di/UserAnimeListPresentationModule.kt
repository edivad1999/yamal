package com.yamal.presentation.userAnimeList.di

import com.yamal.presentation.userAnimeList.presenter.UserAnimeListPresenter
import org.koin.dsl.module

object UserAnimeListPresentationModule {

    operator fun invoke() =
        module {
            single {
                UserAnimeListPresenter(get(), get())
            }
        }
}
