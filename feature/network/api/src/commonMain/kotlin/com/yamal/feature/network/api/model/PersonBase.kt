package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonBase(
    val id: Int,
    val first_name: String,
    val last_name: String
)
