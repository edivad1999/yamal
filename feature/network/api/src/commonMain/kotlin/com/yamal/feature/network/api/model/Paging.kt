package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Paging(
    val previous: String? = null,
    val next: String? = null
)
