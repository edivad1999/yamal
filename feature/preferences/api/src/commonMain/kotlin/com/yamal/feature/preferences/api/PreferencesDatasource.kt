package com.yamal.feature.preferences.api

import com.yamal.feature.network.api.model.AccessToken

interface PreferencesDatasource {

    fun getAccessToken(): AccessToken?
    fun setAccessToken(accessToken: AccessToken)
}
