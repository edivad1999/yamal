package com.yamal.feature.anime.ui.userAnimeList.di

import com.yamal.feature.anime.ui.userAnimeList.presenter.UserAnimeListPresenter
import org.koin.dsl.module

object UserAnimeListPresentationModule {

    operator fun invoke() =
        module {
            single {
                UserAnimeListPresenter(get(), get())
            }
        }
}
