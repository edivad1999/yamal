package com.yamal.feature.anime.implementation

import com.yamal.feature.anime.api.AnimeRepository
import com.yamal.feature.anime.api.model.GenericAnime
import com.yamal.feature.anime.api.model.MediaType
import com.yamal.feature.anime.api.model.Season
import com.yamal.feature.anime.api.model.UserListStatus
import com.yamal.feature.anime.api.model.toDomain
import com.yamal.feature.anime.implementation.mapping.toModel
import com.yamal.platform.utils.MalPagingSource
import com.yamal.platform.network.api.ApiService
import com.yamal.platform.network.api.apiCallScope
import com.yamal.platform.network.api.model.Anime
import com.yamal.platform.network.api.model.RankedAnime

class AnimeRepositoryImpl(private val apiService: ApiService) : AnimeRepository {

    override fun getRanking(): MalPagingSource<RankedAnime, GenericAnime> =
        MalPagingSource(apiCall = { pageSize, offset ->
            apiCallScope {
                apiService.getAnimeRanking(limit = pageSize, offset = offset)
            }
        }, map = {
            GenericAnime(
                id = it.node.id,
                title = it.node.title,
                mainPicture = it.node.mainPictureNetwork?.toModel(),
                rank = it.ranking.rank,
                mediaType = MediaType.fromSerializedValue(it.node.mediaType),
                members = it.node.numListUsers,
                mean = it.node.mean,
                userVote = it.node.myListStatusNetwork?.score,
                startDate = it.node.startDate,
                endDate = it.node.endDate,
                numberOfEpisodes = it.node.numEpisodes,
            )
        })

    override fun getSeasonal(
        season: Season,
        year: String,
    ): MalPagingSource<Anime, GenericAnime> =
        MalPagingSource(apiCall = { pageSize, offset ->
            apiCallScope {
                apiService.getSeasonalAnime(limit = pageSize, offset = offset, season = season.serialName, year = year)
            }
        }, map = {
            GenericAnime(
                id = it.id,
                title = it.title,
                mainPicture = it.mainPictureNetwork?.toModel(),
                rank = it.rank,
                mediaType = MediaType.fromSerializedValue(it.mediaType),
                members = it.numListUsers,
                mean = it.mean,
                userVote = it.myListStatusNetwork?.score,
                startDate = it.startDate,
                endDate = it.endDate,
                numberOfEpisodes = it.numEpisodes,
            )
        })

    override fun getUserAnimeList(status: UserListStatus): MalPagingSource<Anime, GenericAnime> =
        MalPagingSource(apiCall = { pageSize, offset ->
            apiCallScope {
                apiService.getUserAnimeList(limit = pageSize, offset = offset, userListStatus = status.serialName)
            }
        }, map = {
            GenericAnime(
                id = it.id,
                title = it.title,
                mainPicture = it.mainPictureNetwork?.toModel(),
                rank = it.rank,
                mediaType = MediaType.fromSerializedValue(it.mediaType),
                members = it.numListUsers,
                mean = it.mean,
                userVote = it.myListStatusNetwork?.score,
                startDate = it.startDate,
                endDate = it.endDate,
                numberOfEpisodes = it.numEpisodes,
            )
        })

    override suspend fun getAnimeDetails(id: Int) =
        apiCallScope {
            apiService.getAnimeDetails(id).toDomain()
        }.mapLeft { it.throwable.message ?: "Error retrieving anime details" }
}
