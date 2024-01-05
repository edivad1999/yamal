package com.yamal.presentation.animeRanking.di

import com.yamal.presentation.animeRanking.presenter.AnimeRankingPresenter
import org.koin.dsl.module

object AnimeRankingPresentationModule {
    operator fun invoke() =
        module {
            single {
                AnimeRankingPresenter(get())
            }
        }
}
