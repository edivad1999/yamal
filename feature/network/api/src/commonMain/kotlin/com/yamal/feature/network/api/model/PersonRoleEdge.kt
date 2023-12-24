package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonRoleEdge(
    val node: PersonBase,
    val role: String
)
