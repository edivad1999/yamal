package com.yamal.platform.network.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a genre from the MAL API.
 * Corresponds to the Genre schema in the API specification.
 */
@Serializable
data class GenreMalNetwork(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)
