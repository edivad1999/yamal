package com.yamal.feature.user.implementation.mapping

import com.yamal.feature.user.api.model.UserAnimeStatisticsYamal
import com.yamal.feature.user.api.model.UserProfileYamal
import com.yamal.platform.network.api.model.user.UserMalNetwork

fun UserMalNetwork.toYamal() =
    UserProfileYamal(
        id = id,
        name = name,
        picture = picture,
        gender = null, // Not available in MAL API
        birthday = null, // Not available in MAL API
        location = location,
        joinedAt = joinedAt,
        animeStatistics =
            animeStatistics?.let { stats ->
                UserAnimeStatisticsYamal(
                    numItemsWatching = stats.numItemsWatching ?: 0,
                    numItemsCompleted = stats.numItemsCompleted ?: 0,
                    numItemsOnHold = stats.numItemsOnHold ?: 0,
                    numItemsDropped = stats.numItemsDropped ?: 0,
                    numItemsPlanToWatch = stats.numItemsPlanToWatch ?: 0,
                    numItems = stats.numItems ?: 0,
                    numDaysWatched = stats.numDaysWatched?.toDouble() ?: 0.0,
                    numDaysWatching = stats.numDaysWatching?.toDouble() ?: 0.0,
                    numDaysCompleted = stats.numDaysCompleted?.toDouble() ?: 0.0,
                    numDaysOnHold = stats.numDaysOnHold?.toDouble() ?: 0.0,
                    numDaysDropped = stats.numDaysDropped?.toDouble() ?: 0.0,
                    numDays = stats.numDays?.toDouble() ?: 0.0,
                    numEpisodes = stats.numEpisodes ?: 0,
                    numTimesRewatched = stats.numTimesRewatched ?: 0,
                    meanScore = stats.meanScore?.toDouble() ?: 0.0,
                )
            },
    )
