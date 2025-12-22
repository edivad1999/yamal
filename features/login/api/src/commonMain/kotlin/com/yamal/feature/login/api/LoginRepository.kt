package com.yamal.feature.login.api

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun getAuthorizationUrl(): String

    fun isUserAuthenticated(): Flow<Boolean>

    suspend fun authenticate(authorizationCode: String)
}
