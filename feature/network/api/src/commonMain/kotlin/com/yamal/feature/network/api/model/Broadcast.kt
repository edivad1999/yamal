package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class Broadcast(
    @SerialName("day_of_the_week") val dayOfTheWeek: String,
    @SerialName("start_time") val startTime: String?,
)
