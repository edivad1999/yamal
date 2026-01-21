package com.yamal.platform.network.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents statistics for an anime from the MAL API.
 * Corresponds to the AnimeStatistics schema in the API specification.
 */
@Serializable
data class AnimeStatisticsMalNetwork(
    @SerialName("num_list_users") val numListUsers: Int,
    @SerialName("status") val status: AnimeStatisticsStatusMalNetwork,
)
