package com.yamal.platform.network.api

import com.yamal.platform.network.api.model.AccessToken
import com.yamal.platform.network.api.model.anime.AnimeForDetailsMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeListStatusMalNetwork
import com.yamal.platform.network.api.model.list.AnimeNodeEdgeMalNetwork
import com.yamal.platform.network.api.model.list.AnimeRankingEdgeMalNetwork
import com.yamal.platform.network.api.model.list.ListMalNetwork
import com.yamal.platform.network.api.model.list.MangaNodeEdgeMalNetwork
import com.yamal.platform.network.api.model.list.MangaRankingEdgeMalNetwork
import com.yamal.platform.network.api.model.list.UserAnimeListEdgeMalNetwork
import com.yamal.platform.network.api.model.list.UserMangaListEdgeMalNetwork
import com.yamal.platform.network.api.model.manga.MangaForDetailsMalNetwork
import com.yamal.platform.network.api.model.manga.MangaListStatusMalNetwork
import com.yamal.platform.network.api.model.user.UserMalNetwork

interface ApiService {
    // Authentication
    suspend fun getAccessToken(
        code: String,
        codeChallenge: String,
        grantType: String,
    ): AccessToken

    suspend fun refreshToken(refreshToken: String): AccessToken

    // Anime List
    suspend fun getAnimeRanking(
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeRankingEdgeMalNetwork>

    suspend fun getAnimeRankingByType(
        rankingType: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeRankingEdgeMalNetwork>

    suspend fun getSeasonalAnime(
        season: String,
        year: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeNodeEdgeMalNetwork>

    suspend fun getAnimeSuggestions(
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeNodeEdgeMalNetwork>

    suspend fun searchAnime(
        query: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<AnimeNodeEdgeMalNetwork>

    suspend fun getAnimeDetails(animeId: Int): AnimeForDetailsMalNetwork

    // User Anime List
    suspend fun getUserAnimeList(
        userListStatus: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<UserAnimeListEdgeMalNetwork>

    suspend fun updateAnimeListStatus(
        animeId: Int,
        status: String? = null,
        score: Int? = null,
        numWatchedEpisodes: Int? = null,
        isRewatching: Boolean? = null,
        priority: Int? = null,
        numTimesRewatched: Int? = null,
        rewatchValue: Int? = null,
        tags: String? = null,
        comments: String? = null,
    ): AnimeListStatusMalNetwork

    suspend fun deleteAnimeListStatus(animeId: Int)

    // Manga List
    suspend fun getMangaRanking(
        rankingType: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<MangaRankingEdgeMalNetwork>

    suspend fun searchManga(
        query: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<MangaNodeEdgeMalNetwork>

    suspend fun getMangaDetails(mangaId: Int): MangaForDetailsMalNetwork

    // User Manga List
    suspend fun getUserMangaList(
        userListStatus: String,
        limit: Int,
        offset: Int,
    ): ListMalNetwork<UserMangaListEdgeMalNetwork>

    suspend fun updateMangaListStatus(
        mangaId: Int,
        status: String? = null,
        score: Int? = null,
        numVolumesRead: Int? = null,
        numChaptersRead: Int? = null,
        isRereading: Boolean? = null,
        priority: Int? = null,
        numTimesReread: Int? = null,
        rereadValue: Int? = null,
        tags: String? = null,
        comments: String? = null,
    ): MangaListStatusMalNetwork

    suspend fun deleteMangaListStatus(mangaId: Int)

    // User Profile
    suspend fun getUserProfile(): UserMalNetwork
}
