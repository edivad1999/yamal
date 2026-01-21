package com.yamal.platform.network.api.model.edge

import com.yamal.platform.network.api.model.manga.MangaForListMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a related manga edge from the MAL API.
 * Corresponds to the RelatedMangaEdge schema in the API specification.
 */
@Serializable
data class RelatedMangaEdgeMalNetwork(
    @SerialName("node") val node: MangaForListMalNetwork,
    @SerialName("relation_type") val relationType: String,
    @SerialName("relation_type_formatted") val relationTypeFormatted: String? = null,
)
