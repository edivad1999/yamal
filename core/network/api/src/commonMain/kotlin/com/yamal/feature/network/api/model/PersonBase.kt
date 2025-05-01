package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class PersonBase(
    @SerialName("id") val id: Int,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
)
