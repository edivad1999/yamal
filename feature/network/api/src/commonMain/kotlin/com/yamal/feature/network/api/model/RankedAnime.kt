package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RankedAnime(
    val node: Anime,
    val ranking: RankingInfo
)
