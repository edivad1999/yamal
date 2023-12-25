package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class PagingData<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("paging") val paging: Paging,
)

@Serializable data class Paging(
    val previous: String? = null,
    val next: String? = null,
)

typealias AnimeList = PagingData<RelatedAnimeEdge>

typealias AnimeRanking = PagingData<RankedAnime>

typealias SeasonalAnime = PagingData<Anime>

typealias SuggestedAnime = PagingData<Anime>
