package com.yamal.feature.login.implementation

import com.yamal.feature.core.PKCEGenerator
import com.yamal.feature.login.api.LoginRepository
import com.yamal.feature.network.api.ApiService
import com.yamal.feature.network.api.BuildConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LoginRepositoryImpl(
    private val buildConstants: BuildConstants,
    private val apiService: ApiService,
) : LoginRepository {

    val userAuthenticated = MutableStateFlow(false)

    override fun isUserAuthenticated(): Flow<Boolean> = userAuthenticated

    override fun getAuthorizationUrl() =
        "$MAL_AUTH_URL?response_type=$RESPONSE_TYPE_PARAM" +
            "&client_id=${buildConstants.malClientId}" +
            "&code_challenge=${PKCEGenerator.generate(PKCE_LENGTH)}"

    override suspend fun authenticate(authorizationCode: String) {
        if (authorizationCode.isNotBlank()) {
            userAuthenticated.emit(true)
        } else {
            userAuthenticated.emit(false)
        }
    }

    companion object {

        const val MAL_AUTH_URL = "https://myanimelist.net/v1/oauth2/authorize"
        const val RESPONSE_TYPE_PARAM = "code"
        const val PKCE_LENGTH = 128
    }
}
