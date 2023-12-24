package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Broadcast(
    val day_of_the_week: String,
    val start_time: String?
)
