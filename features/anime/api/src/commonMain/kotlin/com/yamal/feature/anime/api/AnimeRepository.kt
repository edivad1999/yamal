package com.yamal.feature.anime.api

import androidx.paging.PagingSource
import arrow.core.Either
import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeListStatusYamal
import com.yamal.feature.anime.api.model.SeasonYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal

interface AnimeRepository {
    fun getRanking(): PagingSource<Int, AnimeForListYamal>

    fun getRankingByType(rankingType: String): PagingSource<Int, AnimeForListYamal>

    fun getSeasonal(
        season: SeasonYamal,
        year: String,
    ): PagingSource<Int, AnimeForListYamal>

    fun searchAnime(query: String): PagingSource<Int, AnimeForListYamal>

    fun getCurrentSeasonAnime(): PagingSource<Int, AnimeForListYamal>

    fun getUpcomingSeasonAnime(): PagingSource<Int, AnimeForListYamal>

    fun getUserAnimeList(status: UserListStatusYamal): PagingSource<Int, AnimeForListYamal>

    suspend fun getAnimeDetails(id: Int): Either<String, AnimeForDetailsYamal>

    suspend fun updateAnimeListStatus(
        animeId: Int,
        status: UserListStatusYamal?,
        score: Int?,
        numWatchedEpisodes: Int?,
        isRewatching: Boolean? = null,
        priority: Int? = null,
        numTimesRewatched: Int? = null,
        rewatchValue: Int? = null,
        tags: String? = null,
        comments: String? = null,
    ): Either<String, AnimeListStatusYamal>

    suspend fun deleteAnimeListStatus(animeId: Int): Either<String, Unit>

    suspend fun getAnimeSuggestions(limit: Int = 10): Either<String, List<AnimeForListYamal>>

    suspend fun getTrendingAnime(limit: Int = 10): Either<String, List<AnimeForListYamal>>

    suspend fun getTopAnime(limit: Int = 10): Either<String, List<AnimeForListYamal>>

    suspend fun getUpcomingAnime(limit: Int = 10): Either<String, List<AnimeForListYamal>>
}
