package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class AnimeDetails(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("main_picture") val mainPicture: Picture?,
    @SerialName("alternative_titles") val alternativeTitles: AlternativeTitles?,
    @SerialName("start_date") val startDate: String?,
    @SerialName("end_date") val endDate: String?,
    @SerialName("synopsis") val synopsis: String?,
    @SerialName("mean") val mean: Float?,
    @SerialName("rank") val rank: Int?,
    @SerialName("popularity") val popularity: Int?,
    @SerialName("num_list_users") val numListUsers: Int,
    @SerialName("num_scoring_users") val numScoringUsers: Int,
    @SerialName("nsfw") val nsfw: String?,
    @SerialName("genres") val genres: List<Genre>,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("media_type") val mediaType: String,
    @SerialName("status") val status: String,
    @SerialName("my_list_status") val myListStatus: MyListStatus?,
    @SerialName("num_episodes") val numEpisodes: Int,
    @SerialName("start_season") val startSeason: StartSeason?,
    @SerialName("broadcast") val broadcast: Broadcast?,
    @SerialName("source") val source: String?,
    @SerialName("average_episode_duration") val averageEpisodeDuration: Int?,
    @SerialName("rating") val rating: String?,
    @SerialName("studios") val studios: List<AnimeStudio>,
    @SerialName("pictures") val pictures: List<Picture>,
    @SerialName("background") val background: String?,
    @SerialName("related_anime") val relatedAnime: List<RelatedAnimeEdge>,
    @SerialName("related_manga") val relatedManga: List<RelatedMangaEdge>,
    @SerialName("recommendations") val recommendations: AnimeRecommendationAggregationEdgeBase,
    @SerialName("statistics") val statistics: Statistics?,
)
