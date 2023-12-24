package com.yamal.feature.network.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AlternativeTitles(
   val synonyms: List<String>?,
    val en: String?,
    val ja: String?
)
