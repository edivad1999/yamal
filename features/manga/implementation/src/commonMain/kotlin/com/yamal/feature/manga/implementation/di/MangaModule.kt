package com.yamal.feature.manga.implementation.di

import com.yamal.feature.manga.api.MangaRepository
import com.yamal.feature.manga.implementation.MangaRepositoryImpl
import org.koin.dsl.module

object MangaModule {
    operator fun invoke() =
        module {
            single<MangaRepository> {
                MangaRepositoryImpl(apiService = get())
            }
        }
}
