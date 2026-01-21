package com.yamal.platform.network.api.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents pagination information from the MAL API.
 * Corresponds to the Paging schema in the API specification.
 */
@Serializable
data class PagingMalNetwork(
    @SerialName("previous") val previous: String? = null,
    @SerialName("next") val next: String? = null,
)
