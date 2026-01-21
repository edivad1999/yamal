package com.yamal.platform.datasource.implementation

import androidx.paging.PagingSource
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeListStatusYamal
import com.yamal.feature.anime.api.model.SeasonYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.feature.login.api.LoginRepository
import com.yamal.platform.datasource.api.AnimeDataSource
import com.yamal.platform.datasource.api.DataSourceError
import com.yamal.platform.jikan.api.JikanApiService
import com.yamal.platform.jikan.api.JikanRateLimiter
import com.yamal.platform.network.api.ApiService
import com.yamal.platform.network.api.apiCallScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

/**
 * Implementation of AnimeDataSource that combines Jikan API (for discovery/details)
 * with MAL API (for user list operations).
 */
class AnimeDataSourceImpl(
    private val jikanApiService: JikanApiService,
    private val malApiService: ApiService,
    private val jikanRateLimiter: JikanRateLimiter,
    private val loginRepository: LoginRepository,
) : AnimeDataSource {
    // ===========================================
    // Discovery Endpoints (Jikan-powered)
    // ===========================================

    override fun searchAnime(query: String): PagingSource<Int, AnimeForListYamal> =
        JikanPagingSource(
            rateLimiter = jikanRateLimiter,
            apiCall = { page, limit ->
                jikanApiService.searchAnime(query = query, page = page, limit = limit)
            },
            mapper = { it.toYamal() },
        )

    override fun getTopAnime(
        filter: String?,
        type: String?,
    ): PagingSource<Int, AnimeForListYamal> =
        JikanPagingSource(
            rateLimiter = jikanRateLimiter,
            apiCall = { page, limit ->
                jikanApiService.getTopAnime(type = type, filter = filter, page = page, limit = limit)
            },
            mapper = { it.toYamal() },
        )

    override fun getSeasonalAnime(
        year: Int,
        season: SeasonYamal,
    ): PagingSource<Int, AnimeForListYamal> =
        JikanPagingSource(
            rateLimiter = jikanRateLimiter,
            apiCall = { page, limit ->
                jikanApiService.getSeasonAnime(
                    year = year,
                    season = season.serialName,
                    page = page,
                    limit = limit,
                )
            },
            mapper = { it.toYamal() },
        )

    override fun getCurrentSeasonAnime(): PagingSource<Int, AnimeForListYamal> =
        JikanPagingSource(
            rateLimiter = jikanRateLimiter,
            apiCall = { page, limit ->
                jikanApiService.getSeasonNow(page = page, limit = limit)
            },
            mapper = { it.toYamal() },
        )

    override fun getUpcomingAnime(): PagingSource<Int, AnimeForListYamal> =
        JikanPagingSource(
            rateLimiter = jikanRateLimiter,
            apiCall = { page, limit ->
                jikanApiService.getSeasonUpcoming(page = page, limit = limit)
            },
            mapper = { it.toYamal() },
        )

    // ===========================================
    // Details Endpoints (Jikan + MAL user status)
    // ===========================================

    override suspend fun getAnimeDetails(id: Int): Either<DataSourceError, AnimeForDetailsYamal> =
        coroutineScope {
            // Fetch from multiple sources in parallel
            val jikanDeferred =
                async {
                    runCatching {
                        jikanRateLimiter.acquire()
                        jikanApiService.getAnimeFullById(id)
                    }
                }

            // Fetch characters from Jikan (separate endpoint)
            val charactersDeferred =
                async {
                    runCatching {
                        jikanRateLimiter.acquire()
                        jikanApiService.getAnimeCharacters(id)
                    }
                }

            // Fetch reviews from Jikan (separate endpoint)
            val reviewsDeferred =
                async {
                    runCatching {
                        jikanRateLimiter.acquire()
                        jikanApiService.getAnimeReviews(id, page = 1)
                    }
                }

            // Only fetch MAL user status if authenticated
            val malDeferred =
                if (loginRepository.isUserAuthenticated().first()) {
                    async {
                        runCatching {
                            malApiService.getAnimeDetails(id)
                        }
                    }
                } else {
                    null
                }

            val jikanResult = jikanDeferred.await()
            val charactersResult = charactersDeferred.await()
            val reviewsResult = reviewsDeferred.await()
            val malResult = malDeferred?.await()

            jikanResult.fold(
                onSuccess = { jikanData ->
                    // Get user list status from MAL if available
                    val userStatus =
                        malResult?.getOrNull()?.myListStatus?.let { malStatus ->
                            AnimeListStatusYamal(
                                status = malStatus.status,
                                score = malStatus.score,
                                numEpisodesWatched = malStatus.numEpisodesWatched,
                                isRewatching = malStatus.isRewatching,
                                startDate = malStatus.startDate,
                                finishDate = malStatus.finishDate,
                                priority = malStatus.priority,
                                numTimesRewatched = malStatus.numTimesRewatched,
                                rewatchValue = malStatus.rewatchValue,
                                tags = malStatus.tags,
                                comments = malStatus.comments,
                                updatedAt = malStatus.updatedAt,
                            )
                        }

                    // Get characters (optional, don't fail if unavailable)
                    val characters =
                        charactersResult
                            .getOrNull()
                            ?.data
                            ?.take(15)
                            ?.map { it.toYamal() } ?: emptyList()

                    // Get reviews (optional, don't fail if unavailable)
                    val reviews =
                        reviewsResult
                            .getOrNull()
                            ?.data
                            ?.take(5)
                            ?.map { it.toYamal() } ?: emptyList()

                    jikanData.data.toYamal(userStatus, characters, reviews).right()
                },
                onFailure = { error ->
                    // If Jikan fails and we have MAL data, use MAL as fallback
                    // But for now, just return error
                    DataSourceError.Network(error.message ?: "Unknown error", error).left()
                },
            )
        }

    // ===========================================
    // User List Operations (MAL-only, enriched with Jikan)
    // ===========================================

    override fun getUserAnimeList(status: UserListStatusYamal): PagingSource<Int, AnimeForListYamal> =
        MalUserListPagingSource(
            malApiService = malApiService,
            status = status,
        )

    override suspend fun updateAnimeListStatus(
        animeId: Int,
        status: String?,
        score: Int?,
        numWatchedEpisodes: Int?,
        isRewatching: Boolean?,
        priority: Int?,
        numTimesRewatched: Int?,
        rewatchValue: Int?,
        tags: String?,
        comments: String?,
    ): Either<DataSourceError, AnimeListStatusYamal> =
        apiCallScope {
            malApiService.updateAnimeListStatus(
                animeId = animeId,
                status = status,
                score = score,
                numWatchedEpisodes = numWatchedEpisodes,
                isRewatching = isRewatching,
                priority = priority,
                numTimesRewatched = numTimesRewatched,
                rewatchValue = rewatchValue,
                tags = tags,
                comments = comments,
            )
        }.fold(
            ifLeft = { DataSourceError.Network(it.throwable.message ?: "Failed to update list").left() },
            ifRight = { malStatus ->
                AnimeListStatusYamal(
                    status = malStatus.status,
                    score = malStatus.score,
                    numEpisodesWatched = malStatus.numEpisodesWatched,
                    isRewatching = malStatus.isRewatching,
                    startDate = malStatus.startDate,
                    finishDate = malStatus.finishDate,
                    priority = malStatus.priority,
                    numTimesRewatched = malStatus.numTimesRewatched,
                    rewatchValue = malStatus.rewatchValue,
                    tags = malStatus.tags,
                    comments = malStatus.comments,
                    updatedAt = malStatus.updatedAt,
                ).right()
            },
        )

    override suspend fun deleteAnimeListStatus(animeId: Int): Either<DataSourceError, Unit> =
        apiCallScope {
            malApiService.deleteAnimeListStatus(animeId)
        }.fold(
            ifLeft = { DataSourceError.Network(it.throwable.message ?: "Failed to delete from list").left() },
            ifRight = { Unit.right() },
        )

    // ===========================================
    // MAL-specific Personalized Endpoints
    // ===========================================

    override suspend fun getAnimeSuggestions(limit: Int): Either<DataSourceError, List<AnimeForListYamal>> =
        if (!loginRepository.isUserAuthenticated().first()) {
            DataSourceError.Unauthorized.left()
        } else {
            apiCallScope {
                malApiService.getAnimeSuggestions(limit = limit, offset = 0)
            }.fold(
                ifLeft = { DataSourceError.Network(it.throwable.message ?: "Failed to get suggestions").left() },
                ifRight = { response ->
                    response.data
                        .map { edge ->
                            edge.node.toYamal()
                        }.right()
                },
            )
        }

    // ===========================================
    // Convenience Methods (Jikan-powered, limited results)
    // ===========================================

    override suspend fun getTrendingAnime(limit: Int): Either<DataSourceError, List<AnimeForListYamal>> =
        runCatching {
            jikanRateLimiter.acquire()
            jikanApiService.getSeasonNow(page = 1, limit = limit)
        }.fold(
            onSuccess = { response ->
                response.data.map { it.toYamal() }.right()
            },
            onFailure = { error ->
                DataSourceError.Network(error.message ?: "Failed to get trending anime", error).left()
            },
        )

    override suspend fun getTopAnimeList(limit: Int): Either<DataSourceError, List<AnimeForListYamal>> =
        runCatching {
            jikanRateLimiter.acquire()
            jikanApiService.getTopAnime(type = null, filter = null, page = 1, limit = limit)
        }.fold(
            onSuccess = { response ->
                response.data.map { it.toYamal() }.right()
            },
            onFailure = { error ->
                DataSourceError.Network(error.message ?: "Failed to get top anime", error).left()
            },
        )

    override suspend fun getUpcomingAnimeList(limit: Int): Either<DataSourceError, List<AnimeForListYamal>> =
        runCatching {
            jikanRateLimiter.acquire()
            jikanApiService.getSeasonUpcoming(page = 1, limit = limit)
        }.fold(
            onSuccess = { response ->
                response.data.map { it.toYamal() }.right()
            },
            onFailure = { error ->
                DataSourceError.Network(error.message ?: "Failed to get upcoming anime", error).left()
            },
        )

    // Private extension to convert MAL models (since we don't want to add this dependency everywhere)
    private fun com.yamal.platform.network.api.model.anime.AnimeForListMalNetwork.toYamal(): AnimeForListYamal =
        AnimeForListYamal(
            id = id,
            title = title,
            mainPicture =
                mainPicture?.let {
                    com.yamal.feature.anime.api.model
                        .PictureYamal(medium = it.medium, large = it.large)
                },
            rank = rank,
            members = numListUsers,
            mean = mean,
            mediaType =
                com.yamal.feature.anime.api.model.MediaTypeYamal
                    .fromSerializedValue(mediaType),
            userVote = null,
            startDate = startDate,
            endDate = endDate,
            numberOfEpisodes = numEpisodes,
            broadcast =
                broadcast?.let {
                    com.yamal.feature.anime.api.model.BroadcastYamal(
                        dayOfTheWeek = it.dayOfTheWeek ?: "",
                        startTime = it.startTime,
                    )
                },
        )
}
