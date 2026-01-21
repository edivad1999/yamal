package com.yamal.feature.anime.api.model

enum class SeasonYamal(
    val serialName: String,
) {
    Winter("winter"),
    Spring("spring"),
    Summer("summer"),
    Fall("fall"),
    ;

    companion object {
        fun fromSerializedValue(value: String): SeasonYamal = entries.first { it.serialName == value }
    }
}
