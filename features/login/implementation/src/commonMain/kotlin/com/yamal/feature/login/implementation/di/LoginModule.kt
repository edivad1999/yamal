package com.yamal.feature.login.implementation.di

import com.yamal.feature.login.api.LoginRepository
import com.yamal.feature.login.implementation.LoginRepositoryImpl
import org.koin.dsl.module

object LoginModule {
    operator fun invoke() =
        module {
            single<LoginRepository> {
                LoginRepositoryImpl(buildConstants = get(), apiService = get(), preferencesDatasource = get())
            }
        }
}
