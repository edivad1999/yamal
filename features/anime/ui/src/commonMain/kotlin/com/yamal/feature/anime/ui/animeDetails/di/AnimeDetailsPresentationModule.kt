package com.yamal.feature.anime.ui.animeDetails.di

import com.yamal.feature.anime.ui.animeDetails.presenter.AnimeDetailsPresenter
import org.koin.dsl.module

object AnimeDetailsPresentationModule {
    operator fun invoke() =
        module {
            factory { (animeId: Int) ->
                AnimeDetailsPresenter(get(), animeId)
            }
        }
}
