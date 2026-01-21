package com.yamal.platform.datasource.implementation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.BroadcastYamal
import com.yamal.feature.anime.api.model.MediaTypeYamal
import com.yamal.feature.anime.api.model.PictureYamal
import com.yamal.feature.anime.api.model.UserListStatusYamal
import com.yamal.platform.network.api.ApiService

/**
 * PagingSource for user's anime list from MAL API.
 * Uses offset-based pagination like the existing MalPagingSource.
 */
class MalUserListPagingSource(
    private val malApiService: ApiService,
    private val status: UserListStatusYamal,
) : PagingSource<Int, AnimeForListYamal>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeForListYamal> {
        val offset = params.key ?: 0

        return try {
            val response =
                malApiService.getUserAnimeList(
                    userListStatus = status.serialName,
                    limit = params.loadSize,
                    offset = offset,
                )

            val items =
                response.data.map { edge ->
                    val node = edge.node
                    AnimeForListYamal(
                        id = node.id,
                        title = node.title,
                        mainPicture =
                            node.mainPicture?.let {
                                PictureYamal(medium = it.medium, large = it.large)
                            },
                        rank = node.rank,
                        members = node.numListUsers,
                        mean = node.mean,
                        mediaType = MediaTypeYamal.fromSerializedValue(node.mediaType),
                        userVote = edge.listStatus?.score,
                        startDate = node.startDate,
                        endDate = node.endDate,
                        numberOfEpisodes = node.numEpisodes,
                        broadcast =
                            node.broadcast?.let {
                                BroadcastYamal(
                                    dayOfTheWeek = it.dayOfTheWeek ?: "",
                                    startTime = it.startTime,
                                )
                            },
                    )
                }

            LoadResult.Page(
                data = items,
                prevKey = if (offset == 0) null else (offset - params.loadSize).coerceAtLeast(0),
                nextKey = if (response.paging?.next == null) null else offset + params.loadSize,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeForListYamal>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize) ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
}
