package com.yamal.feature.network.implementation

import com.yamal.feature.network.api.KtorFactory

class KtorFactoryImpl : KtorFactory {

    override fun createClient() {
        BuildKonfig.malClientId
    }
}
