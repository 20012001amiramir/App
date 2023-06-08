package com.example.fu.util

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * This is temp wrapper to use StateFlow without initial value, distinct and cache capabilities.
 * Change this to StateFlow once it released.
 */
class PublishFlow<T> : Flow<T> {
    private val stateFlow = MutableStateFlow<T?>(null) // Initial null

    fun pass(value: T) {
        stateFlow.value = value
        stateFlow.value = null // This removes distinct and cache behavior of the StateFlow.
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        collector.emitAll(
            stateFlow.filterNotNull() // Filter nulls
        )
    }
}