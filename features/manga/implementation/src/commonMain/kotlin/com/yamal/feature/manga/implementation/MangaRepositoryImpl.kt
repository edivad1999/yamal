package com.yamal.feature.manga.implementation

import arrow.core.Either
import com.yamal.feature.manga.api.MangaRepository
import com.yamal.feature.manga.api.model.MangaForDetailsYamal
import com.yamal.feature.manga.api.model.MangaForListYamal
import com.yamal.feature.manga.api.model.MangaListStatusYamal
import com.yamal.feature.manga.api.model.UserMangaListStatusYamal
import com.yamal.feature.manga.implementation.mapping.toYamal
import com.yamal.platform.network.api.ApiService
import com.yamal.platform.network.api.apiCallScope
import com.yamal.platform.network.api.model.list.MangaRankingEdgeMalNetwork
import com.yamal.platform.network.api.model.list.UserMangaListEdgeMalNetwork
import com.yamal.platform.utils.MalPagingSource

class MangaRepositoryImpl(
    private val apiService: ApiService,
) : MangaRepository {
    override fun getRanking(rankingType: String): MalPagingSource<MangaRankingEdgeMalNetwork, MangaForListYamal> =
        MalPagingSource(
            apiCall = { pageSize, offset ->
                apiCallScope {
                    apiService.getMangaRanking(rankingType = rankingType, limit = pageSize, offset = offset)
                }
            },
            map = { it.node.toYamal() },
        )

    override fun getUserMangaList(status: UserMangaListStatusYamal): MalPagingSource<UserMangaListEdgeMalNetwork, MangaForListYamal> =
        MalPagingSource(
            apiCall = { pageSize, offset ->
                apiCallScope {
                    apiService.getUserMangaList(
                        userListStatus = status.serialName,
                        limit = pageSize,
                        offset = offset,
                    )
                }
            },
            map = { it.node.toYamal() },
        )

    override suspend fun getMangaDetails(id: Int): Either<String, MangaForDetailsYamal> =
        apiCallScope {
            apiService.getMangaDetails(id).toYamal()
        }.mapLeft { it.throwable.message ?: "Error retrieving manga details" }

    override suspend fun updateMangaListStatus(
        mangaId: Int,
        status: UserMangaListStatusYamal?,
        score: Int?,
        numVolumesRead: Int?,
        numChaptersRead: Int?,
        isRereading: Boolean?,
        priority: Int?,
        numTimesReread: Int?,
        rereadValue: Int?,
        tags: String?,
        comments: String?,
    ): Either<String, MangaListStatusYamal> =
        apiCallScope {
            apiService
                .updateMangaListStatus(
                    mangaId = mangaId,
                    status = status?.serialName,
                    score = score,
                    numVolumesRead = numVolumesRead,
                    numChaptersRead = numChaptersRead,
                    isRereading = isRereading,
                    priority = priority,
                    numTimesReread = numTimesReread,
                    rereadValue = rereadValue,
                    tags = tags,
                    comments = comments,
                ).toYamal()
        }.mapLeft { it.throwable.message ?: "Error updating manga list status" }

    override suspend fun deleteMangaListStatus(mangaId: Int): Either<String, Unit> =
        apiCallScope {
            apiService.deleteMangaListStatus(mangaId)
        }.mapLeft { it.throwable.message ?: "Error deleting manga from list" }
}
