package com.yamal.feature.anime.api.model

import com.yamal.feature.network.api.model.Picture

data class AnimeRanking(
    val id: Int,
    val title: String,
    val mainPicture: AnimeMainPicture?,
    val rank: Int,
)

//TODO move to impl module
fun Picture.toModel() = AnimeMainPicture(large = large, medium = medium)


data class AnimeMainPicture(
    val large: String?,
    val medium: String
)
