package com.yamal.feature.home.api

import com.yamal.feature.home.api.model.CounterModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun counterFlow(): Flow<CounterModel>

    suspend fun setCounter(number: Int)
}
