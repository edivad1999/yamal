package com.yamal.platform.network.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a person with their role from the MAL API.
 * Corresponds to the PersonRoleEdge schema in the API specification.
 */
@Serializable
data class PersonRoleEdgeMalNetwork(
    @SerialName("node") val node: PersonBaseMalNetwork,
    @SerialName("role") val role: String,
)
