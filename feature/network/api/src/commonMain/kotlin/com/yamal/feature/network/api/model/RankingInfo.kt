package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RankingInfo(
    val rank: Int,
    val previous_rank: Int?
)
