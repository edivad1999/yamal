package com.yamal.feature.network.implementation

import com.yamal.feature.network.api.KtorFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorFactoryImpl(private val json: Json) : KtorFactory {

    override fun createClient() = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpCache)
        engine {}
        install(HttpTimeout) {
            socketTimeoutMillis = Int.MAX_VALUE.toLong()
            connectTimeoutMillis = Int.MAX_VALUE.toLong()
            requestTimeoutMillis = Int.MAX_VALUE.toLong()
        }
        // TODO install logging with napier
        install(DefaultRequest) {
            header("X-MAL-CLIENT-ID", BuildKonfig.malClientId)
//                header(HttpHeaders.Authorization, "Bearer TODO") TODO AUTH TOKEN INJECTION
        }
    }
}
