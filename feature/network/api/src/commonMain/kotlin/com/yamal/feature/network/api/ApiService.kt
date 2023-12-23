package com.yamal.feature.network.api

import com.yamal.feature.network.api.model.AccessToken

interface ApiService {

    suspend fun getAccessToken(
        clientId: String,
        code: String,
        codeChallenge: String,
        grantType: String,
    ): AccessToken

    suspend fun refreshToken(
        clientId: String,
        refreshToken: String,
    ): AccessToken
}
