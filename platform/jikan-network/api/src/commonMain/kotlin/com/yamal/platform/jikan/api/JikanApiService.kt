package com.yamal.platform.jikan.api

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

interface JikanApiService {
    // Single anime endpoints
    suspend fun getAnimeById(id: Int): JikanSingleResponse<AnimeJikanNetwork>

    suspend fun getAnimeFullById(id: Int): JikanSingleResponse<AnimeFullJikanNetwork>

    suspend fun getAnimeCharacters(id: Int): JikanPaginatedResponse<AnimeCharacterJikanNetwork>

    suspend fun getAnimeStaff(id: Int): JikanPaginatedResponse<AnimeStaffJikanNetwork>

    suspend fun getAnimeEpisodes(
        id: Int,
        page: Int = 1,
    ): JikanPaginatedResponse<AnimeEpisodeJikanNetwork>

    suspend fun getAnimeVideos(id: Int): JikanSingleResponse<AnimeVideosJikanNetwork>

    suspend fun getAnimeRecommendations(id: Int): JikanPaginatedResponse<AnimeRecommendationJikanNetwork>

    suspend fun getAnimeReviews(
        id: Int,
        page: Int = 1,
    ): JikanPaginatedResponse<AnimeReviewJikanNetwork>

    suspend fun getAnimeNews(
        id: Int,
        page: Int = 1,
    ): JikanPaginatedResponse<AnimeNewsJikanNetwork>

    // Search endpoints
    suspend fun searchAnime(
        query: String? = null,
        page: Int = 1,
        limit: Int = 25,
        type: String? = null,
        score: Float? = null,
        minScore: Float? = null,
        maxScore: Float? = null,
        status: String? = null,
        rating: String? = null,
        sfw: Boolean = true,
        genres: String? = null,
        genresExclude: String? = null,
        orderBy: String? = null,
        sort: String? = null,
        startDate: String? = null,
        endDate: String? = null,
    ): JikanPaginatedResponse<AnimeJikanNetwork>

    // Top/Rankings endpoints
    suspend fun getTopAnime(
        type: String? = null,
        filter: String? = null,
        rating: String? = null,
        sfw: Boolean = true,
        page: Int = 1,
        limit: Int = 25,
    ): JikanPaginatedResponse<AnimeJikanNetwork>

    // Seasonal endpoints
    suspend fun getSeasonAnime(
        year: Int,
        season: String,
        filter: String? = null,
        sfw: Boolean = true,
        page: Int = 1,
        limit: Int = 25,
    ): JikanPaginatedResponse<AnimeJikanNetwork>

    suspend fun getSeasonNow(
        filter: String? = null,
        sfw: Boolean = true,
        page: Int = 1,
        limit: Int = 25,
    ): JikanPaginatedResponse<AnimeJikanNetwork>

    suspend fun getSeasonUpcoming(
        filter: String? = null,
        sfw: Boolean = true,
        page: Int = 1,
        limit: Int = 25,
    ): JikanPaginatedResponse<AnimeJikanNetwork>
}
