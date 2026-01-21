package com.yamal.feature.anime.api.model

enum class UserListStatusYamal(
    val serialName: String,
) {
    Watching("watching"),
    Completed("completed"),
    OnHold("on_hold"),
    Dropped("dropped"),
    PlanToWatch("plan_to_watch"),
    ;

    fun toSerialName(): String = serialName

    companion object {
        fun fromSerializedValue(value: String): UserListStatusYamal = entries.first { it.serialName == value }
    }
}
