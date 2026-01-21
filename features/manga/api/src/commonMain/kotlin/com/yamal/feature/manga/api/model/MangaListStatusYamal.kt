package com.yamal.feature.manga.api.model

data class MangaListStatusYamal(
    val status: String? = null,
    val score: Int? = null,
    val numVolumesRead: Int? = null,
    val numChaptersRead: Int? = null,
    val isRereading: Boolean? = null,
    val startDate: String? = null,
    val finishDate: String? = null,
    val priority: Int? = null,
    val numTimesReread: Int? = null,
    val rereadValue: Int? = null,
    val tags: List<String>? = null,
    val comments: String? = null,
    val updatedAt: String? = null,
)
