package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeStudio(
    val id: Int,
    val name: String
)
