package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeList(
    val data: List<RelatedAnimeEdge>,
    val paging: Paging
)
