package com.yamal.feature.anime.api.model

data class AnimeRanking(
    val id: Int,
    val title: String,
    val mainPicture: AnimeMainPicture?,
    val rank: Int,
)

data class AnimeMainPicture(
    val large: String?,
    val medium: String,
)
