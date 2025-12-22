package com.yamal.platform.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class BroadcastNetwork(
    @SerialName("day_of_the_week") val dayOfTheWeek: String,
    @SerialName("start_time") val startTime: String?,
)
