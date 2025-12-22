package com.yamal.platform.network.api

import com.yamal.platform.network.api.model.AccessToken
import com.yamal.platform.network.api.model.AnimeDetailsNetwork
import com.yamal.platform.network.api.model.AnimeRankingNetwork
import com.yamal.platform.network.api.model.SeasonalAnime
import com.yamal.platform.network.api.model.UserListAnime

interface ApiService {
    suspend fun getAccessToken(
        code: String,
        codeChallenge: String,
        grantType: String,
    ): AccessToken

    suspend fun refreshToken(refreshToken: String): AccessToken

    suspend fun getAnimeRanking(
        limit: Int,
        offset: Int,
    ): AnimeRankingNetwork

    suspend fun getSeasonalAnime(
        season: String,
        year: String,
        limit: Int,
        offset: Int,
    ): SeasonalAnime

    suspend fun getUserAnimeList(
        userListStatus: String,
        limit: Int,
        offset: Int,
    ): UserListAnime

    suspend fun getAnimeDetails(animeId: Int): AnimeDetailsNetwork
}
