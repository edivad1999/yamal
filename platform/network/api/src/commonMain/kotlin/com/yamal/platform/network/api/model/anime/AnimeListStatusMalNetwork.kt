package com.yamal.platform.network.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user's list status for an anime from the MAL API.
 * Corresponds to the AnimeListStatus schema in the API specification.
 */
@Serializable
data class AnimeListStatusMalNetwork(
    @SerialName("status") val status: String? = null,
    @SerialName("score") val score: Int? = null,
    @SerialName("num_episodes_watched") val numEpisodesWatched: Int? = null,
    @SerialName("is_rewatching") val isRewatching: Boolean? = null,
    @SerialName("start_date") val startDate: String? = null,
    @SerialName("finish_date") val finishDate: String? = null,
    @SerialName("priority") val priority: Int? = null,
    @SerialName("num_times_rewatched") val numTimesRewatched: Int? = null,
    @SerialName("rewatch_value") val rewatchValue: Int? = null,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("comments") val comments: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
)
