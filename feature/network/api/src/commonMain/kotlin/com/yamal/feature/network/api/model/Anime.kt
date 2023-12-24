package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    val id: Int,
    val title: String,
    val main_picture: Picture?,
    val alternative_titles: AlternativeTitles?,
    val start_date: String?,
    val end_date: String?,
    val synopsis: String?,
    val mean: Float?,
    val rank: Int?,
    val popularity: Int?,
    val num_list_users: Int,
    val num_scoring_users: Int,
    val nsfw: String?,
    val genres: List<Genre>,
    val created_at: String,
    val updated_at: String,
    val media_type: String,
    val status: String,
    val my_list_status: MyListStatus?,
    val num_episodes: Int,
    val start_season: StartSeason?,
    val broadcast: Broadcast?,
    val source: String?,
    val average_episode_duration: Int?,
    val rating: String?,
    val studios: List<AnimeStudio>
)
