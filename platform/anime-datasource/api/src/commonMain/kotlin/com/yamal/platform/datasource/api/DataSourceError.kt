package com.yamal.platform.datasource.api

/**
 * Represents errors that can occur when fetching data from data sources.
 */
sealed class DataSourceError {
    /**
     * A network error occurred.
     */
    data class Network(
        val message: String,
        val throwable: Throwable? = null,
    ) : DataSourceError()

    /**
     * The API returned a rate limit error (429).
     */
    data class RateLimited(
        val retryAfterSeconds: Int? = null,
    ) : DataSourceError()

    /**
     * The requested resource was not found.
     */
    data class NotFound(
        val id: Int,
    ) : DataSourceError()

    /**
     * The user is not authorized (needs to log in).
     */
    data object Unauthorized : DataSourceError()

    /**
     * An unknown error occurred.
     */
    data class Unknown(
        val message: String,
    ) : DataSourceError()

    fun toErrorMessage(): String =
        when (this) {
            is Network -> message
            is RateLimited -> "Rate limited. Please try again in ${retryAfterSeconds ?: "a few"} seconds."
            is NotFound -> "Item with ID $id not found."
            is Unauthorized -> "Please log in to continue."
            is Unknown -> message
        }
}
