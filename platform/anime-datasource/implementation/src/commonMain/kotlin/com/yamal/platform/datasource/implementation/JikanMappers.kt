package com.yamal.platform.datasource.implementation

import com.yamal.feature.anime.api.model.AlternativeTitlesYamal
import com.yamal.feature.anime.api.model.AnimeCharacterYamal
import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeListStatusYamal
import com.yamal.feature.anime.api.model.AnimeRecommendationYamal
import com.yamal.feature.anime.api.model.AnimeReviewYamal
import com.yamal.feature.anime.api.model.AnimeSeasonYamal
import com.yamal.feature.anime.api.model.AnimeStatisticsYamal
import com.yamal.feature.anime.api.model.AnimeStudioYamal
import com.yamal.feature.anime.api.model.BroadcastYamal
import com.yamal.feature.anime.api.model.ExternalLinkYamal
import com.yamal.feature.anime.api.model.GenreYamal
import com.yamal.feature.anime.api.model.MangaForListYamal
import com.yamal.feature.anime.api.model.MediaTypeYamal
import com.yamal.feature.anime.api.model.PictureYamal
import com.yamal.feature.anime.api.model.RelatedItemYamal
import com.yamal.feature.anime.api.model.RelationTypeYamal
import com.yamal.feature.anime.api.model.RelationYamal
import com.yamal.feature.anime.api.model.ReviewReactionsYamal
import com.yamal.feature.anime.api.model.ReviewUserYamal
import com.yamal.feature.anime.api.model.SeasonYamal
import com.yamal.feature.anime.api.model.ThemeSongsYamal
import com.yamal.feature.anime.api.model.TrailerYamal
import com.yamal.feature.anime.api.model.VoiceActorYamal
import com.yamal.platform.jikan.api.model.anime.AnimeCharacterJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeFullJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeRecommendationJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeReviewJikanNetwork
import com.yamal.platform.jikan.api.model.common.BroadcastJikanNetwork
import com.yamal.platform.jikan.api.model.common.ExternalLinkJikanNetwork
import com.yamal.platform.jikan.api.model.common.ImagesJikanNetwork
import com.yamal.platform.jikan.api.model.common.MalUrlJikanNetwork
import com.yamal.platform.jikan.api.model.common.RelationJikanNetwork
import com.yamal.platform.jikan.api.model.common.ThemeSongsJikanNetwork
import com.yamal.platform.jikan.api.model.common.TitleJikanNetwork
import com.yamal.platform.jikan.api.model.common.TrailerJikanNetwork

// ===========================================
// Basic Anime Mapping
// ===========================================

fun AnimeJikanNetwork.toYamal(): AnimeForListYamal =
    AnimeForListYamal(
        id = malId,
        title = title,
        mainPicture = images.toYamal(),
        rank = rank,
        members = members,
        mean = score,
        mediaType = type.toMediaType(),
        userVote = null, // Not available from Jikan list endpoints
        startDate = aired?.from?.substringBefore("T"),
        endDate = aired?.to?.substringBefore("T"),
        numberOfEpisodes = episodes,
        broadcast = broadcast?.toYamal(),
    )

// ===========================================
// Full Anime Details Mapping
// ===========================================

fun AnimeFullJikanNetwork.toYamal(
    malUserStatus: AnimeListStatusYamal? = null,
    characters: List<AnimeCharacterYamal> = emptyList(),
    reviews: List<AnimeReviewYamal> = emptyList(),
): AnimeForDetailsYamal =
    AnimeForDetailsYamal(
        id = malId,
        title = title,
        mainPicture = images.toYamal(),
        alternativeTitles = titles.toAlternativeTitles(titleEnglish, titleJapanese, titleSynonyms),
        startDate = aired?.from?.substringBefore("T"),
        endDate = aired?.to?.substringBefore("T"),
        synopsis = synopsis,
        mean = score,
        rank = rank,
        popularity = popularity,
        numListUsers = members ?: 0,
        numScoringUsers = scoredBy ?: 0,
        nsfw = null, // Not directly available from Jikan
        genres = (genres + explicitGenres + themes + demographics).map { it.toGenre() },
        createdAt = "", // Not available from Jikan
        updatedAt = "", // Not available from Jikan
        mediaType = type.toMediaType(),
        status = status ?: "",
        myListStatus = malUserStatus,
        numEpisodes = episodes ?: 0,
        startSeason =
            run {
                val seasonVal = season
                if (year != null && seasonVal != null) {
                    AnimeSeasonYamal(year = year.toString(), season = SeasonYamal.fromSerializedValue(seasonVal))
                } else {
                    null
                }
            },
        broadcast = broadcast?.toYamal(),
        source = source,
        averageEpisodeDuration = duration?.parseDurationToSeconds(),
        rating = rating,
        studios = studios.map { it.toStudio() },
        pictures = emptyList(), // Can be fetched separately from /pictures endpoint
        background = background,
        relatedAnime = relations.flatMap { it.toRelatedAnime() },
        relatedManga = relations.flatMap { it.toRelatedManga() },
        recommendations = emptyList(), // Can be fetched separately from /recommendations endpoint
        statistics = null, // Can be fetched separately from /statistics endpoint
        // Jikan API additional fields
        trailer = trailer?.toYamal(),
        themeSongs = theme?.toYamal(),
        streamingLinks = streaming.map { it.toYamal() },
        externalLinks = external.map { it.toYamal() },
        characters = characters,
        reviews = reviews,
    )

