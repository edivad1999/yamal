package com.yamal.feature.login.implementation

import com.yamal.platform.utils.PKCEGenerator
import com.yamal.feature.login.api.LoginRepository
import com.yamal.platform.network.api.ApiService
import com.yamal.platform.network.api.BuildConstants
import com.yamal.platform.network.api.apiCallScope
import com.yamal.platform.storage.api.PreferencesDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class LoginRepositoryImpl(
    private val buildConstants: BuildConstants,
    private val apiService: ApiService,
    private val preferencesDatasource: PreferencesDatasource,
) : LoginRepository {

    private val codeChallenge = PKCEGenerator.generate(PKCE_LENGTH)
    private val userAuthenticated =
        flow {
            while (true) {
                emit(preferencesDatasource.getAccessToken())
                kotlinx.coroutines.delay(100)
            }
        }.flowOn(Dispatchers.IO)

    override fun isUserAuthenticated(): Flow<Boolean> = userAuthenticated.map { it != null }

    override fun getAuthorizationUrl() =
        "$MAL_AUTH_URL?response_type=$RESPONSE_TYPE_PARAM" +
            "&client_id=${buildConstants.malClientId}" +
            "&code_challenge=$codeChallenge"

    override suspend fun authenticate(authorizationCode: String) {
        apiCallScope {
            apiService.getAccessToken(
                code = authorizationCode,
                codeChallenge = codeChallenge,
                grantType = GRANT_TYPE,
            )
        }.onRight {
            preferencesDatasource.setAccessToken(it)
        }
    }

    companion object {

        const val MAL_AUTH_URL = "https://myanimelist.net/v1/oauth2/authorize"
        const val RESPONSE_TYPE_PARAM = "code"
        const val PKCE_LENGTH = 128
        const val GRANT_TYPE = "authorization_code"
    }
}
