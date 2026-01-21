package com.yamal.platform.datasource.api

import androidx.paging.PagingSource
import arrow.core.Either
import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeListStatusYamal
import com.yamal.feature.anime.api.model.SeasonYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.platform.datasource.api.model.PaginatedResult

/**
 * Unified data source for anime data.
 * Combines data from Jikan API (discovery, details) and MAL API (user list operations).
 */
interface AnimeDataSource {
    // ===========================================
    // Discovery Endpoints (Jikan-powered)
    // ===========================================

    /**
     * Search anime by query.
     */
    fun searchAnime(query: String): PagingSource<Int, AnimeForListYamal>

    /**
     * Get top/ranked anime.
     * @param filter Optional filter like "airing", "upcoming", "bypopularity", "favorite"
     * @param type Optional type filter like "tv", "movie", "ova", "special", "ona", "music"
     */
    fun getTopAnime(
        filter: String? = null,
        type: String? = null,
    ): PagingSource<Int, AnimeForListYamal>

    /**
     * Get anime for a specific season.
     */
    fun getSeasonalAnime(
        year: Int,
        season: SeasonYamal,
    ): PagingSource<Int, AnimeForListYamal>

    /**
     * Get anime from current season.
     */
    fun getCurrentSeasonAnime(): PagingSource<Int, AnimeForListYamal>

    /**
     * Get upcoming anime.
     */
    fun getUpcomingAnime(): PagingSource<Int, AnimeForListYamal>

    // ===========================================
    // Details Endpoints (Jikan + MAL user status)
    // ===========================================

    /**
     * Get anime details.
     * Fetches rich data from Jikan and combines with MAL user list status if authenticated.
     */
    suspend fun getAnimeDetails(id: Int): Either<DataSourceError, AnimeForDetailsYamal>

    // ===========================================
    // User List Operations (MAL-only)
    // ===========================================

    /**
     * Get user's anime list filtered by status.
     * Items are enriched with Jikan data for better display.
     */
    fun getUserAnimeList(status: UserListStatusYamal): PagingSource<Int, AnimeForListYamal>

    /**
     * Update anime list status.
     */
    suspend fun updateAnimeListStatus(
        animeId: Int,
        status: String? = null,
        score: Int? = null,
        numWatchedEpisodes: Int? = null,
        isRewatching: Boolean? = null,
        priority: Int? = null,
        numTimesRewatched: Int? = null,
        rewatchValue: Int? = null,
        tags: String? = null,
        comments: String? = null,
    ): Either<DataSourceError, AnimeListStatusYamal>

    /**
     * Delete anime from user's list.
     */
    suspend fun deleteAnimeListStatus(animeId: Int): Either<DataSourceError, Unit>

    // ===========================================
    // MAL-specific Personalized Endpoints
    // ===========================================

    /**
     * Get anime suggestions for the authenticated user.
     * Requires authentication - uses MAL API.
     */
    suspend fun getAnimeSuggestions(limit: Int = 10): Either<DataSourceError, List<AnimeForListYamal>>

    // ===========================================
    // Convenience Methods (Jikan-powered, limited results)
    // ===========================================

    /**
     * Get trending (currently airing) anime - limited list for home screen.
     */
    suspend fun getTrendingAnime(limit: Int = 10): Either<DataSourceError, List<AnimeForListYamal>>

    /**
     * Get top anime - limited list for home screen.
     */
    suspend fun getTopAnimeList(limit: Int = 10): Either<DataSourceError, List<AnimeForListYamal>>

    /**
     * Get upcoming anime - limited list for home screen.
     */
    suspend fun getUpcomingAnimeList(limit: Int = 10): Either<DataSourceError, List<AnimeForListYamal>>
}
