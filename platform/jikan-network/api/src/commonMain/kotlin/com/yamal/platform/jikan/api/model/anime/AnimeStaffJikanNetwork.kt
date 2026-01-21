package com.yamal.platform.jikan.api.model.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeStaffJikanNetwork(
    @SerialName("person") val person: PersonMetaJikanNetwork,
    @SerialName("positions") val positions: List<String> = emptyList(),
)
