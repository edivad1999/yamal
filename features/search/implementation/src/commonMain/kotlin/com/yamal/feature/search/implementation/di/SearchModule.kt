package com.yamal.feature.search.implementation.di

import com.yamal.feature.search.api.SearchRepository
import com.yamal.feature.search.implementation.SearchRepositoryImpl
import org.koin.dsl.module

object SearchModule {
    operator fun invoke() =
        module {
            single<SearchRepository> {
                SearchRepositoryImpl(animeDataSource = get())
            }
        }
}
