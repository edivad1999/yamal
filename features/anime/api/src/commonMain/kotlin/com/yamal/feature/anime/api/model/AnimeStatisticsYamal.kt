package com.yamal.feature.anime.api.model

data class AnimeStatisticsYamal(
    val numListUsers: Int,
    val watching: Int,
    val completed: Int,
    val onHold: Int,
    val dropped: Int,
    val planToWatch: Int,
)
