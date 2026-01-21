package com.yamal.feature.anime.api.model

enum class MediaTypeYamal(
    val serialName: String,
    val displayName: String,
) {
    Tv("tv", "TV"),
    Ova("ova", "OVA"),
    Movie("movie", "Movie"),
    Special("special", "Special"),
    Ona("ona", "ONA"),
    Music("music", "Music"),
    Unknown("unknown", "Unknown"),
    ;

    companion object {
        fun fromSerializedValue(value: String?): MediaTypeYamal = entries.firstOrNull { it.serialName == value } ?: Unknown
    }
}
