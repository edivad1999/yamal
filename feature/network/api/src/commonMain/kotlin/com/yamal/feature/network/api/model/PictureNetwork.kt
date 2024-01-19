package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class PictureNetwork(
    @SerialName("large") val large: String?,
    @SerialName("medium") val medium: String,
)
