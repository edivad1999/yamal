package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeRecommendationAggregationEdgeBase(
    val node: Anime,
    val num_recommendations: Int
)