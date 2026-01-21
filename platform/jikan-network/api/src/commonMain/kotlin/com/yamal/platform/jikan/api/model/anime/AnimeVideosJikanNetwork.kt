package com.yamal.platform.jikan.api.model.anime

import com.yamal.platform.jikan.api.model.common.ImagesJikanNetwork
import com.yamal.platform.jikan.api.model.common.TrailerJikanNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeVideosJikanNetwork(
    @SerialName("promo") val promo: List<PromoVideoJikanNetwork> = emptyList(),
    @SerialName("episodes") val episodes: List<EpisodeVideoJikanNetwork> = emptyList(),
    @SerialName("music_videos") val musicVideos: List<MusicVideoJikanNetwork> = emptyList(),
)

@Serializable
data class PromoVideoJikanNetwork(
    @SerialName("title") val title: String,
    @SerialName("trailer") val trailer: TrailerJikanNetwork,
)

@Serializable
data class EpisodeVideoJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("title") val title: String,
    @SerialName("episode") val episode: String,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImagesJikanNetwork? = null,
)

@Serializable
data class MusicVideoJikanNetwork(
    @SerialName("title") val title: String,
    @SerialName("video") val video: TrailerJikanNetwork,
    @SerialName("meta") val meta: MusicVideoMetaJikanNetwork? = null,
)

@Serializable
data class MusicVideoMetaJikanNetwork(
    @SerialName("title") val title: String? = null,
    @SerialName("author") val author: String? = null,
)
