package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class AnimeRecommendationAggregationEdgeBase(
    @SerialName("node") val node: Anime,
    @SerialName("num_recommendations") val numRecommendations: Int,
)
