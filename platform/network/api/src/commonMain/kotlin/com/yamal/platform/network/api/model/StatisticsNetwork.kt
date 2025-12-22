package com.yamal.platform.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class StatisticsNetwork(
    @SerialName("num_list_users") val numListUsers: Int,
    @SerialName("status") val statsStatus: StatsStatus,
)
