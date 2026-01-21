package com.yamal.feature.anime.api.model

data class MangaForListYamal(
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
    val numberOfVolumes: Int?,
    val numberOfChapters: Int?,
)
