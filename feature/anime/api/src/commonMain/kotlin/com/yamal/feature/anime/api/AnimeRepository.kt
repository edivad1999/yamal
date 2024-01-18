package com.yamal.feature.anime.api

import arrow.core.Either
import com.yamal.feature.anime.api.model.AnimeDetails
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.feature.anime.api.model.Season
import com.yamal.feature.anime.api.model.UserListStatus
import com.yamal.feature.core.MalPagingSource
import com.yamal.feature.network.api.ApiError
import com.yamal.feature.network.api.model.Anime
import com.yamal.feature.network.api.model.RankedAnime

interface AnimeRepository {
    fun getRanking(): MalPagingSource<RankedAnime, GenericAnime>

    fun getSeasonal(
        season: Season,
        year: String,
    ): MalPagingSource<Anime, GenericAnime>

    fun getUserAnimeList(status: UserListStatus): MalPagingSource<Anime, GenericAnime>

    suspend fun getAnimeDetails(id: Int): Either<String?, AnimeDetails>
}