// ===========================================
// Supporting Type Mappings
// ===========================================

fun ImagesJikanNetwork?.toYamal(): PictureYamal? {
    val imageUrl = this?.jpg?.largeImageUrl ?: this?.jpg?.imageUrl ?: this?.webp?.largeImageUrl ?: this?.webp?.imageUrl
    return imageUrl?.let { PictureYamal(medium = it, large = it) }
}

fun BroadcastJikanNetwork.toYamal(): BroadcastYamal =
    BroadcastYamal(
        dayOfTheWeek = day?.lowercase()?.replace("s", "") ?: "", // "Mondays" -> "monday"
        startTime = time,
    )

fun MalUrlJikanNetwork.toGenre(): GenreYamal =
    GenreYamal(
        id = malId,
        name = name,
    )

fun MalUrlJikanNetwork.toStudio(): AnimeStudioYamal =
    AnimeStudioYamal(
        id = malId,
        name = name,
    )

fun List<TitleJikanNetwork>.toAlternativeTitles(
    english: String?,
    japanese: String?,
    synonyms: List<String>,
): AlternativeTitlesYamal =
    AlternativeTitlesYamal(
        en = english ?: firstOrNull { it.type == "English" }?.title,
        ja = japanese ?: firstOrNull { it.type == "Japanese" }?.title,
        synonyms = synonyms.ifEmpty { filter { it.type == "Synonym" }.map { it.title } },
    )

fun String?.toMediaType(): MediaTypeYamal =
    when (this?.lowercase()) {
        "tv" -> MediaTypeYamal.Tv

        "movie" -> MediaTypeYamal.Movie

        "ova" -> MediaTypeYamal.Ova

        "ona" -> MediaTypeYamal.Ona

        "special" -> MediaTypeYamal.Special

        "music" -> MediaTypeYamal.Music

        "cm" -> MediaTypeYamal.Special

        // Map CM to Special
        "pv" -> MediaTypeYamal.Special

        // Map PV to Special
        "tv_special" -> MediaTypeYamal.Special

        // Map TV Special to Special
        else -> MediaTypeYamal.Unknown
    }

fun RelationJikanNetwork.toRelatedAnime(): List<RelatedItemYamal<AnimeForListYamal>> =
    entry
        .filter { it.type == "anime" }
        .map { malUrl ->
            RelatedItemYamal(
                relation = relation.toRelationYamal(),
                node =
                    AnimeForListYamal(
                        id = malUrl.malId,
                        title = malUrl.name,
                        mainPicture = null,
                        rank = null,
                        members = null,
                        mean = null,
                        mediaType = MediaTypeYamal.Unknown,
                        userVote = null,
                        startDate = null,
                        endDate = null,
                        numberOfEpisodes = null,
                    ),
            )
        }

fun RelationJikanNetwork.toRelatedManga(): List<RelatedItemYamal<MangaForListYamal>> =
    entry
        .filter { it.type == "manga" }
        .map { malUrl ->
            RelatedItemYamal(
                relation = relation.toRelationYamal(),
                node =
                    MangaForListYamal(
                        id = malUrl.malId,
                        title = malUrl.name,
                        mainPicture = null,
                        rank = null,
                        members = null,
                        mean = null,
                        mediaType = MediaTypeYamal.Unknown,
                        userVote = null,
                        startDate = null,
                        endDate = null,
                        numberOfVolumes = null,
                        numberOfChapters = null,
                    ),
            )
        }

