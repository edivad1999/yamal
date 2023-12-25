package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class Status(
    @SerialName("watching") val watching: Int,
    @SerialName("completed") val completed: Int,
    @SerialName("on_hold") val onHold: Int,
    @SerialName("dropped") val dropped: Int,
    @SerialName("plan_to_watch") val planToWatch: Int,
)
