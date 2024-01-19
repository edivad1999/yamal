package com.yamal.feature.anime.api.model

data class GenericManga(
    val id: Int,
    val title: String,
    val mainPicture: Picture?,
    val rank: Int?,
    val members: Int?,
    val mean: Float?,
    val mediaType: MediaType,
    val userVote: Int?,
    val startDate: String?,
    val endDate: String?,
    val numberOfVolumes: Int?,
    val numberOfChapters: Int?,
)
