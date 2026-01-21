package com.yamal.platform.network.api.model.manga

import com.yamal.platform.network.api.model.base.WorkForListMalNetwork
import com.yamal.platform.network.api.model.common.AlternativeTitlesMalNetwork
import com.yamal.platform.network.api.model.common.GenreMalNetwork
import com.yamal.platform.network.api.model.common.PersonRoleEdgeMalNetwork
import com.yamal.platform.network.api.model.common.PictureMalNetwork
import com.yamal.platform.network.api.model.edge.MangaMagazineRelationEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.MangaRecommendationAggregationEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.RelatedAnimeEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.RelatedMangaEdgeMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a manga with full details from the MAL API.
 * Corresponds to the MangaForDetails schema in the API specification.
 * Implements WorkForListMalNetwork sealed interface with additional detail-level fields.
 */
@Serializable
data class MangaForDetailsMalNetwork(
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
    // Manga-specific fields
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("my_list_status") val myListStatus: MangaListStatusMalNetwork? = null,
    @SerialName("num_volumes") val numVolumes: Int? = null,
    @SerialName("num_chapters") val numChapters: Int? = null,
    @SerialName("authors") val authors: List<PersonRoleEdgeMalNetwork> = emptyList(),
    // Detail-specific fields
    @SerialName("pictures") val pictures: List<PictureMalNetwork> = emptyList(),
    @SerialName("background") val background: String? = null,
    @SerialName("related_anime") val relatedAnime: List<RelatedAnimeEdgeMalNetwork> = emptyList(),
    @SerialName("related_manga") val relatedManga: List<RelatedMangaEdgeMalNetwork> = emptyList(),
    @SerialName("recommendations") val recommendations: List<MangaRecommendationAggregationEdgeMalNetwork> = emptyList(),
    @SerialName("serialization") val serialization: List<MangaMagazineRelationEdgeMalNetwork> = emptyList(),
) : WorkForListMalNetwork
