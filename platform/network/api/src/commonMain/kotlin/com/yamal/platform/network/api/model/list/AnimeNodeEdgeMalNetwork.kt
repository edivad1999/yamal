package com.yamal.platform.network.api.model.list

import com.yamal.platform.network.api.model.anime.AnimeForListMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a wrapper for an anime node in list responses from the MAL API.
 * Corresponds to the AnimeNodeEdge schema in the API specification.
 */
@Serializable
data class AnimeNodeEdgeMalNetwork(
    @SerialName("node") val node: AnimeForListMalNetwork,
)
