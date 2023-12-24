package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Picture(
    val large: String?,
    val medium: String
)
