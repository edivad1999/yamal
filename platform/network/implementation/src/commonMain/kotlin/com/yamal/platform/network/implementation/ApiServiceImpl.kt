package com.yamal.platform.network.implementation

import com.yamal.platform.network.api.ApiService
import com.yamal.platform.network.api.BuildConstants
import com.yamal.platform.network.api.model.AccessToken
import com.yamal.platform.network.api.model.anime.AnimeForDetailsMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeListStatusMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeRequestField
import com.yamal.platform.network.api.model.anime.mergeToRequestString
import com.yamal.platform.network.api.model.list.AnimeNodeEdgeMalNetwork
import com.yamal.platform.network.api.model.list.AnimeRankingEdgeMalNetwork
import com.yamal.platform.network.api.model.list.ListMalNetwork
import com.yamal.platform.network.api.model.list.MangaNodeEdgeMalNetwork
import com.yamal.platform.network.api.model.list.MangaRankingEdgeMalNetwork
import com.yamal.platform.network.api.model.list.UserAnimeListEdgeMalNetwork
import com.yamal.platform.network.api.model.list.UserMangaListEdgeMalNetwork
import com.yamal.platform.network.api.model.manga.MangaForDetailsMalNetwork
import com.yamal.platform.network.api.model.manga.MangaListStatusMalNetwork
import com.yamal.platform.network.api.model.manga.MangaRequestField
import com.yamal.platform.network.api.model.manga.mergeToRequestString
import com.yamal.platform.network.api.model.user.UserMalNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
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
        httpClient
            .post("$authBaseUrl/token") {
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
    ): ListMalNetwork<AnimeRankingEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/anime/ranking") {
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
    ): ListMalNetwork<AnimeNodeEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/anime/season/$year/$season") {
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("sort", "anime_num_list_users")
                parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
            }.body()

    override suspend fun getUserAnimeList(
        userListStatus: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<UserAnimeListEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/users/@me/animelist") {
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("status", userListStatus)
                parameter("sort", "anime_start_date")
                parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
            }.body()

    override suspend fun getAnimeDetails(animeId: Int): AnimeForDetailsMalNetwork =
        httpClient
            .get("$malBaseUrl/anime/$animeId") {
                parameter("fields", AnimeRequestField.animeDetailsFields().mergeToRequestString())
            }.body()

    override suspend fun getAnimeRankingByType(
        rankingType: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeRankingEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/anime/ranking") {
                parameter("ranking_type", rankingType)
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
            }.body()

    override suspend fun getAnimeSuggestions(
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeNodeEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/anime/suggestions") {
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
            }.body()

    override suspend fun searchAnime(
        query: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeNodeEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/anime") {
                parameter("q", query)
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("fields", AnimeRequestField.animeRankingFields().mergeToRequestString())
            }.body()

    override suspend fun updateAnimeListStatus(
        animeId: Int,
        status: String?,
        score: Int?,
        numWatchedEpisodes: Int?,
        isRewatching: Boolean?,
        priority: Int?,
        numTimesRewatched: Int?,
        rewatchValue: Int?,
        tags: String?,
        comments: String?,
    ): AnimeListStatusMalNetwork =
        httpClient
            .patch("$malBaseUrl/anime/$animeId/my_list_status") {
                setBody(
                    FormDataContent(
                        Parameters.build {
                            status?.let { append("status", it) }
                            score?.let { append("score", it.toString()) }
                            numWatchedEpisodes?.let { append("num_watched_episodes", it.toString()) }
                            isRewatching?.let { append("is_rewatching", it.toString()) }
                            priority?.let { append("priority", it.toString()) }
                            numTimesRewatched?.let { append("num_times_rewatched", it.toString()) }
                            rewatchValue?.let { append("rewatch_value", it.toString()) }
                            tags?.let { append("tags", it) }
                            comments?.let { append("comments", it) }
                        },
                    ),
                )
            }.body()

    override suspend fun deleteAnimeListStatus(animeId: Int) {
        httpClient.delete("$malBaseUrl/anime/$animeId/my_list_status")
    }

    // Manga endpoints
    override suspend fun getMangaRanking(
        rankingType: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<MangaRankingEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/manga/ranking") {
                parameter("ranking_type", rankingType)
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("fields", MangaRequestField.mangaRankingFields().mergeToRequestString())
            }.body()

    override suspend fun searchManga(
        query: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<MangaNodeEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/manga") {
                parameter("q", query)
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("fields", MangaRequestField.mangaRankingFields().mergeToRequestString())
            }.body()

    override suspend fun getMangaDetails(mangaId: Int): MangaForDetailsMalNetwork =
        httpClient
            .get("$malBaseUrl/manga/$mangaId") {
                parameter("fields", MangaRequestField.mangaDetailsFields().mergeToRequestString())
            }.body()

    override suspend fun getUserMangaList(
        userListStatus: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<UserMangaListEdgeMalNetwork> =
        httpClient
            .get("$malBaseUrl/users/@me/mangalist") {
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("status", userListStatus)
                parameter("sort", "manga_start_date")
                parameter("fields", MangaRequestField.mangaRankingFields().mergeToRequestString())
            }.body()

    override suspend fun updateMangaListStatus(
        mangaId: Int,
        status: String?,
        score: Int?,
        numVolumesRead: Int?,
        numChaptersRead: Int?,
        isRereading: Boolean?,
        priority: Int?,
        numTimesReread: Int?,
        rereadValue: Int?,
        tags: String?,
        comments: String?,
    ): MangaListStatusMalNetwork =
        httpClient
            .patch("$malBaseUrl/manga/$mangaId/my_list_status") {
                setBody(
                    FormDataContent(
                        Parameters.build {
                            status?.let { append("status", it) }
                            score?.let { append("score", it.toString()) }
                            numVolumesRead?.let { append("num_volumes_read", it.toString()) }
                            numChaptersRead?.let { append("num_chapters_read", it.toString()) }
                            isRereading?.let { append("is_rereading", it.toString()) }
                            priority?.let { append("priority", it.toString()) }
                            numTimesReread?.let { append("num_times_reread", it.toString()) }
                            rereadValue?.let { append("reread_value", it.toString()) }
                            tags?.let { append("tags", it) }
                            comments?.let { append("comments", it) }
                        },
                    ),
                )
            }.body()

    override suspend fun deleteMangaListStatus(mangaId: Int) {
        httpClient.delete("$malBaseUrl/manga/$mangaId/my_list_status")
    }

    // User Profile
    override suspend fun getUserProfile(): UserMalNetwork =
        httpClient
            .get("$malBaseUrl/users/@me") {
                parameter("fields", "anime_statistics")
            }.body()
}
