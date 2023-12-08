package com.yamal.feature.home.implementation

import com.yamal.feature.home.api.HomeRepository
import com.yamal.feature.home.api.model.CounterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class HomeRepositoryImpl : HomeRepository {

    private val cachedCounter: MutableStateFlow<CounterModel> = MutableStateFlow(CounterModel(0))
    override fun counterFlow(): Flow<CounterModel> = cachedCounter
    override suspend fun setCounter(number: Int) {
        cachedCounter.emit(CounterModel(number))
    }
}
