package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("main_picture") val mainPicture: Picture?,
    @SerialName("alternative_titles") val alternativeTitles: AlternativeTitles? = null,
    @SerialName("start_date") val startDate: String? = null,
    @SerialName("end_date") val endDate: String? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("mean") val mean: Float? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("num_list_users") val numListUsers: Int? = null,
    @SerialName("num_scoring_users") val numScoringUsers: Int? = null,
    @SerialName("nsfw") val nsfw: String? = null,
    @SerialName("genres") val genres: List<Genre> = emptyList(),
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("my_list_status") val myListStatus: MyListStatus? = null,
    @SerialName("num_episodes") val numEpisodes: Int? = null,
    @SerialName("start_season") val startSeason: StartSeason? = null,
    @SerialName("broadcast") val broadcast: Broadcast? = null,
    @SerialName("source") val source: String? = null,
    @SerialName("average_episode_duration") val averageEpisodeDuration: Int? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("studios") val studios: List<AnimeStudio> = emptyList(),
)
