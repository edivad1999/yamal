package com.yamal.feature.network.api

import com.yamal.feature.network.api.model.AccessToken
import com.yamal.feature.network.api.model.AnimeRanking

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

    suspend fun getAnimeRanking(): AnimeRanking
}
