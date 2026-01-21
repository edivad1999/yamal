package com.yamal.platform.network.api.model.edge

import com.yamal.platform.network.api.model.anime.AnimeForListMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a related anime edge from the MAL API.
 * Corresponds to the RelatedAnimeEdge schema in the API specification.
 */
@Serializable
data class RelatedAnimeEdgeMalNetwork(
    @SerialName("node") val node: AnimeForListMalNetwork,
    @SerialName("relation_type") val relationType: String,
    @SerialName("relation_type_formatted") val relationTypeFormatted: String? = null,
)
