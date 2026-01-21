package com.yamal.platform.jikan.api.model.anime

import com.yamal.platform.jikan.api.model.common.BroadcastJikanNetwork
import com.yamal.platform.jikan.api.model.common.DateRangeJikanNetwork
import com.yamal.platform.jikan.api.model.common.ExternalLinkJikanNetwork
import com.yamal.platform.jikan.api.model.common.ImagesJikanNetwork
import com.yamal.platform.jikan.api.model.common.MalUrlJikanNetwork
import com.yamal.platform.jikan.api.model.common.RelationJikanNetwork
import com.yamal.platform.jikan.api.model.common.ThemeSongsJikanNetwork
import com.yamal.platform.jikan.api.model.common.TitleJikanNetwork
import com.yamal.platform.jikan.api.model.common.TrailerJikanNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImagesJikanNetwork,
    @SerialName("trailer") val trailer: TrailerJikanNetwork? = null,
    @SerialName("approved") val approved: Boolean = true,
    @SerialName("titles") val titles: List<TitleJikanNetwork> = emptyList(),
    @SerialName("title") val title: String,
    @SerialName("title_english") val titleEnglish: String? = null,
    @SerialName("title_japanese") val titleJapanese: String? = null,
    @SerialName("title_synonyms") val titleSynonyms: List<String> = emptyList(),
    @SerialName("type") val type: String? = null,
    @SerialName("source") val source: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("airing") val airing: Boolean = false,
    @SerialName("aired") val aired: DateRangeJikanNetwork? = null,
    @SerialName("duration") val duration: String? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("score") val score: Float? = null,
    @SerialName("scored_by") val scoredBy: Int? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("members") val members: Int? = null,
    @SerialName("favorites") val favorites: Int? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("background") val background: String? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("broadcast") val broadcast: BroadcastJikanNetwork? = null,
    @SerialName("producers") val producers: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("licensors") val licensors: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("studios") val studios: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("genres") val genres: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("explicit_genres") val explicitGenres: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("themes") val themes: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("demographics") val demographics: List<MalUrlJikanNetwork> = emptyList(),
)

@Serializable
data class AnimeFullJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImagesJikanNetwork,
    @SerialName("trailer") val trailer: TrailerJikanNetwork? = null,
    @SerialName("approved") val approved: Boolean = true,
    @SerialName("titles") val titles: List<TitleJikanNetwork> = emptyList(),
    @SerialName("title") val title: String,
    @SerialName("title_english") val titleEnglish: String? = null,
    @SerialName("title_japanese") val titleJapanese: String? = null,
    @SerialName("title_synonyms") val titleSynonyms: List<String> = emptyList(),
    @SerialName("type") val type: String? = null,
    @SerialName("source") val source: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("airing") val airing: Boolean = false,
    @SerialName("aired") val aired: DateRangeJikanNetwork? = null,
    @SerialName("duration") val duration: String? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("score") val score: Float? = null,
    @SerialName("scored_by") val scoredBy: Int? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("members") val members: Int? = null,
    @SerialName("favorites") val favorites: Int? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("background") val background: String? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("broadcast") val broadcast: BroadcastJikanNetwork? = null,
    @SerialName("producers") val producers: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("licensors") val licensors: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("studios") val studios: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("genres") val genres: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("explicit_genres") val explicitGenres: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("themes") val themes: List<MalUrlJikanNetwork> = emptyList(),
    @SerialName("demographics") val demographics: List<MalUrlJikanNetwork> = emptyList(),
    // Full anime extra fields
    @SerialName("relations") val relations: List<RelationJikanNetwork> = emptyList(),
    @SerialName("theme") val theme: ThemeSongsJikanNetwork? = null,
    @SerialName("external") val external: List<ExternalLinkJikanNetwork> = emptyList(),
    @SerialName("streaming") val streaming: List<ExternalLinkJikanNetwork> = emptyList(),
)
