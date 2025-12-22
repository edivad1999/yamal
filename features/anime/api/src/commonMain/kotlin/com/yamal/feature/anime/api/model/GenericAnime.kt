package com.yamal.feature.anime.api.model

data class GenericAnime(
    val id: Int,
    val title: String,
    val mainPicture: Picture?,
    val rank: Int?,
    val members: Int?,
    val mean: Float?,
    val mediaType: MediaType,
    val userVote: Int?,
    val startDate: String?,
    val endDate: String?,
    val numberOfEpisodes: Int?,
)

data class Picture(
    val large: String?,
    val medium: String,
)

enum class MediaType {
    Unknown,
    Tv,
    Ova,
    Movie,
    Special,
    Ona,
    Music,
    ;

    companion object {
        fun fromSerializedValue(string: String?) =
            when (string) {
                "unknown" -> Unknown
                "tv" -> Tv
                "ova" -> Ova
                "movie" -> Movie
                "special" -> Special
                "ona" -> Ona
                "music" -> Music
                else -> Unknown
            }
    }
}

enum class Season(
    val serialName: String,
) {
    Winter("winter"),
    Spring("spring"),
    Summer("summer"),
    Fall("fall"),
}
