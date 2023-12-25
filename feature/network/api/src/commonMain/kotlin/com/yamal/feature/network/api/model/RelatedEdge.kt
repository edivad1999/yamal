package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RelatedAnimeEdge(
    val node: Anime,
    val relation_type: String?,
    val relation_type_formatted: String?,
)

@Serializable
data class RelatedMangaEdge(
    val node: Manga,
    val relation_type: String?,
    val relation_type_formatted: String?,
)

