package com.yamal.feature.network.implementation

import com.yamal.feature.network.api.ApiService
import com.yamal.feature.network.api.model.AccessToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

class ApiServiceImpl(private val httpClient: HttpClient) : ApiService {

    companion object {

        val authBaseUrl: String = "https://myanimelist.net/v1/oauth2"
    }

    override suspend fun getAccessToken(
        clientId: String,
        code: String,
        codeChallenge: String,
        grantType: String,
    ): AccessToken =
        httpClient.post("$authBaseUrl/token") {
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("client_id", clientId)
                        append("code", code)
                        append("code_verifier", codeChallenge)
                        append("grant_type", grantType)
                    },
                ),
            )
        }.body()

    override suspend fun refreshToken(
        clientId: String,
        refreshToken: String,
    ): AccessToken = httpClient.refreshToken(clientId, refreshToken)
}

suspend fun HttpClient.refreshToken(
    clientId: String,
    refreshToken: String,
    block: HttpRequestBuilder.() -> Unit = {},
): AccessToken = post("${ApiServiceImpl.authBaseUrl}token") {
    block()
    setBody(
        FormDataContent(
            Parameters.build {
                append("client_id", clientId)
                append("refresh_token", refreshToken)
                append("grant_type", "refresh_token")
            },
        ),
    )
}.body()

val ApiServiceImpl.authBaseUrl: String
    get() = "https://myanimelist.net/v1/oauth2"
