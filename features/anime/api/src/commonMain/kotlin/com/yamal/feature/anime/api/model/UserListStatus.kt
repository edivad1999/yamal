package com.yamal.feature.anime.api.model

enum class UserListStatus(val serialName: String) {
    Watching("watching"),
    Completed("completed"),
    OnHold("on_hold"),
    Dropped("dropped"),
    PlanToWatch(""),
}
