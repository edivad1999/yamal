package com.yamal.platform.network.api.model.edge

import com.yamal.platform.network.api.model.anime.AnimeForListMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an anime recommendation aggregation edge from the MAL API.
 * Corresponds to the AnimeRecommendationAggregationEdgeBase schema in the API specification.
 */
@Serializable
data class AnimeRecommendationAggregationEdgeMalNetwork(
    @SerialName("node") val node: AnimeForListMalNetwork,
    @SerialName("num_recommendations") val numRecommendations: Int,
)
