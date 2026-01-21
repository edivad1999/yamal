package com.yamal.platform.network.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a person (author, etc.) from the MAL API.
 * Corresponds to the PersonBase schema in the API specification.
 */
@Serializable
data class PersonBaseMalNetwork(
    @SerialName("id") val id: Int,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
)
