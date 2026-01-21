package com.yamal.platform.jikan.api.model.anime

import com.yamal.platform.jikan.api.model.common.ImagesJikanNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeReviewJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("type") val type: String,
    @SerialName("reactions") val reactions: ReviewReactionsJikanNetwork,
    @SerialName("date") val date: String,
    @SerialName("review") val review: String,
    @SerialName("score") val score: Int,
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("is_spoiler") val isSpoiler: Boolean = false,
    @SerialName("is_preliminary") val isPreliminary: Boolean = false,
    @SerialName("episodes_watched") val episodesWatched: Int? = null,
    @SerialName("user") val user: ReviewUserJikanNetwork,
)

@Serializable
data class ReviewReactionsJikanNetwork(
    @SerialName("overall") val overall: Int = 0,
    @SerialName("nice") val nice: Int = 0,
    @SerialName("love_it") val loveIt: Int = 0,
    @SerialName("funny") val funny: Int = 0,
    @SerialName("confusing") val confusing: Int = 0,
    @SerialName("informative") val informative: Int = 0,
    @SerialName("well_written") val wellWritten: Int = 0,
    @SerialName("creative") val creative: Int = 0,
)

@Serializable
data class ReviewUserJikanNetwork(
    @SerialName("username") val username: String,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImagesJikanNetwork? = null,
)
