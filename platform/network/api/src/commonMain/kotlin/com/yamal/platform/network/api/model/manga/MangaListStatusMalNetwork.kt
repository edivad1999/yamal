package com.yamal.platform.network.api.model.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user's list status for a manga from the MAL API.
 * Corresponds to the MangaListStatus schema in the API specification.
 */
@Serializable
data class MangaListStatusMalNetwork(
    @SerialName("status") val status: String? = null,
    @SerialName("score") val score: Int? = null,
    @SerialName("num_volumes_read") val numVolumesRead: Int? = null,
    @SerialName("num_chapters_read") val numChaptersRead: Int? = null,
    @SerialName("is_rereading") val isRereading: Boolean? = null,
    @SerialName("start_date") val startDate: String? = null,
    @SerialName("finish_date") val finishDate: String? = null,
    @SerialName("priority") val priority: Int? = null,
    @SerialName("num_times_reread") val numTimesReread: Int? = null,
    @SerialName("reread_value") val rereadValue: Int? = null,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("comments") val comments: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
)
