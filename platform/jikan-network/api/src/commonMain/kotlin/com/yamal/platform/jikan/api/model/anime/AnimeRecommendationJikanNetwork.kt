package com.yamal.platform.jikan.api.model.anime

import com.yamal.platform.jikan.api.model.common.ImagesJikanNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeRecommendationJikanNetwork(
    @SerialName("entry") val entry: RecommendationEntryJikanNetwork,
    @SerialName("url") val url: String,
    @SerialName("votes") val votes: Int,
)

@Serializable
data class RecommendationEntryJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImagesJikanNetwork? = null,
    @SerialName("title") val title: String,
)
