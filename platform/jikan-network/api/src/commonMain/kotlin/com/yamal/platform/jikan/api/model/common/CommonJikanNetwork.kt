package com.yamal.platform.jikan.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesJikanNetwork(
    @SerialName("jpg") val jpg: ImageUrlsJikanNetwork? = null,
    @SerialName("webp") val webp: ImageUrlsJikanNetwork? = null,
)

@Serializable
data class ImageUrlsJikanNetwork(
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("small_image_url") val smallImageUrl: String? = null,
    @SerialName("large_image_url") val largeImageUrl: String? = null,
)

@Serializable
data class MalUrlJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("type") val type: String? = null,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
)

@Serializable
data class TitleJikanNetwork(
    @SerialName("type") val type: String,
    @SerialName("title") val title: String,
)

@Serializable
data class DateRangeJikanNetwork(
    @SerialName("from") val from: String? = null,
    @SerialName("to") val to: String? = null,
    @SerialName("prop") val prop: DatePropJikanNetwork? = null,
    @SerialName("string") val string: String? = null,
)

@Serializable
data class DatePropJikanNetwork(
    @SerialName("from") val from: DateComponentJikanNetwork? = null,
    @SerialName("to") val to: DateComponentJikanNetwork? = null,
)

@Serializable
data class DateComponentJikanNetwork(
    @SerialName("day") val day: Int? = null,
    @SerialName("month") val month: Int? = null,
    @SerialName("year") val year: Int? = null,
)

@Serializable
data class BroadcastJikanNetwork(
    @SerialName("day") val day: String? = null,
    @SerialName("time") val time: String? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("string") val string: String? = null,
)

@Serializable
data class TrailerJikanNetwork(
    @SerialName("youtube_id") val youtubeId: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("embed_url") val embedUrl: String? = null,
    @SerialName("images") val images: TrailerImagesJikanNetwork? = null,
)

@Serializable
data class TrailerImagesJikanNetwork(
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("small_image_url") val smallImageUrl: String? = null,
    @SerialName("medium_image_url") val mediumImageUrl: String? = null,
    @SerialName("large_image_url") val largeImageUrl: String? = null,
    @SerialName("maximum_image_url") val maximumImageUrl: String? = null,
)

@Serializable
data class RelationJikanNetwork(
    @SerialName("relation") val relation: String,
    @SerialName("entry") val entry: List<MalUrlJikanNetwork>,
)

@Serializable
data class ThemeSongsJikanNetwork(
    @SerialName("openings") val openings: List<String> = emptyList(),
    @SerialName("endings") val endings: List<String> = emptyList(),
)

@Serializable
data class ExternalLinkJikanNetwork(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
)
