package com.yamal.platform.network.api.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user's anime statistics from the MAL API.
 * Corresponds to the UserAnimeStatistics schema in the API specification.
 */
@Serializable
data class UserAnimeStatisticsMalNetwork(
    @SerialName("num_items_watching") val numItemsWatching: Int? = null,
    @SerialName("num_items_completed") val numItemsCompleted: Int? = null,
    @SerialName("num_items_on_hold") val numItemsOnHold: Int? = null,
    @SerialName("num_items_dropped") val numItemsDropped: Int? = null,
    @SerialName("num_items_plan_to_watch") val numItemsPlanToWatch: Int? = null,
    @SerialName("num_items") val numItems: Int? = null,
    @SerialName("num_days_watched") val numDaysWatched: Float? = null,
    @SerialName("num_days_watching") val numDaysWatching: Float? = null,
    @SerialName("num_days_completed") val numDaysCompleted: Float? = null,
    @SerialName("num_days_on_hold") val numDaysOnHold: Float? = null,
    @SerialName("num_days_dropped") val numDaysDropped: Float? = null,
    @SerialName("num_days") val numDays: Float? = null,
    @SerialName("num_episodes") val numEpisodes: Int? = null,
    @SerialName("num_times_rewatched") val numTimesRewatched: Int? = null,
    @SerialName("mean_score") val meanScore: Float? = null,
)
