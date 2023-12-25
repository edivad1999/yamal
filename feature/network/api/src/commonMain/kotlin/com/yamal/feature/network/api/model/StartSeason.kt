package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class StartSeason(
    @SerialName("year") val year: Int,
    @SerialName("season") val season: String,
)
