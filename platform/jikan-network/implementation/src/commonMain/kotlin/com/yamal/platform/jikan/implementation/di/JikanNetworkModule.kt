package com.yamal.platform.jikan.implementation.di

import com.yamal.platform.jikan.api.JikanApiService
import com.yamal.platform.jikan.api.JikanRateLimiter
import com.yamal.platform.jikan.implementation.JikanApiServiceImpl
import com.yamal.platform.jikan.implementation.JikanKtorFactory
import com.yamal.platform.jikan.implementation.JikanRateLimiterImpl
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object JikanNetworkModule {
    operator fun invoke() =
        module {
            single {
                Json {
                    coerceInputValues = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            }
            single {
                JikanKtorFactory(json = get())
            }
            single<JikanRateLimiter> {
                JikanRateLimiterImpl()
            }
            single<JikanApiService> {
                JikanApiServiceImpl(
                    httpClient = get<JikanKtorFactory>().createClient(),
                    rateLimiter = get(),
                )
            }
        }
}
