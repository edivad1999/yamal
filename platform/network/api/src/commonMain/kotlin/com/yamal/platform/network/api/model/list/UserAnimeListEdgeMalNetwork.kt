package com.yamal.platform.network.api.model.list

import com.yamal.platform.network.api.model.anime.AnimeForListMalNetwork
import com.yamal.platform.network.api.model.anime.AnimeListStatusMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user's anime list item from the MAL API.
 * Corresponds to the UserAnimeListEdge schema in the API specification.
 */
@Serializable
data class UserAnimeListEdgeMalNetwork(
    @SerialName("node") val node: AnimeForListMalNetwork,
    @SerialName("list_status") val listStatus: AnimeListStatusMalNetwork? = null,
)
