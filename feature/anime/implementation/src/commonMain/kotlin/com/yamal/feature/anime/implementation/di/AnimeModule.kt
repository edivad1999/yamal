package com.yamal.feature.anime.implementation.di

import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.implementation.AnimeRepositoryImpl
import org.koin.dsl.module

object AnimeModule {

    operator fun invoke() = module {
        single<AnimeRepository> {
            AnimeRepositoryImpl(apiService = get())
        }
    }
}
