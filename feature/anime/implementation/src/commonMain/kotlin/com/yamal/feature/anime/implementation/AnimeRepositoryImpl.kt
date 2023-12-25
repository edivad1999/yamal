package com.yamal.feature.anime.implementation

import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeRanking
import com.yamal.feature.anime.api.model.MalPagingSource
import com.yamal.feature.anime.implementation.mapping.toModel
import com.yamal.feature.network.api.ApiService
import com.yamal.feature.network.api.apiCallScope

class AnimeRepositoryImpl(private val apiService: ApiService) : AnimeRepository {

    override fun getRanking() = MalPagingSource(apiCall = { pageSize, offset ->
        apiCallScope {
            apiService.getAnimeRanking(limit = pageSize, offset = offset)
        }
    }, map = {
        AnimeRanking(
            id = it.node.id, title = it.node.title, mainPicture = it.node.mainPicture?.toModel(), rank = it.ranking.rank
        )
    })
}

