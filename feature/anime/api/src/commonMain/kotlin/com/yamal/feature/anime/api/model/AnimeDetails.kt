package com.yamal.feature.anime.api.model

import com.yamal.feature.network.api.model.AlternativeTitlesNetwork
import com.yamal.feature.network.api.model.AnimeDetailsNetwork
import com.yamal.feature.network.api.model.AnimeRecommendationAggregationEdgeBase
import com.yamal.feature.network.api.model.AnimeStudioNetwork
import com.yamal.feature.network.api.model.BroadcastNetwork
import com.yamal.feature.network.api.model.GenreNetwork
import com.yamal.feature.network.api.model.MyListStatusNetwork
import com.yamal.feature.network.api.model.PictureNetwork
import com.yamal.feature.network.api.model.RelatedAnimeEdge
import com.yamal.feature.network.api.model.RelatedMangaEdge
import com.yamal.feature.network.api.model.StartSeason
import com.yamal.feature.network.api.model.StatisticsNetwork
import com.yamal.feature.network.api.model.StatsStatus

data class AnimeDetails(
    val id: Int,
    val title: String,
    val mainPicture: Picture?,
    val alternativeTitles: AlternativeTitles?,
    val startDate: String?,
    val endDate: String?,
    val synopsis: String?,
    val mean: Float?,
    val rank: Int?,
    val popularity: Int?,
    val numListUsers: Int,
    val numScoringUsers: Int,
    val nsfw: String?,
    val genre: List<Genre>,
    val createdAt: String,
    val updatedAt: String,
    val mediaType: MediaType,
    val status: String,
    val myListStatus: MyListStatus?,
    val numEpisodes: Int,
    val startSeason: AnimeSeason?,
    val broadcast: Broadcast?,
    val source: String?,
    val averageEpisodeDuration: Int?,
    val rating: String?,
    val studios: List<AnimeStudio>,
    val pictures: List<Picture>,
    val background: String?,
    val relatedAnime: List<RelatedItem<GenericAnime>>,
    val relatedManga: List<RelatedItem<GenericManga>>,
    val recommendations: List<AnimeRecommendation>,
    val statistics: Statistics?,
)

data class AnimeSeason(val year: String, val season: Season) {
    companion object
}

data class NamedIdObject(val id: Int, val name: String)

data class AlternativeTitles(
    val synonyms: List<String>,
    val en: String?,
    val ja: String?,
)

data class Broadcast(
    val dayOfTheWeek: String,
    val startTime: String?,
)

data class AnimeRecommendation(
    val node: GenericAnime,
    val numRecommendations: Int,
)

data class Statistics(
    val numListUsers: Int,
    val statsStatus: StatsStatus,
)

data class RelatedItem<T>(
    val node: T,
    val relation: Relation,
)

data class Relation(
    val type: RelationType,
    val formatted: String,
)

