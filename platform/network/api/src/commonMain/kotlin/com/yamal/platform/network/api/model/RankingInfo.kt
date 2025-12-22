package com.yamal.platform.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class RankingInfo(
    @SerialName("rank") val rank: Int,
    @SerialName("previous_rank") val previousRank: Int? = null,
)
