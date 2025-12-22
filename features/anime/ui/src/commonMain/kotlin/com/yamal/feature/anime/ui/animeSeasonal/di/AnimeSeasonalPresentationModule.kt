package com.yamal.feature.anime.ui.animeSeasonal.di

import com.yamal.feature.anime.ui.animeSeasonal.presenter.AnimeSeasonalPresenter
import org.koin.dsl.module

object AnimeSeasonalPresentationModule {
    operator fun invoke() =
        module {
            single {
                AnimeSeasonalPresenter(get())
            }
        }
}
