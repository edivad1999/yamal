package com.yamal.feature.anime.api.model

data class AnimeReviewYamal(
    val id: Int,
    val url: String,
    val score: Int,
    val review: String,
    val date: String,
    val isSpoiler: Boolean,
    val isPreliminary: Boolean,
    val episodesWatched: Int?,
    val tags: List<String>,
    val reactions: ReviewReactionsYamal,
    val user: ReviewUserYamal,
)

data class ReviewReactionsYamal(
    val overall: Int,
    val nice: Int,
    val loveIt: Int,
    val funny: Int,
    val confusing: Int,
    val informative: Int,
    val wellWritten: Int,
    val creative: Int,
)

data class ReviewUserYamal(
    val username: String,
    val url: String,
    val imageUrl: String?,
)
