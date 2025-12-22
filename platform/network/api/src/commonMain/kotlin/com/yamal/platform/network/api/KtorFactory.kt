package com.yamal.platform.network.api

import io.ktor.client.HttpClient

interface KtorFactory {
    fun createClient(): HttpClient
}
