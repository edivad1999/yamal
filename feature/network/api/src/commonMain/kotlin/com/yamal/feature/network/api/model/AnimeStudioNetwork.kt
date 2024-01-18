package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class AnimeStudioNetwork(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)
