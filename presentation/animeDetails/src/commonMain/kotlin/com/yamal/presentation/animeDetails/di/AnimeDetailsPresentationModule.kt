package com.yamal.presentation.animeDetails.di


import com.yamal.presentation.animeDetails.presenter.AnimeDetailsPresenter
import org.koin.dsl.module

object AnimeDetailsPresentationModule {
    operator fun invoke() =
        module {
            single {
                (animeId: Int) -> AnimeDetailsPresenter(get(), animeId)
            }
        }
}
