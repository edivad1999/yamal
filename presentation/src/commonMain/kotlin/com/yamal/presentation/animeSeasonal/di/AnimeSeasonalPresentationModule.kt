package com.yamal.presentation.animeSeasonal.di

import com.yamal.presentation.animeSeasonal.presenter.AnimeSeasonalPresenter
import org.koin.dsl.module

object AnimeSeasonalPresentationModule {

    operator fun invoke() =
        module {
            single {
                AnimeSeasonalPresenter(get())
            }
        }
}
