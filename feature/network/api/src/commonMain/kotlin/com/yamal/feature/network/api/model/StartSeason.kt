package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class StartSeason(
    val year: Int,
    val season: String
)
