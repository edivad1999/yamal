package com.yamal.platform.network.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents ranking information from the MAL API.
 * Corresponds to the RankingInfo schema in the API specification.
 */
@Serializable
data class RankingInfoMalNetwork(
    @SerialName("rank") val rank: Int,
    @SerialName("previous_rank") val previousRank: Int? = null,
)
