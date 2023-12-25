package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class Statistics(
    @SerialName("num_list_users") val numListUsers: Int,
    @SerialName("status") val status: Status,
)
