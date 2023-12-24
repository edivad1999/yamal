package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    val id: Int,
    val title: String,
    val main_picture: Picture?,
    val alternative_titles: AlternativeTitles? = null,
    val start_date: String? = null,
    val end_date: String? = null,
    val synopsis: String? = null,
    val mean: Float? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val num_list_users: Int? = null,
    val num_scoring_users: Int? = null,
    val nsfw: String? = null,
    val genres: List<Genre> = emptyList(),
    val created_at: String? = null,
    val updated_at: String? = null,
    val media_type: String? = null,
    val status: String? = null,
    val my_list_status: MyListStatus? = null,
    val num_episodes: Int? = null,
    val start_season: StartSeason? = null,
    val broadcast: Broadcast? = null,
    val source: String? = null,
    val average_episode_duration: Int? = null,
    val rating: String? = null,
    val studios: List<AnimeStudio> = emptyList(),
)
