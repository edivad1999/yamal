package com.yamal.feature.anime.ui.animeRanking.di

import com.yamal.feature.anime.ui.animeRanking.presenter.AnimeRankingPresenter
import org.koin.dsl.module

object AnimeRankingPresentationModule {
    operator fun invoke() =
        module {
            single {
                AnimeRankingPresenter(get())
            }
        }
}
