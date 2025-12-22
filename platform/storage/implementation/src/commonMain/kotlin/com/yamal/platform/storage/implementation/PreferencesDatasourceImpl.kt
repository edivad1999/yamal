package com.yamal.platform.storage.implementation

import com.russhwolf.settings.Settings
import com.yamal.platform.network.api.model.AccessToken
import com.yamal.platform.storage.api.PreferencesDatasource
import com.yamal.platform.storage.implementation.extensions.string

class PreferencesDatasourceImpl(
    settings: Settings,
) : PreferencesDatasource {
    private var access: String by settings.string()
    private var refresh: String by settings.string()

    override fun getAccessToken(): AccessToken? {
        if (access.isBlank() || refresh.isBlank()) return null
        return AccessToken(accessToken = access, refreshToken = refresh)
    }

    override fun setAccessToken(accessToken: AccessToken) {
        access = accessToken.accessToken
        refresh = accessToken.refreshToken
    }
}
