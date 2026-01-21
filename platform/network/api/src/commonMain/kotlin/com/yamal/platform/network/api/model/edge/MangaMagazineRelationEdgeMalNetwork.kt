package com.yamal.platform.network.api.model.edge

import com.yamal.platform.network.api.model.common.MagazineMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a manga-magazine relation edge from the MAL API.
 * Corresponds to the MangaMagazineRelationEdge schema in the API specification.
 */
@Serializable
data class MangaMagazineRelationEdgeMalNetwork(
    @SerialName("node") val node: MagazineMalNetwork,
    @SerialName("role") val role: String? = null,
)
