package com.yamal.feature.network.api

import io.ktor.client.HttpClient

interface KtorFactory {

    fun createClient(): HttpClient
}
