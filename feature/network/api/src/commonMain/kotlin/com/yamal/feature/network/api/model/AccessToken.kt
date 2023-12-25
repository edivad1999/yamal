package com.yamal.feature.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class AccessToken(
    @SerialName("token_type") val tokenType: String = "",
    @SerialName("expires_in") val expiresIn: Int = 0,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
)
