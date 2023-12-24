package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Statistics(
    val num_list_users: Int,
    val status: Status
)
