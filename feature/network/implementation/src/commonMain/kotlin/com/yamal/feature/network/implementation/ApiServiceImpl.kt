package com.yamal.feature.network.implementation

import com.yamal.feature.network.api.ApiService
import io.ktor.client.HttpClient

class ApiServiceImpl(private val httpClient: HttpClient) : ApiService
