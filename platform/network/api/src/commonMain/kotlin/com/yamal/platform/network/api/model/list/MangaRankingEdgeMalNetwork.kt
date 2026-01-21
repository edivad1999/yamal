package com.yamal.platform.network.api.model.list

import com.yamal.platform.network.api.model.common.RankingInfoMalNetwork
import com.yamal.platform.network.api.model.manga.MangaForListMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a manga with ranking information from the MAL API.
 * Used in manga ranking list responses.
 */
@Serializable
data class MangaRankingEdgeMalNetwork(
    @SerialName("node") val node: MangaForListMalNetwork,
    @SerialName("ranking") val ranking: RankingInfoMalNetwork? = null,
)
