package com.yamal.platform.jikan.api.model.anime

import com.yamal.platform.jikan.api.model.common.ImagesJikanNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeNewsJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("title") val title: String,
    @SerialName("date") val date: String,
    @SerialName("author_username") val authorUsername: String,
    @SerialName("author_url") val authorUrl: String,
    @SerialName("forum_url") val forumUrl: String? = null,
    @SerialName("images") val images: ImagesJikanNetwork? = null,
    @SerialName("comments") val comments: Int = 0,
    @SerialName("excerpt") val excerpt: String? = null,
)
