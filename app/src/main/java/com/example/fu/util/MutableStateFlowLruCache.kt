package com.example.fu.util

import android.util.LruCache
import kotlinx.coroutines.flow.MutableStateFlow

// TODO: (Discuss) Use it instead getOrPut or remove it.
class MutableStateFlowLruCache<K, I>(maxSize: Int) : LruCache<K, MutableStateFlow<I?>>(maxSize) {
    override fun create(key: K): MutableStateFlow<I?> {
        return MutableStateFlow(null)
    }
}