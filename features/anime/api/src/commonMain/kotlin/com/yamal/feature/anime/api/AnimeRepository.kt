package com.yamal.feature.anime.api

import arrow.core.Either
import com.yamal.feature.anime.api.model.AnimeDetails
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.feature.anime.api.model.Season
import com.yamal.feature.anime.api.model.UserListStatus
import com.yamal.platform.network.api.model.Anime
import com.yamal.platform.network.api.model.RankedAnime
import com.yamal.platform.utils.MalPagingSource

interface AnimeRepository {
    fun getRanking(): MalPagingSource<RankedAnime, GenericAnime>

    fun getSeasonal(
        season: Season,
        year: String,
    ): MalPagingSource<Anime, GenericAnime>

    fun getUserAnimeList(status: UserListStatus): MalPagingSource<Anime, GenericAnime>

    suspend fun getAnimeDetails(id: Int): Either<String, AnimeDetails>
}
