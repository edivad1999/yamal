package com.yamal.feature.network.api

import arrow.core.Either
import arrow.core.left
import arrow.core.right

suspend fun <T> apiCallScope(block: suspend () -> T): Either<ApiError, T> = runCatching {
    block().right()
}.getOrElse {
    ApiError(it).left()
}

data class ApiError(val throwable: Throwable)
