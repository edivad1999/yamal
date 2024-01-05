package com.yamal.feature.anime.api

import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.feature.anime.api.model.MalPagingSource
import com.yamal.feature.anime.api.model.Season
import com.yamal.feature.network.api.model.Anime
import com.yamal.feature.network.api.model.RankedAnime

interface AnimeRepository {
    fun getRanking(): MalPagingSource<RankedAnime, GenericAnime>

    fun getSeasonal(
        season: Season,
        year: String,
    ): MalPagingSource<Anime, GenericAnime>

    fun getAnimeDetails(id: Int)
}
