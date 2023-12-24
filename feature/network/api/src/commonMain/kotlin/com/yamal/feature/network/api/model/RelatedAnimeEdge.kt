package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RelatedAnimeEdge(
    val node: Anime,
    val relation_type: String?,
    val relation_type_formatted: String?
)

@Serializable
data class RelatedMangaEdge(
    val node: Manga,
    val relation_type: String?,
    val relation_type_formatted: String?
)

@Serializable
data class Manga(
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
    val num_volumes: Int,
    val num_chapters: Int,
    val authors: PersonRoleEdge
) {

}
