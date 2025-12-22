package com.yamal.platform.storage.api

import com.yamal.platform.network.api.model.AccessToken

interface PreferencesDatasource {
    fun getAccessToken(): AccessToken?

    fun setAccessToken(accessToken: AccessToken)
}
