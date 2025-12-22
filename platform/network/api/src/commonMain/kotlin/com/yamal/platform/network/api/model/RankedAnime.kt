package com.yamal.platform.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class RankedAnime(
    @SerialName("node") val node: Anime,
    @SerialName("ranking") val ranking: RankingInfo,
)
