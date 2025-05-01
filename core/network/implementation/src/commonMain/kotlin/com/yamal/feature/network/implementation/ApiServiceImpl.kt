package com.yamal.feature.network.implementation

import com.yamal.feature.network.api.ApiService
import com.yamal.feature.network.api.BuildConstants
import com.yamal.feature.network.api.model.AccessToken
import com.yamal.feature.network.api.model.AnimeDetailsNetwork
import com.yamal.feature.network.api.model.AnimeRankingNetwork
import com.yamal.feature.network.api.model.AnimeRequestField
import com.yamal.feature.network.api.model.PagingData
import com.yamal.feature.network.api.model.RelatedAnimeEdge
import com.yamal.feature.network.api.model.SeasonalAnime
import com.yamal.feature.network.api.model.UserListAnime
import com.yamal.feature.network.api.model.mergeToRequestString
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

class ApiServiceImpl(
    private val httpClient: HttpClient,
    private val buildConstants: BuildConstants,
) : ApiService {

    companion object {

        val authBaseUrl: String = "https://myanimelist.net/v1/oauth2"
        val malBaseUrl: String = "https://api.myanimelist.net/v2"
    }

    override suspend fun getAccessToken(
        code: String,
        codeChallenge: String,
        grantType: String,
    ): AccessToken =
        httpClient.post("$authBaseUrl/token") {
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("client_id", buildConstants.malClientId)
                        append("code", code)
                        append("code_verifier", codeChallenge)
                        append("grant_type", grantType)
                    },
                ),
            )
        }.body()

    override suspend fun refreshToken(refreshToken: String): AccessToken = httpClient.refreshToken(buildConstants.malClientId, refreshToken)

    override suspend fun getAnimeRanking(
        limit: Int,
        offset: Int,
    ): AnimeRankingNetwork =
        httpClient.get("$malBaseUrl/anime/ranking") {
            parameter("ranking_type", "all")
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
        }.body()

    override suspend fun getSeasonalAnime(
        season: String,
        year: String,
        limit: Int,
        offset: Int,
    ): SeasonalAnime =
        httpClient.get("$malBaseUrl/anime/season/$year/$season") {
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("sort", "anime_num_list_users")
            parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
        }.body<PagingData<RelatedAnimeEdge>>().let {
            PagingData(
                data = it.data.map { it.node },
                it.paging,
            )
        }

    override suspend fun getUserAnimeList(
        userListStatus: String,
        limit: Int,
        offset: Int,
    ): UserListAnime =
        httpClient.get("$malBaseUrl/users/@me/animelist") {
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("status", userListStatus)
            parameter("sort", "anime_start_date")
            parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
        }.body<PagingData<RelatedAnimeEdge>>().let {
            PagingData(
                data = it.data.map { it.node },
                it.paging,
            )
        }

    override suspend fun getAnimeDetails(
        animeId: Int
    ): AnimeDetailsNetwork =
        httpClient.get("$malBaseUrl/anime/$animeId") {
            parameter("fields", AnimeRequestField.animeDetailsFields().mergeToRequestString())
        }.body()
}
