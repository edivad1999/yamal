package com.yamal.feature.anime.implementation

import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeRanking
import com.yamal.feature.anime.api.model.toModel
import com.yamal.feature.network.api.ApiService
import com.yamal.feature.network.api.apiCallScope

class AnimeRepositoryImpl(private val apiService: ApiService) : AnimeRepository {

    override suspend fun getRanking() = apiCallScope {
        apiService.getAnimeRanking().data.map {
            AnimeRanking(
                id = it.node.id,
                title = it.node.title,
                mainPicture = it.node.main_picture?.toModel(),
                rank = it.ranking.rank
            )
        }
    }.mapLeft {
        it.throwable.message?: "error"
    }
}
