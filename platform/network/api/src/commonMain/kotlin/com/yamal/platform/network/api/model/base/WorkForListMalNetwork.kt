package com.yamal.platform.network.api.model.base

import com.yamal.platform.network.api.model.common.AlternativeTitlesMalNetwork
import com.yamal.platform.network.api.model.common.GenreMalNetwork

/**
 * Interface for work types with list-level fields.
 * Corresponds to the WorkForList schema in the MAL API specification.
 * Extends WorkBaseMalNetwork with additional common fields for anime/manga lists.
 */
interface WorkForListMalNetwork : WorkBaseMalNetwork {
    val alternativeTitles: AlternativeTitlesMalNetwork?
    val startDate: String?
    val endDate: String?
    val synopsis: String?
    val mean: Float?
    val rank: Int?
    val popularity: Int?
    val numListUsers: Int?
    val numScoringUsers: Int?
    val nsfw: String?
    val genres: List<GenreMalNetwork>
    val createdAt: String?
    val updatedAt: String?
}
