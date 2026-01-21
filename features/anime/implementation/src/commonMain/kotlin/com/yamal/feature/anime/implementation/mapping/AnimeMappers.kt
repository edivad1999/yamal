package com.yamal.feature.anime.implementation.mapping

import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeRecommendationYamal
import com.yamal.feature.anime.api.model.MediaTypeYamal
import com.yamal.feature.anime.api.model.RelatedItemYamal
import com.yamal.feature.anime.api.model.RelationTypeYamal
import com.yamal.feature.anime.api.model.RelationYamal
import com.yamal.platform.network.api.model.anime.AnimeForDetailsMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeForListMalNetwork
import com.yamal.platform.network.api.model.edge.AnimeRecommendationAggregationEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.RelatedAnimeEdgeMalNetwork

/**
 * Maps a network anime for list model to the domain anime for list model.
 */
fun AnimeForListMalNetwork.toYamal() =
    AnimeForListYamal(
        id = id,
        title = title,
        mainPicture = mainPicture?.toYamal(),
        rank = rank,
        members = numListUsers,
        mean = mean,
        mediaType = MediaTypeYamal.fromSerializedValue(mediaType),
        userVote = myListStatus?.score,
        startDate = startDate,
        endDate = endDate,
        numberOfEpisodes = numEpisodes,
        broadcast = broadcast?.toYamal(),
    )

/**
 * Maps a network anime recommendation aggregation edge model to the domain anime recommendation model.
 */
fun AnimeRecommendationAggregationEdgeMalNetwork.toYamal() =
    AnimeRecommendationYamal(
        node = node.toYamal(),
        numRecommendations = numRecommendations,
    )

/**
 * Maps a network related anime edge model to the domain related item model.
 */
fun RelatedAnimeEdgeMalNetwork.toYamal() =
    RelatedItemYamal(
        node = node.toYamal(),
        relation =
            RelationYamal(
                type = RelationTypeYamal.fromSerializedValue(relationType),
                formatted = relationTypeFormatted ?: "",
            ),
    )

/**
 * Maps a network anime for details model to the domain anime for details model.
 */
fun AnimeForDetailsMalNetwork.toYamal() =
    AnimeForDetailsYamal(
        id = id,
        title = title,
        mainPicture = mainPicture?.toYamal(),
        alternativeTitles = alternativeTitles?.toYamal(),
        startDate = startDate,
        endDate = endDate,
        synopsis = synopsis,
        mean = mean,
        rank = rank,
        popularity = popularity,
        numListUsers = numListUsers ?: 0,
        numScoringUsers = numScoringUsers ?: 0,
        nsfw = nsfw,
        genres = genres.map { it.toYamal() },
        createdAt = createdAt ?: "",
        updatedAt = updatedAt ?: "",
        mediaType = MediaTypeYamal.fromSerializedValue(mediaType),
        status = status ?: "",
        myListStatus = myListStatus?.toYamal(),
        numEpisodes = numEpisodes ?: 0,
        startSeason = startSeason?.toYamal(),
        broadcast = broadcast?.toYamal(),
        source = source,
        averageEpisodeDuration = averageEpisodeDuration,
        rating = rating,
        studios = studios.map { it.toYamal() },
        pictures = pictures.map { it.toYamal() },
        background = background,
        relatedAnime = relatedAnime.map { it.toYamal() },
        relatedManga = relatedManga.map { it.toYamal() },
        recommendations = recommendations.map { it.toYamal() },
        statistics = statistics?.toYamal(),
    )
