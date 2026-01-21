package com.yamal.feature.anime.implementation.mapping

import com.yamal.feature.anime.api.model.AlternativeTitlesYamal
import com.yamal.feature.anime.api.model.AnimeListStatusYamal
import com.yamal.feature.anime.api.model.AnimeSeasonYamal
import com.yamal.feature.anime.api.model.AnimeStatisticsYamal
import com.yamal.feature.anime.api.model.AnimeStudioYamal
import com.yamal.feature.anime.api.model.BroadcastYamal
import com.yamal.feature.anime.api.model.GenreYamal
import com.yamal.feature.anime.api.model.PictureYamal
import com.yamal.feature.anime.api.model.SeasonYamal
import com.yamal.platform.network.api.model.anime.AnimeListStatusMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeSeasonMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeStatisticsMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeStudioMalNetwork
import com.yamal.platform.network.api.model.anime.BroadcastMalNetwork
import com.yamal.platform.network.api.model.common.AlternativeTitlesMalNetwork
import com.yamal.platform.network.api.model.common.GenreMalNetwork
import com.yamal.platform.network.api.model.common.PictureMalNetwork

/**
 * Maps a network picture model to the domain picture model.
 */
fun PictureMalNetwork.toYamal() =
    PictureYamal(
        large = large,
        medium = medium,
    )

/**
 * Maps a network genre model to the domain genre model.
 */
fun GenreMalNetwork.toYamal() =
    GenreYamal(
        id = id,
        name = name,
    )

/**
 * Maps network alternative titles to the domain alternative titles model.
 */
fun AlternativeTitlesMalNetwork.toYamal() =
    AlternativeTitlesYamal(
        synonyms = synonyms ?: emptyList(),
        en = en,
        ja = ja,
    )

/**
 * Maps a network broadcast model to the domain broadcast model.
 */
fun BroadcastMalNetwork.toYamal() =
    BroadcastYamal(
        dayOfTheWeek = dayOfTheWeek,
        startTime = startTime,
    )

/**
 * Maps a network anime studio model to the domain anime studio model.
 */
fun AnimeStudioMalNetwork.toYamal() =
    AnimeStudioYamal(
        id = id,
        name = name,
    )

/**
 * Maps a network anime season model to the domain anime season model.
 */
fun AnimeSeasonMalNetwork.toYamal() =
    AnimeSeasonYamal(
        year = year.toString(),
        season = SeasonYamal.fromSerializedValue(season),
    )

/**
 * Maps a network anime list status model to the domain anime list status model.
 */
fun AnimeListStatusMalNetwork.toYamal() =
    AnimeListStatusYamal(
        status = status,
        score = score,
        numEpisodesWatched = numEpisodesWatched,
        isRewatching = isRewatching,
        startDate = startDate,
        finishDate = finishDate,
        priority = priority,
        numTimesRewatched = numTimesRewatched,
        rewatchValue = rewatchValue,
        tags = tags,
        comments = comments,
        updatedAt = updatedAt,
    )

/**
 * Maps a network anime statistics model to the domain anime statistics model.
 */
fun AnimeStatisticsMalNetwork.toYamal() =
    AnimeStatisticsYamal(
        numListUsers = numListUsers,
        watching = status.watching,
        completed = status.completed,
        onHold = status.onHold,
        dropped = status.dropped,
        planToWatch = status.planToWatch,
    )
