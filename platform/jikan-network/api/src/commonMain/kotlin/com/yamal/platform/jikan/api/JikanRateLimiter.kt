package com.yamal.platform.jikan.api

/**
 * Rate limiter for Jikan API requests.
 * Jikan API limits: 3 requests/second, 60 requests/minute.
 */
interface JikanRateLimiter {
    /**
     * Acquires a permit to make a request.
     * Suspends if rate limit would be exceeded.
     */
    suspend fun acquire()

    /**
     * Reports that a 429 (rate limited) response was received.
     * Triggers extended backoff.
     */
    fun reportRateLimited()
}
