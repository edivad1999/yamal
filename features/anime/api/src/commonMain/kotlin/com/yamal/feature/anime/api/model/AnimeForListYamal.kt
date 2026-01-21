package com.yamal.feature.anime.api.model

data class AnimeForListYamal(
    val id: Int,
    val title: String,
    val mainPicture: PictureYamal?,
    val rank: Int?,
    val members: Int?,
    val mean: Float?,
    val mediaType: MediaTypeYamal,
    val userVote: Int?,
    val startDate: String?,
    val endDate: String?,
    val numberOfEpisodes: Int?,
    val broadcast: BroadcastYamal? = null,
)
