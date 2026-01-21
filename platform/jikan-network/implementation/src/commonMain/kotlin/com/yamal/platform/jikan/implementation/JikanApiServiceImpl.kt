package com.yamal.platform.jikan.implementation

import com.yamal.platform.jikan.api.JikanApiService
import com.yamal.platform.jikan.api.JikanRateLimiter
import com.yamal.platform.jikan.api.model.anime.AnimeCharacterJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeEpisodeJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeFullJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeNewsJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeRecommendationJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeReviewJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeStaffJikanNetwork
import com.yamal.platform.jikan.api.model.anime.AnimeVideosJikanNetwork
import com.yamal.platform.jikan.api.model.pagination.JikanPaginatedResponse
import com.yamal.platform.jikan.api.model.pagination.JikanSingleResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class JikanApiServiceImpl(
    private val httpClient: HttpClient,
    private val rateLimiter: JikanRateLimiter,
) : JikanApiService {
    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4"
    }

    private suspend inline fun <reified T> rateLimitedGet(
        url: String,
        crossinline block: io.ktor.client.request.HttpRequestBuilder.() -> Unit = {},
    ): T {
        rateLimiter.acquire()
        return try {
            httpClient.get(url) { block() }.body()
        } catch (e: Exception) {
            // Check if it's a 429 error
            if (e.message?.contains("429") == true) {
                rateLimiter.reportRateLimited()
            }
            throw e
        }
    }

    override suspend fun getAnimeById(id: Int): JikanSingleResponse<AnimeJikanNetwork> = rateLimitedGet("$BASE_URL/anime/$id")

    override suspend fun getAnimeFullById(id: Int): JikanSingleResponse<AnimeFullJikanNetwork> = rateLimitedGet("$BASE_URL/anime/$id/full")

    override suspend fun getAnimeCharacters(id: Int): JikanPaginatedResponse<AnimeCharacterJikanNetwork> =
        rateLimitedGet("$BASE_URL/anime/$id/characters")

    override suspend fun getAnimeStaff(id: Int): JikanPaginatedResponse<AnimeStaffJikanNetwork> = rateLimitedGet("$BASE_URL/anime/$id/staff")

    override suspend fun getAnimeEpisodes(
        id: Int,
        page: Int,
    ): JikanPaginatedResponse<AnimeEpisodeJikanNetwork> =
        rateLimitedGet("$BASE_URL/anime/$id/episodes") {
            parameter("page", page)
        }

    override suspend fun getAnimeVideos(id: Int): JikanSingleResponse<AnimeVideosJikanNetwork> = rateLimitedGet("$BASE_URL/anime/$id/videos")

    override suspend fun getAnimeRecommendations(id: Int): JikanPaginatedResponse<AnimeRecommendationJikanNetwork> =
        rateLimitedGet("$BASE_URL/anime/$id/recommendations")

    override suspend fun getAnimeReviews(
        id: Int,
        page: Int,
    ): JikanPaginatedResponse<AnimeReviewJikanNetwork> =
        rateLimitedGet("$BASE_URL/anime/$id/reviews") {
            parameter("page", page)
        }

    override suspend fun getAnimeNews(
        id: Int,
        page: Int,
    ): JikanPaginatedResponse<AnimeNewsJikanNetwork> =
        rateLimitedGet("$BASE_URL/anime/$id/news") {
            parameter("page", page)
        }

    override suspend fun searchAnime(
        query: String?,
        page: Int,
        limit: Int,
        type: String?,
        score: Float?,
        minScore: Float?,
        maxScore: Float?,
        status: String?,
        rating: String?,
        sfw: Boolean,
        genres: String?,
        genresExclude: String?,
        orderBy: String?,
        sort: String?,
        startDate: String?,
        endDate: String?,
    ): JikanPaginatedResponse<AnimeJikanNetwork> =
        rateLimitedGet("$BASE_URL/anime") {
            query?.let { parameter("q", it) }
            parameter("page", page)
            parameter("limit", limit)
            type?.let { parameter("type", it) }
            score?.let { parameter("score", it) }
            minScore?.let { parameter("min_score", it) }
            maxScore?.let { parameter("max_score", it) }
            status?.let { parameter("status", it) }
            rating?.let { parameter("rating", it) }
            if (sfw) parameter("sfw", "true")
            genres?.let { parameter("genres", it) }
            genresExclude?.let { parameter("genres_exclude", it) }
            orderBy?.let { parameter("order_by", it) }
            sort?.let { parameter("sort", it) }
            startDate?.let { parameter("start_date", it) }
            endDate?.let { parameter("end_date", it) }
        }

    override suspend fun getTopAnime(
        type: String?,
        filter: String?,
        rating: String?,
        sfw: Boolean,
        page: Int,
        limit: Int,
    ): JikanPaginatedResponse<AnimeJikanNetwork> =
        rateLimitedGet("$BASE_URL/top/anime") {
            type?.let { parameter("type", it) }
            filter?.let { parameter("filter", it) }
            rating?.let { parameter("rating", it) }
            if (sfw) parameter("sfw", "true")
            parameter("page", page)
            parameter("limit", limit)
        }

    override suspend fun getSeasonAnime(
        year: Int,
        season: String,
        filter: String?,
        sfw: Boolean,
        page: Int,
        limit: Int,
    ): JikanPaginatedResponse<AnimeJikanNetwork> =
        rateLimitedGet("$BASE_URL/seasons/$year/$season") {
            filter?.let { parameter("filter", it) }
            if (sfw) parameter("sfw", "true")
            parameter("page", page)
            parameter("limit", limit)
        }

    override suspend fun getSeasonNow(
        filter: String?,
        sfw: Boolean,
        page: Int,
        limit: Int,
    ): JikanPaginatedResponse<AnimeJikanNetwork> =
        rateLimitedGet("$BASE_URL/seasons/now") {
            filter?.let { parameter("filter", it) }
            if (sfw) parameter("sfw", "true")
            parameter("page", page)
            parameter("limit", limit)
        }

    override suspend fun getSeasonUpcoming(
        filter: String?,
        sfw: Boolean,
        page: Int,
        limit: Int,
    ): JikanPaginatedResponse<AnimeJikanNetwork> =
        rateLimitedGet("$BASE_URL/seasons/upcoming") {
            filter?.let { parameter("filter", it) }
            if (sfw) parameter("sfw", "true")
            parameter("page", page)
            parameter("limit", limit)
        }
}
