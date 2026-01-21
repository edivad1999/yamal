package com.yamal.platform.network.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a picture/image from the MAL API.
 * Corresponds to the Picture schema in the API specification.
 */
@Serializable
data class PictureMalNetwork(
    @SerialName("large") val large: String? = null,
    @SerialName("medium") val medium: String,
)
