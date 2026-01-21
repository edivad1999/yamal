package com.yamal.platform.network.api.model.base

import com.yamal.platform.network.api.model.common.PictureMalNetwork

/**
 * Base sealed interface for all work types (Anime, Manga).
 * Corresponds to the WorkBase schema in the MAL API specification.
 */
sealed interface WorkBaseMalNetwork {
    val id: Int
    val title: String
    val mainPicture: PictureMalNetwork?
}
