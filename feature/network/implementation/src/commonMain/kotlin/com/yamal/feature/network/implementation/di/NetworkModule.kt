package com.yamal.feature.network.implementation.di

import com.yamal.feature.network.api.ApiService
import com.yamal.feature.network.api.BuildConstants
import com.yamal.feature.network.api.KtorFactory
import com.yamal.feature.network.implementation.ApiServiceImpl
import com.yamal.feature.network.implementation.BuildConstantsImpl
import com.yamal.feature.network.implementation.KtorFactoryImpl
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
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
            ApiServiceImpl(httpClient = get<KtorFactory>().createClient())
        }
    }
}
