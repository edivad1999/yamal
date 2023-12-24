package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeRanking(
    val data: List<RankedAnime>,
    val paging: Paging
)
