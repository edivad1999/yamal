package com.yamal.platform.jikan.api.model.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanPaginatedResponse<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("pagination") val pagination: PaginationJikanNetwork,
)

@Serializable
data class JikanSingleResponse<T>(
    @SerialName("data") val data: T,
)

@Serializable
data class PaginationJikanNetwork(
    @SerialName("last_visible_page") val lastVisiblePage: Int,
    @SerialName("has_next_page") val hasNextPage: Boolean,
    @SerialName("current_page") val currentPage: Int? = null,
    @SerialName("items") val items: PaginationItemsJikanNetwork? = null,
)

@Serializable
data class PaginationItemsJikanNetwork(
    @SerialName("count") val count: Int,
    @SerialName("total") val total: Int,
    @SerialName("per_page") val perPage: Int,
)
