package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class AnimeDetailsNetwork(
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
    @SerialName("num_episodes") val numEpisodes: Int,
    @SerialName("start_season") val startSeason: StartSeason?,
    @SerialName("broadcast") val broadcastNetwork: BroadcastNetwork?,
    @SerialName("source") val source: String?,
    @SerialName("average_episode_duration") val averageEpisodeDuration: Int?,
    @SerialName("rating") val rating: String?,
    @SerialName("studios") val studios: List<AnimeStudioNetwork>,
    @SerialName("pictures") val pictureNetworks: List<PictureNetwork>,
    @SerialName("background") val background: String?,
    @SerialName("related_anime") val relatedAnime: List<RelatedAnimeEdge>,
    @SerialName("related_manga") val relatedManga: List<RelatedMangaEdge>,
    @SerialName("recommendations") val recommendations: List<AnimeRecommendationAggregationEdgeBase>,
    @SerialName("statistics") val statisticsNetwork: StatisticsNetwork?,
)
