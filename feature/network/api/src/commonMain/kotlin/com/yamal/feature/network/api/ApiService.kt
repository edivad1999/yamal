package com.yamal.feature.network.api

import com.yamal.feature.network.api.model.AccessToken
import com.yamal.feature.network.api.model.AnimeRankingNetwork

interface ApiService {

    suspend fun getAccessToken(
        code: String,
        codeChallenge: String,
        grantType: String,
    ): AccessToken

    suspend fun refreshToken(
        refreshToken: String,
    ): AccessToken

    suspend fun getAnimeRanking(limit: Int, offset: Int): AnimeRankingNetwork
}