fun String.toRelationYamal(): RelationYamal =
    RelationYamal(
        type = toRelationType(),
        formatted = this,
    )

fun String.toRelationType(): RelationTypeYamal =
    when (lowercase()) {
        "sequel" -> RelationTypeYamal.Sequel

        "prequel" -> RelationTypeYamal.Prequel

        "alternative setting" -> RelationTypeYamal.AlternativeSetting

        "alternative version" -> RelationTypeYamal.AlternativeVersion

        "side story" -> RelationTypeYamal.SideStory

        "parent story" -> RelationTypeYamal.ParentStory

        "summary" -> RelationTypeYamal.Summary

        "full story" -> RelationTypeYamal.FullStory

        // Map other types to closest match
        "spin-off", "spin off" -> RelationTypeYamal.SideStory

        "adaptation" -> RelationTypeYamal.AlternativeVersion

        "character" -> RelationTypeYamal.SideStory

        "other" -> RelationTypeYamal.SideStory

        else -> RelationTypeYamal.SideStory
    }

fun AnimeRecommendationJikanNetwork.toYamal(): AnimeRecommendationYamal =
    AnimeRecommendationYamal(
        node =
            AnimeForListYamal(
                id = entry.malId,
                title = entry.title,
                mainPicture = entry.images.toYamal(),
                rank = null,
                members = null,
                mean = null,
                mediaType = MediaTypeYamal.Unknown,
                userVote = null,
                startDate = null,
                endDate = null,
                numberOfEpisodes = null,
            ),
        numRecommendations = votes,
    )

// ===========================================
// Utility Functions
// ===========================================

/**
 * Parse duration string like "24 min per ep" or "1 hr 30 min" to seconds.
 */
fun String.parseDurationToSeconds(): Int? {
    val hourMatch = Regex("(\\d+)\\s*hr").find(this)
    val minMatch = Regex("(\\d+)\\s*min").find(this)

    val hours = hourMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
    val minutes = minMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0

    return if (hours > 0 || minutes > 0) {
        (hours * 3600) + (minutes * 60)
    } else {
        null
    }
}

// ===========================================
// Jikan Additional Data Mappings
// ===========================================

fun TrailerJikanNetwork.toYamal(): TrailerYamal =
    TrailerYamal(
        youtubeId = youtubeId,
        url = url,
        embedUrl = embedUrl,
        thumbnailUrl = images?.largeImageUrl ?: images?.mediumImageUrl ?: images?.imageUrl,
    )

fun ThemeSongsJikanNetwork.toYamal(): ThemeSongsYamal =
    ThemeSongsYamal(
        openings = openings,
        endings = endings,
    )

fun ExternalLinkJikanNetwork.toYamal(): ExternalLinkYamal =
    ExternalLinkYamal(
        name = name,
        url = url,
    )

fun AnimeCharacterJikanNetwork.toYamal(): AnimeCharacterYamal =
    AnimeCharacterYamal(
        id = character.malId,
        name = character.name,
        imageUrl = character.images?.jpg?.imageUrl ?: character.images?.webp?.imageUrl,
        role = role,
        voiceActors =
            voiceActors.map { va ->
                VoiceActorYamal(
                    id = va.person.malId,
                    name = va.person.name,
                    imageUrl =
                        va.person.images
                            ?.jpg
                            ?.imageUrl,
                    language = va.language,
                )
            },
    )

fun AnimeReviewJikanNetwork.toYamal(): AnimeReviewYamal =
    AnimeReviewYamal(
        id = malId,
        url = url,
        score = score,
        review = review,
        date = date,
        isSpoiler = isSpoiler,
        isPreliminary = isPreliminary,
        episodesWatched = episodesWatched,
        tags = tags,
        reactions =
            ReviewReactionsYamal(
                overall = reactions.overall,
                nice = reactions.nice,
                loveIt = reactions.loveIt,
                funny = reactions.funny,
                confusing = reactions.confusing,
                informative = reactions.informative,
                wellWritten = reactions.wellWritten,
                creative = reactions.creative,
            ),
        user =
            ReviewUserYamal(
                username = user.username,
                url = user.url,
                imageUrl = user.images?.jpg?.imageUrl ?: user.images?.webp?.imageUrl,
            ),
    )
