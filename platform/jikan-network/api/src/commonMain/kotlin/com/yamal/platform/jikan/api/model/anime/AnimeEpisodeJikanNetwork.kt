package com.yamal.platform.jikan.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeEpisodeJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String? = null,
    @SerialName("title") val title: String,
    @SerialName("title_japanese") val titleJapanese: String? = null,
    @SerialName("title_romanji") val titleRomanji: String? = null,
    @SerialName("aired") val aired: String? = null,
    @SerialName("score") val score: Float? = null,
    @SerialName("filler") val filler: Boolean = false,
    @SerialName("recap") val recap: Boolean = false,
    @SerialName("forum_url") val forumUrl: String? = null,
)
