package com.yamal.platform.network.api.model.list

import com.yamal.platform.network.api.model.anime.AnimeForListMalNetwork
import com.yamal.platform.network.api.model.common.RankingInfoMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an anime with ranking information from the MAL API.
 * Corresponds to the AnimeRankingEdge schema in the API specification.
 */
@Serializable
data class AnimeRankingEdgeMalNetwork(
    @SerialName("node") val node: AnimeForListMalNetwork,
    @SerialName("ranking") val ranking: RankingInfoMalNetwork,
)
