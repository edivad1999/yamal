package com.yamal.feature.manga.api.model

enum class UserMangaListStatusYamal(
    val serialName: String,
) {
    Reading("reading"),
    Completed("completed"),
    OnHold("on_hold"),
    Dropped("dropped"),
    PlanToRead("plan_to_read"),
    ;

    fun toSerialName(): String = serialName

    companion object {
        fun fromSerializedValue(value: String): UserMangaListStatusYamal = entries.first { it.serialName == value }
    }
}
