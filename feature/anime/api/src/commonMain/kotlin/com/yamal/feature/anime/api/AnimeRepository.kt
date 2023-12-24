package com.yamal.feature.anime.api

import arrow.core.Either
import com.yamal.feature.anime.api.model.AnimeRanking

interface AnimeRepository {

    suspend fun getRanking(): Either<String, List<AnimeRanking>>
}
