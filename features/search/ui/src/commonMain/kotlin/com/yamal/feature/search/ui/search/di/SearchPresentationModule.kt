package com.yamal.feature.search.ui.search.di

import com.yamal.feature.search.ui.search.presenter.SearchPresenter
import org.koin.dsl.module

object SearchPresentationModule {
    operator fun invoke() =
        module {
            single {
                SearchPresenter(searchRepository = get())
            }
        }
}
