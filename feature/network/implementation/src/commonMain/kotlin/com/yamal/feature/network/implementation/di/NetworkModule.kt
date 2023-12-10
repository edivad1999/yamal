package com.yamal.feature.network.implementation.di

import com.yamal.feature.network.api.ApiService
import com.yamal.feature.network.api.BuildConstants
import com.yamal.feature.network.api.KtorFactory
import com.yamal.feature.network.implementation.ApiServiceImpl
import com.yamal.feature.network.implementation.BuildConstantsImpl
import com.yamal.feature.network.implementation.KtorFactoryImpl
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object NetworkModule {

    operator fun invoke() = module {
        single<BuildConstants> {
            BuildConstantsImpl()
        }
        single {
            Json {
                // TODO JSON settings
            }
        }
        single<KtorFactory> {
            KtorFactoryImpl(json = get())
        }
        single<ApiService> {
            ApiServiceImpl(httpClient = get<KtorFactory>().createClient())
        }
    }
}
