package com.yamal.feature.user.implementation.di

import com.yamal.feature.user.api.UserRepository
import com.yamal.feature.user.implementation.UserRepositoryImpl
import org.koin.dsl.module

object UserModule {
    operator fun invoke() =
        module {
            single<UserRepository> {
                UserRepositoryImpl(apiService = get())
            }
        }
}
