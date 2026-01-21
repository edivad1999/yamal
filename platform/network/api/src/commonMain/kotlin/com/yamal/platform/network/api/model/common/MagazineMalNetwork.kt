package com.yamal.platform.network.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a magazine from the MAL API.
 * Corresponds to the Magazine schema in the API specification.
 */
@Serializable
data class MagazineMalNetwork(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)