data class MyListStatus(
    val status: String? = null,
    val score: Int? = null,
    val numEpisodeWatched: Int? = null,
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

enum class RelationType(val serialName: String) {
    Sequel("sequel"),
    Prequel("prequel"),
    AlternativeSetting("alternative_setting"),
    AlternativeVersion("alternative_version"),
    SideStory("side_story"),
    ParentStory("parent_story"),
    Summary("summary"),
    FullStory("full_story"),
    ;
}

fun PictureNetwork.toPicture() = Picture(
    large = this.large,
    medium = this.medium
)

fun AlternativeTitlesNetwork.toAlternativeTitles() = AlternativeTitles(
    synonyms = synonyms ?: emptyList(),
    en = en,
    ja = ja
)

fun GenreNetwork.toGenre() = Genre(
    id = id,
    name = name
)

fun MyListStatusNetwork.toMyListStatus() = MyListStatus(
    status = status,
    score = score,
    numEpisodeWatched = numEpisodeWatched,
    isRewatching = isRewatching,
    startDate = startDate,
    finishDate = finishDate,
    priority = priority,
    numTimesRewatched = numTimesRewatched,
    rewatchValue = rewatchValue
)

fun BroadcastNetwork.toBroadcast() = Broadcast(
    dayOfTheWeek, startTime
)

fun AnimeStudioNetwork.toAnimeStudio() = AnimeStudio(
    id, name
)

fun AnimeRecommendationAggregationEdgeBase.toAnimeRecommendation() = AnimeRecommendation(
    node = GenericAnime(
        id = node.id,
        title = node.title,
        mainPicture = node.mainPictureNetwork?.toPicture(),
        rank = node.rank,
        members = node.numListUsers,
        mean = node.mean,
        mediaType = MediaType.fromSerializedValue(node.mediaType),
        userVote = node.myListStatusNetwork?.score,
        startDate = node.startDate,
        endDate = node.endDate,
        numberOfEpisodes = node.numEpisodes
    ),
    numRecommendations = numRecommendations
)

fun StatisticsNetwork.toStatistics() = Statistics(
    numListUsers, statsStatus
)

fun StartSeason.toAnimeSeason() = AnimeSeason(
    year.toString(),
    Season.entries.first { it.serialName == season }
)

fun RelatedAnimeEdge.toRelatedAnime() = RelatedItem<GenericAnime>(
    node = GenericAnime(
        id = node.id,
        title = node.title,
        mainPicture = node.mainPictureNetwork?.toPicture(),
        rank = node.rank,
        members = node.numListUsers,
        mean = node.mean,
        mediaType = MediaType.fromSerializedValue(node.mediaType),
        userVote = node.myListStatusNetwork?.score,
        startDate = node.startDate,
        endDate = node.endDate,
        numberOfEpisodes = node.numEpisodes
    ),
    relation = Relation(RelationType.entries.first{it.serialName == relation_type}, relation_type_formatted?: "")
)

fun RelatedMangaEdge.toRelatedManga() = RelatedItem<GenericManga>(
    node = GenericManga(
        id = node.id,
        title = node.title,
        mainPicture = node.mainPictureNetwork?.toPicture(),
        rank = node.rank,
        members = node.numListUsers,
        mean = node.mean,
        mediaType = MediaType.fromSerializedValue(node.mediaType),
        userVote = node.myListStatusNetwork?.score,
        startDate = node.startDate,
        endDate = node.endDate,
        numberOfVolumes = node.numVolumes,
        numberOfChapters = node.numChapters
    ),
    relation = Relation(RelationType.entries.first{it.serialName == relation_type}, relation_type_formatted?: "")
)

fun AnimeDetailsNetwork.toDomain() = AnimeDetails(
    id = id,
    title = title,
    mainPicture = mainPictureNetwork?.toPicture(),
    alternativeTitles = alternativeTitlesNetwork?.toAlternativeTitles(),
    startDate = startDate,
    endDate = endDate,
    synopsis = synopsis,
    mean = mean,
    rank = rank,
    popularity = popularity,
    numListUsers = numListUsers,
    numScoringUsers = numScoringUsers,
    nsfw = nsfw,
    genre = genreNetworks.map { it.toGenre() },
    createdAt = createdAt,
    updatedAt = updatedAt,
    mediaType = MediaType.fromSerializedValue(mediaType),
    status = status,
    myListStatus = myListStatusNetwork?.toMyListStatus(),
    numEpisodes = numEpisodes,
    startSeason = startSeason?.toAnimeSeason(),
    broadcast = broadcastNetwork?.toBroadcast(),
    source = source,
    averageEpisodeDuration = averageEpisodeDuration,
    rating = rating,
    studios = studios.map { it.toAnimeStudio() },
    pictures = pictureNetworks.map { it.toPicture() },
    background = background,
    relatedAnime = relatedAnime.map { it.toRelatedAnime() },
    relatedManga = relatedManga.map { it.toRelatedManga() },
    recommendations = recommendations.map { it.toAnimeRecommendation() },
    statistics = statisticsNetwork?.toStatistics()
)

typealias Genre = NamedIdObject
typealias AnimeStudio = NamedIdObject
