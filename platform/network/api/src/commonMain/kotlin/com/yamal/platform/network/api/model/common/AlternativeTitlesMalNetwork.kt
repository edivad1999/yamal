package com.yamal.platform.network.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents alternative titles from the MAL API.
 * Part of the WorkForList schema in the API specification.
 */
@Serializable
data class AlternativeTitlesMalNetwork(
    @SerialName("synonyms") val synonyms: List<String>? = null,
    @SerialName("en") val en: String? = null,
    @SerialName("ja") val ja: String? = null,
)
