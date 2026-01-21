package com.yamal.platform.network.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents broadcast information for an anime from the MAL API.
 * Part of the AnimeForList schema in the API specification.
 */
@Serializable
data class BroadcastMalNetwork(
    @SerialName("day_of_the_week") val dayOfTheWeek: String,
    @SerialName("start_time") val startTime: String? = null,
)
