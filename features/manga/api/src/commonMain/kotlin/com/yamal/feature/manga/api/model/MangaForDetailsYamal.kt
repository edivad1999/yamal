package com.yamal.feature.manga.api.model

data class MangaForDetailsYamal(
    val id: Int,
    val title: String,
    val mainPicture: PictureYamal?,
    val alternativeTitles: AlternativeTitlesYamal?,
    val startDate: String?,
    val endDate: String?,
    val synopsis: String?,
    val mean: Float?,
    val rank: Int?,
    val popularity: Int?,
    val numListUsers: Int,
    val numScoringUsers: Int,
    val nsfw: String?,
    val genres: List<GenreYamal>,
    val createdAt: String,
    val updatedAt: String,
    val mediaType: MangaMediaTypeYamal,
    val status: String,
    val myListStatus: MangaListStatusYamal?,
    val numVolumes: Int,
    val numChapters: Int,
    val authors: List<AuthorYamal>,
    val pictures: List<PictureYamal>,
    val background: String?,
    val relatedAnime: List<RelatedItemYamal>,
    val relatedManga: List<RelatedItemYamal>,
    val recommendations: List<MangaRecommendationYamal>,
    val serialization: List<MagazineYamal>,
)

data class PictureYamal(
    val large: String?,
    val medium: String?,
)

data class AlternativeTitlesYamal(
    val synonyms: List<String>,
    val en: String?,
    val ja: String?,
)

data class GenreYamal(
    val id: Int,
    val name: String,
)

data class AuthorYamal(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
)

data class MagazineYamal(
    val id: Int,
    val name: String,
)

data class RelatedItemYamal(
    val id: Int,
    val title: String,
    val mainPicture: PictureYamal?,
    val relationType: String,
    val relationTypeFormatted: String,
)

data class MangaRecommendationYamal(
    val id: Int,
    val title: String,
    val mainPicture: PictureYamal?,
    val numRecommendations: Int,
)
