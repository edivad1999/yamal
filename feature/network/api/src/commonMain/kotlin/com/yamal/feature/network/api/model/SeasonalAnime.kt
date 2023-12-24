package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SeasonalAnime(
    val data: List<Anime>,
    val paging: Paging
)
