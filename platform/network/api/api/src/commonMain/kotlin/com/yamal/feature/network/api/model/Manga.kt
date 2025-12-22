package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Manga(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("main_picture") val mainPictureNetwork: PictureNetwork?,
    @SerialName("alternative_titles") val alternativeTitlesNetwork: AlternativeTitlesNetwork?,
    @SerialName("start_date") val startDate: String?,
    @SerialName("end_date") val endDate: String?,
    @SerialName("synopsis") val synopsis: String?,
    @SerialName("mean") val mean: Float?,
    @SerialName("rank") val rank: Int?,
    @SerialName("popularity") val popularity: Int?,
    @SerialName("num_list_users") val numListUsers: Int,
    @SerialName("num_scoring_users") val numScoringUsers: Int,
    @SerialName("nsfw") val nsfw: String?,
    @SerialName("genres") val genreNetworks: List<GenreNetwork>,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("media_type") val mediaType: String,
    @SerialName("status") val status: String,
    @SerialName("my_list_status") val myListStatusNetwork: MyListStatusNetwork?,
    @SerialName("num_volumes") val numVolumes: Int,
    @SerialName("num_chapters") val numChapters: Int,
    @SerialName("authors") val authors: PersonRoleEdge,
)
