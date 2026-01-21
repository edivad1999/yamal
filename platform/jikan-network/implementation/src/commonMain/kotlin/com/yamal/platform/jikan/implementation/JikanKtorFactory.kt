package com.yamal.platform.jikan.implementation

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory for creating an HTTP client configured for Jikan API.
 * No authentication required (Jikan is a public API).
 */
class JikanKtorFactory(
    private val json: Json,
) {
    fun createClient(): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(json)
            }
            install(HttpCache)
            engine {}
            expectSuccess = true
            install(HttpTimeout) {
                socketTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                requestTimeoutMillis = 30_000
            }
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Napier.i(message, tag = "JikanHttpClient")
                        }
                    }
                level = LogLevel.ALL
            }
            install(HttpRequestRetry) {
                maxRetries = 3
                retryIf { _, response ->
                    !response.status.isSuccess() &&
                        response.status != HttpStatusCode.NotFound &&
                        response.status != HttpStatusCode.TooManyRequests
                }
                exponentialDelay()
            }
        }
}
