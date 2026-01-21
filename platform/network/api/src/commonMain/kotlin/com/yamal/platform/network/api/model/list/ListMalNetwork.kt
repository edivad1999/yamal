package com.yamal.platform.network.api.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a generic paginated list response from the MAL API.
 * Corresponds to various List schemas in the API specification.
 *
 * @param T The type of items contained in the list
 */
@Serializable
data class ListMalNetwork<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("paging") val paging: PagingMalNetwork? = null,
)
