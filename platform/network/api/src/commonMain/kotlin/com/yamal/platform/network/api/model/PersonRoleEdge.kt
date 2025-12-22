package com.yamal.platform.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class PersonRoleEdge(
    @SerialName("node") val node: PersonBase,
    @SerialName("role") val role: String,
)
