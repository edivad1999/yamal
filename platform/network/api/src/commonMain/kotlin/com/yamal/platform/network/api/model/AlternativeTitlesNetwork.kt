package com.yamal.platform.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class AlternativeTitlesNetwork(
    @SerialName("synonyms") val synonyms: List<String>?,
    @SerialName("en") val en: String?,
    @SerialName("ja") val ja: String?,
)
