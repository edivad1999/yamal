package com.yamal.feature.user.api

import arrow.core.Either
import com.yamal.feature.user.api.model.UserProfileYamal

interface UserRepository {
    suspend fun getUserProfile(): Either<String, UserProfileYamal>
}
