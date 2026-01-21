package com.yamal.platform.datasource.implementation.di

import com.yamal.platform.datasource.api.AnimeDataSource
import com.yamal.platform.datasource.implementation.AnimeDataSourceImpl
import org.koin.dsl.module

object DataSourceModule {
    operator fun invoke() =
        module {
            single<AnimeDataSource> {
                AnimeDataSourceImpl(
                    jikanApiService = get(),
                    malApiService = get(),
                    jikanRateLimiter = get(),
                    loginRepository = get(),
                )
            }
        }
}
