package com.yamal.feature.anime.implementation

import androidx.paging.PagingSource
import arrow.core.Either
import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.AnimeForDetailsYamal
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.AnimeListStatusYamal
import com.yamal.feature.anime.api.model.SeasonYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.platform.datasource.api.AnimeDataSource

class AnimeRepositoryImpl(
    private val animeDataSource: AnimeDataSource,
) : AnimeRepository {
    override fun getRanking(): PagingSource<Int, AnimeForListYamal> = animeDataSource.getTopAnime(filter = null, type = null)

    override fun getRankingByType(rankingType: String): PagingSource<Int, AnimeForListYamal> =
        animeDataSource.getTopAnime(filter = rankingType, type = null)

    override fun getSeasonal(
        season: SeasonYamal,
        year: String,
    ): PagingSource<Int, AnimeForListYamal> = animeDataSource.getSeasonalAnime(year = year.toInt(), season = season)

    override fun searchAnime(query: String): PagingSource<Int, AnimeForListYamal> = animeDataSource.searchAnime(query)

    override fun getCurrentSeasonAnime(): PagingSource<Int, AnimeForListYamal> = animeDataSource.getCurrentSeasonAnime()

    override fun getUpcomingSeasonAnime(): PagingSource<Int, AnimeForListYamal> = animeDataSource.getUpcomingAnime()

    override fun getUserAnimeList(status: UserListStatusYamal): PagingSource<Int, AnimeForListYamal> = animeDataSource.getUserAnimeList(status)

    override suspend fun getAnimeDetails(id: Int): Either<String, AnimeForDetailsYamal> =
        animeDataSource.getAnimeDetails(id).mapLeft { it.toErrorMessage() }

    override suspend fun updateAnimeListStatus(
        animeId: Int,
        status: UserListStatusYamal?,
        score: Int?,
        numWatchedEpisodes: Int?,
        isRewatching: Boolean?,
        priority: Int?,
        numTimesRewatched: Int?,
        rewatchValue: Int?,
        tags: String?,
        comments: String?,
    ): Either<String, AnimeListStatusYamal> =
        animeDataSource
            .updateAnimeListStatus(
                animeId = animeId,
                status = status?.serialName,
                score = score,
                numWatchedEpisodes = numWatchedEpisodes,
                isRewatching = isRewatching,
                priority = priority,
                numTimesRewatched = numTimesRewatched,
                rewatchValue = rewatchValue,
                tags = tags,
                comments = comments,
            ).mapLeft { it.toErrorMessage() }

    override suspend fun deleteAnimeListStatus(animeId: Int): Either<String, Unit> =
        animeDataSource.deleteAnimeListStatus(animeId).mapLeft { it.toErrorMessage() }

    override suspend fun getAnimeSuggestions(limit: Int): Either<String, List<AnimeForListYamal>> =
        animeDataSource.getAnimeSuggestions(limit).mapLeft { it.toErrorMessage() }

    override suspend fun getTrendingAnime(limit: Int): Either<String, List<AnimeForListYamal>> =
        animeDataSource.getTrendingAnime(limit).mapLeft { it.toErrorMessage() }

    override suspend fun getTopAnime(limit: Int): Either<String, List<AnimeForListYamal>> =
        animeDataSource.getTopAnimeList(limit).mapLeft { it.toErrorMessage() }

    override suspend fun getUpcomingAnime(limit: Int): Either<String, List<AnimeForListYamal>> =
        animeDataSource.getUpcomingAnimeList(limit).mapLeft { it.toErrorMessage() }
}
