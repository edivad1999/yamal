package com.yamal.feature.user.implementation

import arrow.core.Either
import com.yamal.feature.user.api.UserRepository
import com.yamal.feature.user.api.model.UserProfileYamal
import com.yamal.feature.user.implementation.mapping.toYamal
import com.yamal.platform.network.api.ApiService
import com.yamal.platform.network.api.apiCallScope

class UserRepositoryImpl(
    private val apiService: ApiService,
) : UserRepository {
    override suspend fun getUserProfile(): Either<String, UserProfileYamal> =
        apiCallScope {
            apiService.getUserProfile().toYamal()
        }.mapLeft { it.throwable.message ?: "Error retrieving user profile" }
}
