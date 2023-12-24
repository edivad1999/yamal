package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeRanking(
    val data: List<Data>,
    val paging: Paging
)
