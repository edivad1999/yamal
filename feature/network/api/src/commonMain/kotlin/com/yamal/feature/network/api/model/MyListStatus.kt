package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class MyListStatus(
    val status: String?,
    val score: Int,
    val num_episode_watched: Int,
    val is_rewatching: Boolean,
    val start_date: String?,
    val finish_date: String?,
    val priority: Int,
    val num_times_rewatched: Int,
    val rewatch_value: Int,
    val tags: List<String>,
    val comments: String,
    val updated_at: String
)
