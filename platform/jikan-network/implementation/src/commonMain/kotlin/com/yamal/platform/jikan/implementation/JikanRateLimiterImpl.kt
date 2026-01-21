package com.yamal.platform.jikan.implementation

import com.yamal.platform.jikan.api.JikanRateLimiter
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Token bucket rate limiter for Jikan API.
 * Enforces: 3 requests/second, 60 requests/minute.
 */
@OptIn(ExperimentalTime::class)
class JikanRateLimiterImpl : JikanRateLimiter {
    private val mutex = Mutex()
    private val timeSource = TimeSource.Monotonic

    // Rate limits
    private val requestsPerSecond = 3
    private val requestsPerMinute = 60

    // Token buckets
    private var secondTokens = requestsPerSecond
    private var minuteTokens = requestsPerMinute

    // Timestamps for refilling
    private var lastSecondRefill: TimeMark = timeSource.markNow()
    private var lastMinuteRefill: TimeMark = timeSource.markNow()

    // Backoff state for 429 responses
    private var backoffUntil: TimeMark? = null

    override suspend fun acquire() {
        mutex.withLock {
            // Check if we're in backoff period
            backoffUntil?.let { until ->
                if (until.hasNotPassedNow()) {
                    val waitTime = until.elapsedNow().absoluteValue.inWholeMilliseconds
                    if (waitTime > 0) {
                        delay(waitTime)
                    }
                }
                backoffUntil = null
            }

            refillTokens()

            // Wait if we don't have tokens available
            while (secondTokens <= 0 || minuteTokens <= 0) {
                val waitTime =
                    if (secondTokens <= 0) {
                        // Wait until next second refill
                        val elapsed = lastSecondRefill.elapsedNow().inWholeMilliseconds
                        (1000 - elapsed).coerceAtLeast(100)
                    } else {
                        // Wait until next minute refill (but cap at 5 seconds to re-check)
                        5000L
                    }
                delay(waitTime)
                refillTokens()
            }

            // Consume tokens
            secondTokens--
            minuteTokens--
        }
    }

    override fun reportRateLimited() {
        // On 429 response, back off for 5 seconds
        backoffUntil = timeSource.markNow() + 5.seconds
    }

    private fun refillTokens() {
        val now = timeSource.markNow()

        // Refill second bucket
        val secondsElapsed = lastSecondRefill.elapsedNow().inWholeSeconds
        if (secondsElapsed >= 1) {
            secondTokens = requestsPerSecond
            lastSecondRefill = now
        }

        // Refill minute bucket
        val minutesElapsed = lastMinuteRefill.elapsedNow().inWholeMinutes
        if (minutesElapsed >= 1) {
            minuteTokens = requestsPerMinute
            lastMinuteRefill = now
        }
    }
}
