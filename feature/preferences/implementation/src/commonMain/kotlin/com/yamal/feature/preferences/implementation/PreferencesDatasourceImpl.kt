package com.yamal.feature.preferences.implementation

import com.russhwolf.settings.Settings
import com.yamal.feature.network.api.model.AccessToken
import com.yamal.feature.preferences.api.PreferencesDatasource
import com.yamal.feature.preferences.implementation.extensions.string

class PreferencesDatasourceImpl(settings: Settings) : PreferencesDatasource {

    private var access: String by settings.string()
    private var refresh: String by settings.string()
    override fun getAccessToken(): AccessToken? {
        if (access.isBlank() || refresh.isBlank()) return null
        return AccessToken(accessToken = access, refreshToken = refresh)
    }

    override fun setAccessToken(accessToken: AccessToken) {
        access = accessToken.accessToken
        refresh = accessToken.accessToken
    }
}
