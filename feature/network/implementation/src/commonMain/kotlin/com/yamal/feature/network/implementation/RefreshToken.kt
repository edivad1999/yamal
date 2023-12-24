package com.yamal.feature.network.implementation

import com.yamal.feature.network.api.model.AccessToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

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