package com.yamal.feature.user.api.model

data class UserProfileYamal(
    val id: Int,
    val name: String,
    val picture: String?,
    val gender: String?,
    val birthday: String?,
    val location: String?,
    val joinedAt: String?,
    val animeStatistics: UserAnimeStatisticsYamal?,
)

data class UserAnimeStatisticsYamal(
    val numItemsWatching: Int,
    val numItemsCompleted: Int,
    val numItemsOnHold: Int,
    val numItemsDropped: Int,
    val numItemsPlanToWatch: Int,
    val numItems: Int,
    val numDaysWatched: Double,
    val numDaysWatching: Double,
    val numDaysCompleted: Double,
    val numDaysOnHold: Double,
    val numDaysDropped: Double,
    val numDays: Double,
    val numEpisodes: Int,
    val numTimesRewatched: Int,
    val meanScore: Double,
)
