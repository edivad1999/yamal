package com.yamal.platform.network.implementation.di

import com.yamal.platform.network.api.ApiService
import com.yamal.platform.network.api.BuildConstants
import com.yamal.platform.network.api.KtorFactory
import com.yamal.platform.network.implementation.ApiServiceImpl
import com.yamal.platform.network.implementation.BuildConstantsImpl
import com.yamal.platform.network.implementation.KtorFactoryImpl
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object NetworkModule {

    operator fun invoke() = module {
        single<BuildConstants> {
            BuildConstantsImpl()
        }
        single {
            Json {
                coerceInputValues = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        }
        single<KtorFactory> {
            KtorFactoryImpl(json = get(), preferencesDatasource = get(), buildConstants = get())
        }
        single<ApiService> {
            ApiServiceImpl(httpClient = get<KtorFactory>().createClient(), buildConstants = get())
        }
    }
}
