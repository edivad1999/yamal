package com.yamal.platform.network.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a season for an anime from the MAL API.
 * Corresponds to the AnimeSeason schema in the API specification.
 */
@Serializable
data class AnimeSeasonMalNetwork(
    @SerialName("year") val year: Int,
    @SerialName("season") val season: String,
)
