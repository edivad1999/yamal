package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SuggestedAnime(
    val daya: List<Anime>,
    val paging: Paging
)
