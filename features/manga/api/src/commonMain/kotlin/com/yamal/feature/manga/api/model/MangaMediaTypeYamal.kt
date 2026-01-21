package com.yamal.feature.manga.api.model

enum class MangaMediaTypeYamal(
    val serialName: String,
    val displayName: String,
) {
    Unknown("unknown", "Unknown"),
    Manga("manga", "Manga"),
    Novel("novel", "Novel"),
    OneShot("one_shot", "One-shot"),
    Doujinshi("doujinshi", "Doujinshi"),
    Manhwa("manhwa", "Manhwa"),
    Manhua("manhua", "Manhua"),
    Oel("oel", "OEL"),
    LightNovel("light_novel", "Light Novel"),
    ;

    companion object {
        fun fromSerializedValue(value: String?): MangaMediaTypeYamal = entries.find { it.serialName == value } ?: Unknown
    }
}
