package com.yamal.platform.network.api.model.anime

import com.yamal.platform.network.api.model.base.WorkForListMalNetwork
import com.yamal.platform.network.api.model.common.AlternativeTitlesMalNetwork
import com.yamal.platform.network.api.model.common.GenreMalNetwork
import com.yamal.platform.network.api.model.common.PictureMalNetwork
import com.yamal.platform.network.api.model.edge.AnimeRecommendationAggregationEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.RelatedAnimeEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.RelatedMangaEdgeMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an anime with detailed fields from the MAL API.
 * Corresponds to the AnimeForDetails schema in the API specification.
 * Extends AnimeForList with additional detail-only fields like pictures, background,
 * related works, recommendations, and statistics.
 * Implements WorkForListMalNetwork sealed interface.
 */
@Serializable
data class AnimeForDetailsMalNetwork(
    // WorkForListMalNetwork fields
    @SerialName("id") override val id: Int,
    @SerialName("title") override val title: String,
    @SerialName("main_picture") override val mainPicture: PictureMalNetwork? = null,
    @SerialName("alternative_titles") override val alternativeTitles: AlternativeTitlesMalNetwork? = null,
    @SerialName("start_date") override val startDate: String? = null,
    @SerialName("end_date") override val endDate: String? = null,
    @SerialName("synopsis") override val synopsis: String? = null,
    @SerialName("mean") override val mean: Float? = null,
    @SerialName("rank") override val rank: Int? = null,
    @SerialName("popularity") override val popularity: Int? = null,
    @SerialName("num_list_users") override val numListUsers: Int? = null,
    @SerialName("num_scoring_users") override val numScoringUsers: Int? = null,
    @SerialName("nsfw") override val nsfw: String? = null,
    @SerialName("genres") override val genres: List<GenreMalNetwork> = emptyList(),
    @SerialName("created_at") override val createdAt: String? = null,
    @SerialName("updated_at") override val updatedAt: String? = null,
    // Anime-specific fields from AnimeForListMalNetwork
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("my_list_status") val myListStatus: AnimeListStatusMalNetwork? = null,
    @SerialName("num_episodes") val numEpisodes: Int? = null,
    @SerialName("start_season") val startSeason: AnimeSeasonMalNetwork? = null,
    @SerialName("broadcast") val broadcast: BroadcastMalNetwork? = null,
    @SerialName("source") val source: String? = null,
    @SerialName("average_episode_duration") val averageEpisodeDuration: Int? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("studios") val studios: List<AnimeStudioMalNetwork> = emptyList(),
    // Detail-only fields
    @SerialName("pictures") val pictures: List<PictureMalNetwork> = emptyList(),
    @SerialName("background") val background: String? = null,
    @SerialName("related_anime") val relatedAnime: List<RelatedAnimeEdgeMalNetwork> = emptyList(),
    @SerialName("related_manga") val relatedManga: List<RelatedMangaEdgeMalNetwork> = emptyList(),
    @SerialName("recommendations") val recommendations: List<AnimeRecommendationAggregationEdgeMalNetwork> = emptyList(),
    @SerialName("statistics") val statistics: AnimeStatisticsMalNetwork? = null,
) : WorkForListMalNetwork
