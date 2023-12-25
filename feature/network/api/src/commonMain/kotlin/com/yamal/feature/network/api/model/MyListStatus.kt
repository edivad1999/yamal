package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class MyListStatus(
    @SerialName("status") val status: String?,
    @SerialName("score") val score: Int,
    @SerialName("num_episode_watched") val numEpisodeWatched: Int,
    @SerialName("is_rewatching") val isRewatching: Boolean,
    @SerialName("start_date") val startDate: String?,
    @SerialName("finish_date") val finishDate: String?,
    @SerialName("priority") val priority: Int,
    @SerialName("num_times_rewatched") val numTimesRewatched: Int,
    @SerialName("rewatch_value") val rewatchValue: Int,
    @SerialName("tags") val tags: List<String>,
    @SerialName("comments") val comments: String,
    @SerialName("updated_at") val updatedAt: String,
)
