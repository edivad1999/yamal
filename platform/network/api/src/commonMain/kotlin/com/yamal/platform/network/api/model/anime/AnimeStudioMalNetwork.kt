package com.yamal.platform.network.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a studio from the MAL API.
 * Corresponds to the AnimeStudio schema in the API specification.
 */
@Serializable
data class AnimeStudioMalNetwork(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)
