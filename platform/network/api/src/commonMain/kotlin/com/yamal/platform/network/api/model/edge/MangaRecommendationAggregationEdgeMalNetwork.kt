package com.yamal.platform.network.api.model.edge

import com.yamal.platform.network.api.model.manga.MangaForListMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a manga recommendation aggregation edge from the MAL API.
 * Corresponds to the MangaRecommendationAggregationEdgeBase schema in the API specification.
 */
@Serializable
data class MangaRecommendationAggregationEdgeMalNetwork(
    @SerialName("node") val node: MangaForListMalNetwork,
    @SerialName("num_recommendations") val numRecommendations: Int,
)
