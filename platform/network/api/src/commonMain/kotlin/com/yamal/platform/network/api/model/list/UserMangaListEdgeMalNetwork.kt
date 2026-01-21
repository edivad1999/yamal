package com.yamal.platform.network.api.model.list

import com.yamal.platform.network.api.model.manga.MangaForListMalNetwork
import com.yamal.platform.network.api.model.manga.MangaListStatusMalNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user's manga list item from the MAL API.
 * Corresponds to the UserMangaListEdge schema in the API specification.
 */
@Serializable
data class UserMangaListEdgeMalNetwork(
    @SerialName("node") val node: MangaForListMalNetwork,
    @SerialName("list_status") val listStatus: MangaListStatusMalNetwork? = null,
)
