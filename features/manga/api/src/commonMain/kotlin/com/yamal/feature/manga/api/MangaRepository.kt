package com.yamal.feature.manga.api

import arrow.core.Either
import com.yamal.feature.manga.api.model.MangaForDetailsYamal
import com.yamal.feature.manga.api.model.MangaForListYamal
import com.yamal.feature.manga.api.model.MangaListStatusYamal
import com.yamal.feature.manga.api.model.UserMangaListStatusYamal
import com.yamal.platform.network.api.model.list.MangaRankingEdgeMalNetwork
import com.yamal.platform.network.api.model.list.UserMangaListEdgeMalNetwork
import com.yamal.platform.utils.MalPagingSource

interface MangaRepository {
    fun getRanking(rankingType: String = "all"): MalPagingSource<MangaRankingEdgeMalNetwork, MangaForListYamal>

    fun getUserMangaList(status: UserMangaListStatusYamal): MalPagingSource<UserMangaListEdgeMalNetwork, MangaForListYamal>

    suspend fun getMangaDetails(id: Int): Either<String, MangaForDetailsYamal>

    suspend fun updateMangaListStatus(
        mangaId: Int,
        status: UserMangaListStatusYamal?,
        score: Int?,
        numVolumesRead: Int?,
        numChaptersRead: Int?,
        isRereading: Boolean? = null,
        priority: Int? = null,
        numTimesReread: Int? = null,
        rereadValue: Int? = null,
        tags: String? = null,
        comments: String? = null,
    ): Either<String, MangaListStatusYamal>

    suspend fun deleteMangaListStatus(mangaId: Int): Either<String, Unit>
}
