package com.yamal.feature.manga.ui.mangaDetails.di

import com.yamal.feature.manga.ui.mangaDetails.presenter.MangaDetailsPresenter
import org.koin.dsl.module

object MangaDetailsPresentationModule {
    operator fun invoke() =
        module {
            single { (mangaId: Int) ->
                MangaDetailsPresenter(get(), mangaId)
            }
        }
}
