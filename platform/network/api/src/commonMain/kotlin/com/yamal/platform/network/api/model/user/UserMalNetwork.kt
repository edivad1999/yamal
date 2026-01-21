package com.yamal.platform.network.api.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user profile from the MAL API.
 * Corresponds to the User schema in the API specification.
 */
@Serializable
data class UserMalNetwork(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("location") val location: String? = null,
    @SerialName("joined_at") val joinedAt: String? = null,
    @SerialName("picture") val picture: String? = null,
    @SerialName("anime_statistics") val animeStatistics: UserAnimeStatisticsMalNetwork? = null,
)
