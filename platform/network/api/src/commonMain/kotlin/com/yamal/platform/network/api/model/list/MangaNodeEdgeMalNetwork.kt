package com.yamal.platform.network.api.model.list

import com.yamal.platform.network.api.model.manga.MangaForListMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a wrapper for a manga node in list responses from the MAL API.
 * Used for search results and other list responses.
 */
@Serializable
data class MangaNodeEdgeMalNetwork(
    @SerialName("node") val node: MangaForListMalNetwork,
)
