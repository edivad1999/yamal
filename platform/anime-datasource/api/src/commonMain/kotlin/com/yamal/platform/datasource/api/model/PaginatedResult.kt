package com.yamal.platform.datasource.api.model

/**
 * Represents a paginated result from the data source.
 */
data class PaginatedResult<T>(
    val items: List<T>,
    val hasNextPage: Boolean,
    val currentPage: Int,
    val totalPages: Int? = null,
)
