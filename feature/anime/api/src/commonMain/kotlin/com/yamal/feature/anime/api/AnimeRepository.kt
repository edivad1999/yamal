package com.yamal.feature.anime.api

import com.yamal.feature.anime.api.model.AnimeRanking
import com.yamal.feature.anime.api.model.MalPagingSource
import com.yamal.feature.network.api.model.RankedAnime

interface AnimeRepository {

    fun getRanking(): MalPagingSource<RankedAnime, AnimeRanking>

    fun getAnimeDetails(id: Int)
}
