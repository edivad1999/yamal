package com.yamal.feature.anime.api.model

data class AnimeCharacterYamal(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val role: String,
    val voiceActors: List<VoiceActorYamal>,
)

data class VoiceActorYamal(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val language: String,
)
