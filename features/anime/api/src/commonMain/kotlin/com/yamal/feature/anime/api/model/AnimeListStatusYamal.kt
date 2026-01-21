package com.yamal.feature.anime.api.model

data class AnimeListStatusYamal(
    val status: String? = null,
    val score: Int? = null,
    val numEpisodesWatched: Int? = null,
    val isRewatching: Boolean? = null,
    val startDate: String? = null,
    val finishDate: String? = null,
    val priority: Int? = null,
    val numTimesRewatched: Int? = null,
    val rewatchValue: Int? = null,
    val tags: List<String>? = null,
    val comments: String? = null,
    val updatedAt: String? = null,
)
