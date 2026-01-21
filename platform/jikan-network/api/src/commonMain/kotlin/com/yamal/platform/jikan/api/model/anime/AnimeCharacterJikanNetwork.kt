package com.yamal.platform.jikan.api.model.anime

import com.yamal.platform.jikan.api.model.common.ImagesJikanNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeCharacterJikanNetwork(
    @SerialName("character") val character: CharacterMetaJikanNetwork,
    @SerialName("role") val role: String,
    @SerialName("favorites") val favorites: Int? = null,
    @SerialName("voice_actors") val voiceActors: List<VoiceActorJikanNetwork> = emptyList(),
)

@Serializable
data class CharacterMetaJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImagesJikanNetwork? = null,
    @SerialName("name") val name: String,
)

@Serializable
data class VoiceActorJikanNetwork(
    @SerialName("person") val person: PersonMetaJikanNetwork,
    @SerialName("language") val language: String,
)

@Serializable
data class PersonMetaJikanNetwork(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImagesJikanNetwork? = null,
    @SerialName("name") val name: String,
)
