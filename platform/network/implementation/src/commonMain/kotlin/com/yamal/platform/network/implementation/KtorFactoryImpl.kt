package com.yamal.platform.network.implementation

import com.yamal.platform.network.api.BuildConstants
import com.yamal.platform.network.api.KtorFactory
import com.yamal.platform.storage.api.PreferencesDatasource
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorFactoryImpl(
    private val json: Json,
    private val preferencesDatasource: PreferencesDatasource,
    private val buildConstants: BuildConstants,
) : KtorFactory {
    override fun createClient() =
        HttpClient {
            install(ContentNegotiation) {
                json(json)
            }
            install(HttpCache)
            engine {}
            expectSuccess = true
            install(HttpTimeout) {
                socketTimeoutMillis = Int.MAX_VALUE.toLong()
                connectTimeoutMillis = Int.MAX_VALUE.toLong()
                requestTimeoutMillis = Int.MAX_VALUE.toLong()
            }
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Napier.i(message, tag = "HttpClient")
                        }
                    }
                level = LogLevel.ALL
            }
            install(HttpRequestRetry) {
                maxRetries = 3
                retryIf { request, response ->
                    !response.status.isSuccess() && response.status != HttpStatusCode.NotFound
                }
                exponentialDelay()
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = preferencesDatasource.getAccessToken() ?: return@loadTokens null
                        BearerTokens(accessToken.accessToken, accessToken.refreshToken)
                    }
                    refreshTokens {
                        val oldRefreshToken = preferencesDatasource.getAccessToken()?.refreshToken ?: return@refreshTokens null
                        client
                            .refreshToken(
                                clientId = buildConstants.malClientId,
                                refreshToken = oldRefreshToken,
                            ) {
                                markAsRefreshTokenRequest()
                            }.let {
                                preferencesDatasource.setAccessToken(it)
                                BearerTokens(it.accessToken, it.refreshToken)
                            }
                    }
                }
            }
            install(DefaultRequest) {
                header("X-MAL-CLIENT-ID", BuildKonfig.malClientId)
            }
        }
}
