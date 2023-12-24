package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val watching: Int,
    val completed: Int,
    val on_hold: Int,
    val dropped: Int,
    val plan_to_watch: Int
)
