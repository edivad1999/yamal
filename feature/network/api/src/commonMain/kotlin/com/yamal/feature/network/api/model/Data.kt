package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val node: Anime,
    val ranking: RankingInfo
)
