package com.yamal.feature.manga.implementation.mapping

import com.yamal.feature.manga.api.model.AlternativeTitlesYamal
import com.yamal.feature.manga.api.model.AuthorYamal
import com.yamal.feature.manga.api.model.GenreYamal
import com.yamal.feature.manga.api.model.MagazineYamal
import com.yamal.feature.manga.api.model.MangaForDetailsYamal
import com.yamal.feature.manga.api.model.MangaForListYamal
import com.yamal.feature.manga.api.model.MangaListStatusYamal
import com.yamal.feature.manga.api.model.MangaMediaTypeYamal
import com.yamal.feature.manga.api.model.MangaRecommendationYamal
import com.yamal.feature.manga.api.model.PictureYamal
import com.yamal.feature.manga.api.model.RelatedItemYamal
import com.yamal.platform.network.api.model.common.AlternativeTitlesMalNetwork
import com.yamal.platform.network.api.model.common.GenreMalNetwork
import com.yamal.platform.network.api.model.common.PersonRoleEdgeMalNetwork
import com.yamal.platform.network.api.model.common.PictureMalNetwork
import com.yamal.platform.network.api.model.edge.MangaMagazineRelationEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.MangaRecommendationAggregationEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.RelatedAnimeEdgeMalNetwork
import com.yamal.platform.network.api.model.edge.RelatedMangaEdgeMalNetwork
import com.yamal.platform.network.api.model.manga.MangaForDetailsMalNetwork
import com.yamal.platform.network.api.model.manga.MangaForListMalNetwork
import com.yamal.platform.network.api.model.manga.MangaListStatusMalNetwork

fun PictureMalNetwork.toYamal() =
    PictureYamal(
        large = large,
        medium = medium,
    )

fun GenreMalNetwork.toYamal() =
    GenreYamal(
        id = id,
        name = name,
    )

fun AlternativeTitlesMalNetwork.toYamal() =
    AlternativeTitlesYamal(
        synonyms = synonyms ?: emptyList(),
        en = en,
        ja = ja,
    )

fun MangaListStatusMalNetwork.toYamal() =
    MangaListStatusYamal(
        status = status,
        score = score,
        numVolumesRead = numVolumesRead,
        numChaptersRead = numChaptersRead,
        isRereading = isRereading,
        startDate = startDate,
        finishDate = finishDate,
        priority = priority,
        numTimesReread = numTimesReread,
        rereadValue = rereadValue,
        tags = tags,
        comments = comments,
        updatedAt = updatedAt,
    )

fun PersonRoleEdgeMalNetwork.toAuthorYamal() =
    AuthorYamal(
        id = node.id,
        firstName = node.firstName ?: "",
        lastName = node.lastName ?: "",
        role = role ?: "",
    )

fun MangaMagazineRelationEdgeMalNetwork.toYamal() =
    MagazineYamal(
        id = node.id,
        name = node.name,
    )

fun RelatedAnimeEdgeMalNetwork.toYamal() =
    RelatedItemYamal(
        id = node.id,
        title = node.title,
        mainPicture = node.mainPicture?.toYamal(),
        relationType = relationType,
        relationTypeFormatted = relationTypeFormatted ?: "",
    )

fun RelatedMangaEdgeMalNetwork.toYamal() =
    RelatedItemYamal(
        id = node.id,
        title = node.title,
        mainPicture = node.mainPicture?.toYamal(),
        relationType = relationType,
        relationTypeFormatted = relationTypeFormatted ?: "",
    )

fun MangaRecommendationAggregationEdgeMalNetwork.toYamal() =
    MangaRecommendationYamal(
        id = node.id,
        title = node.title,
        mainPicture = node.mainPicture?.toYamal(),
        numRecommendations = numRecommendations,
    )

fun MangaForListMalNetwork.toYamal() =
    MangaForListYamal(
        id = id,
        title = title,
        mainPicture = mainPicture?.toYamal(),
        rank = rank,
        members = numListUsers,
        mean = mean,
        mediaType = MangaMediaTypeYamal.fromSerializedValue(mediaType),
        userVote = myListStatus?.score,
        startDate = startDate,
        endDate = endDate,
        numberOfVolumes = numVolumes,
        numberOfChapters = numChapters,
    )

fun MangaForDetailsMalNetwork.toYamal() =
    MangaForDetailsYamal(
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
        mediaType = MangaMediaTypeYamal.fromSerializedValue(mediaType),
        status = status ?: "",
        myListStatus = myListStatus?.toYamal(),
        numVolumes = numVolumes ?: 0,
        numChapters = numChapters ?: 0,
        authors = authors.map { it.toAuthorYamal() },
        pictures = pictures.map { it.toYamal() },
        background = background,
        relatedAnime = relatedAnime.map { it.toYamal() },
        relatedManga = relatedManga.map { it.toYamal() },
        recommendations = recommendations.map { it.toYamal() },
        serialization = serialization.map { it.toYamal() },
    )
