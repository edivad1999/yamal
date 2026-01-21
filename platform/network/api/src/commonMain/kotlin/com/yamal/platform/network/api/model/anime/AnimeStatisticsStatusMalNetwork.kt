package com.yamal.platform.network.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the status breakdown in anime statistics from the MAL API.
 * Part of the AnimeStatistics schema in the API specification.
 */
@Serializable
data class AnimeStatisticsStatusMalNetwork(
    @SerialName("watching") val watching: Int,
    @SerialName("completed") val completed: Int,
    @SerialName("on_hold") val onHold: Int,
    @SerialName("dropped") val dropped: Int,
    @SerialName("plan_to_watch") val planToWatch: Int,
)
